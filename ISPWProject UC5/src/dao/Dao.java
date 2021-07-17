package dao;

import control.Controller;
import daoResources.DaoResources;
import entity.Booking;
import entity.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    /**
     * Classe di comunicazione con il database attraverso una serie di metodi che permettono le varie operazioni sul
     * database in base al tipo di filtro utilizzato. Tutte le combinazioni dei vari filtri sono state gestite in
     * quattro metodi riconosciuti in base alle combinazioni di 'corso' e 'sessione'
     */
    private String[] connData;
    private Connection conn;
    private Controller controller = new Controller();
    public Dao() { }

    public Dao(String[] connData) {
        this.connData = connData;
    }



    private ArrayList<Booking> initializationBooking(ResultSet rs) throws SQLException {
        /*Fase di inizializzazione degli oggetti booking a partire dai risultati ottenuti dalle query effettuate sul
        * database. Restituisce una lista di oggetti booking */
        ArrayList<Booking> bookingsList = new ArrayList<>();
        while(rs.next()) {
            Booking booking = new Booking();
            booking.setIdBooking(rs.getString(1));
            booking.setIdBooker(rs.getString(2));
            booking.setEvent_type(rs.getString(3));
            booking.setBookerName(controller.firstLetterUp(rs.getString(4)));
            booking.setBookerSurname(controller.firstLetterUp(rs.getString(5)));
            booking.setDate(rs.getString(6));
            booking.setBegin(rs.getString(7));
            booking.setEnd(rs.getString(8));
            booking.setCourse(controller.firstLetterUp(rs.getString(9)));
            booking.setBookingDate(rs.getString(10));
            booking.setSession(controller.firstLetterUp(rs.getString(11)));
            ResultSet rs1 = findClassrooms(booking.getIdBooking());
            String classrooms = "";
            while (rs1.next()){
                classrooms += rs1.getString(1);
                if(!rs1.isLast()){
                    classrooms += ", ";
                }
            }
            booking.setClassroom(classrooms);
            bookingsList.add(booking);
        }
        return bookingsList;
    }

    //course e session = null
    public List<Booking> req1(Event event) throws SQLException, ClassNotFoundException {
        /*Metodo utilizzato quando 'corso' e 'sessione' risultano vuoti, ovvero non inseriti dall'utente. In particolare
        * distingue due tipi di query: una relativa al caso di data inserita; una relativa al caso di data non inserita
        * e sostituita con la data corrente. In particolare la seconda query permette la ricerca a partire da quella
        * data in poi*/
        ArrayList<Booking> bookingsList;
        conn = DaoResources.getConnection(connData);
        String query = "select distinct idbooking, idbooker,event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                "where event_type = ? and date = ? and begin >= ? and public.\"Bookings\".end <= ? and idbooker = ?";
        String date = event.getDate();
        if(date.isEmpty()){
            date = controller.currentDate();
            query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                    "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                    "where event_type = ? and date >= ? and begin >= ? and public.\"Bookings\".end <= ? and idbooker = ?";
        }
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, event.getEvent_type());
        statement.setString(2, date);
        statement.setString(3, event.getBegin());
        statement.setString(4, event.getEnd());
        statement.setString(5,event.getTeacherId());
        ResultSet rs = statement.executeQuery();
        bookingsList = initializationBooking(rs);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }

    //course e session != null
    public List<Booking> req2(Event event) throws SQLException, ClassNotFoundException {
        /*Metodo utilizzato quando 'corso' e 'sessione' sono stati inseriti dall'utente. In particolare
         * distingue due tipi di query: una relativa al caso di data inserita; una relativa al caso di data non inserita
         * e sostituita con la data corrente. In particolare la seconda query permette la ricerca a partire da quella
         * data in poi*/
        List<Booking> bookingsList;
        conn = DaoResources.getConnection(connData);
        PreparedStatement statement;
        String query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                "where event_type = ? and session = ? and date = ? and begin >= ? and public.\"Bookings\".end <= ? and course = ? and idbooker = ?";
        String date = event.getDate();
        if(date.isEmpty()){
            date = controller.currentDate();
            query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                    "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                    "where event_type = ? and session = ? and date >= ? and begin >= ? and public.\"Bookings\".end <= ? and course = ? and idbooker = ?";
        }
        statement = conn.prepareStatement(query);
        statement.setString(1,event.getEvent_type());
        statement.setString(2,event.getSession());
        statement.setString(3,date);
        statement.setString(4,event.getBegin());
        statement.setString(5,event.getEnd());
        statement.setString(6,event.getCourse());
        statement.setString(7,event.getTeacherId());
        ResultSet rs = statement.executeQuery();
        bookingsList = initializationBooking(rs);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }

    //Solo course = null
    public List<Booking> req3(Event event) throws SQLException, ClassNotFoundException {
        /*Metodo utilizzato quando 'corso' risulta vuoto, ovvero non inserito dall'utente, e 'sessione' risulta pieno.
         * In particolare distingue due tipi di query: una relativa al caso di data inserita; una relativa al caso di data non inserita
         * e sostituita con la data corrente. In particolare la seconda query permette la ricerca a partire da quella
         * data in poi*/
        List<Booking> bookingsList;
        conn = DaoResources.getConnection(connData);
        String query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                "where event_type = ? and session = ? and date = ? and begin >= ? and public.\"Bookings\".end <= ? and idbooker = ?";
        String date = event.getDate();
        if(date.isEmpty()){
            date = controller.currentDate();
            query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course,timestamp, session\n" +
                    "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                    "where event_type = ? and session = ? and date >= ? and begin >= ? and public.\"Bookings\".end <= ? and idbooker = ?";
        }
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1,event.getEvent_type());
        statement.setString(2,event.getSession());
        statement.setString(4,date);
        statement.setString(5,event.getBegin());
        statement.setString(6,event.getEnd());
        statement.setString(7,event.getTeacherId());

        ResultSet rs = statement.executeQuery();
        bookingsList = initializationBooking(rs);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }

    //solo session = null
    public List<Booking> req4(Event event) throws SQLException, ClassNotFoundException {
        /*Metodo utilizzato quando 'sessione' risulta vuoto, ovvero non inserito dall'utente, e 'corso' risulta pieno.
         * In particolare distingue due tipi di query: una relativa al caso di data inserita; una relativa al caso di data non inserita
         * e sostituita con la data corrente. In particolare la seconda query permette la ricerca a partire da quella
         * data in poi*/
        List<Booking> bookingsList;
        conn = DaoResources.getConnection(connData);
        PreparedStatement statement;
        String query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                "where event_type = ? and date = ? and begin >= ? and public.\"Bookings\".end <= ? and course = ? and idbooker = ?";
        String date = event.getDate();
        if(date.isEmpty()){
            date = controller.currentDate();
            query = "select distinct idbooking,idbooker, event_type, name, surname, date,begin, public.\"Bookings\".end, course, timestamp, session\n" +
                    "from public.\"Bookings\" join public.\"Bookers\" on booker = idbooker\n" +
                    "where event_type = ? and date >= ? and begin >= ? and public.\"Bookings\".end <= ? and course = ? and idbooker = ?";
        }
        statement = conn.prepareStatement(query);
        statement.setString(1,event.getEvent_type());
        statement.setString(2,date);
        statement.setString(3,event.getBegin());
        statement.setString(4,event.getEnd());
        statement.setString(5,event.getCourse());
        statement.setString(6,event.getTeacherId());
        ResultSet rs = statement.executeQuery();
        bookingsList = initializationBooking(rs);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }
    private ResultSet findClassrooms(String bookingId) throws SQLException {
        /*Ricerca delle aule relative alla medesima prenotazione. Utilizzata per riempire l'attributo 'classrooms'
        * nell'oggetto booking*/
        PreparedStatement statement;
        String query = "select classroom\n" +
                "from public.\"Bookings\"\n" +
                "where idbooking = ? order by classroom asc";
        statement = conn.prepareStatement(query);
        statement.setString(1,bookingId);
        return statement.executeQuery();
    }
}
