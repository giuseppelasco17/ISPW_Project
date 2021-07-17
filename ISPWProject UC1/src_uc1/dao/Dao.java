package dao;

import daoResources.DaoResources;
import entity.CreationRequest;
import entity.DeleteRequest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dao {
    /**
     * Questa classe contiene i metodi che permettono la comunicazione con il database. Tali metodi vengono richiamati
     * dal controller.
     */

    private String[] connData;
    private Connection conn;


    public Dao(String[] connData) {
        this.connData = connData;
    }

    public List availabilityRooms(List crooms, int i, CreationRequest cReq) {
        /*Ricevo come parametro una lista di aule fisicamente esistenti, e con questo metodo ne verifico la disponiilità.
        * Il parametro intero indica il tentativo di verifica, ovvero il numero di aule su cui fare la verifica.
        * (ES: Se il parametro è uguale a 2, il ciclo scorre la lista 2 elementi alla volta fin quando non trova una coppia
        * realmente prenotabile, cioè disponibile in quel giorno ed in quella fascia oraria. Se la lista finisce senza
        * esito positivo, ritorna una lista vuota che implica il passaggio al terzo ed ultimo tentativo).*/
        List<String> avRooms = new ArrayList<>();
        int j, k, n = i;
        boolean flag;
        for (j = 0; j < crooms.size(); j += n) {
            flag = true;
            for (k = j; k < j + n; k++) {
                if (!checkRoomOnDB((String) crooms.get(k), cReq)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                while (n > 0) {
                    avRooms.add((String) crooms.get(k - n));
                    n--;
                }
                break;
            }
        }
        return avRooms;
    }

    private boolean checkRoomOnDB(String room, CreationRequest cReq) {
        /*Metodo che controlla la disponibilità sul database per permettere l'inserimento di una nuova prenotazione.
        * Esegue un controllo evitando accavallamenti di fasceorarie sulla medesima aula nello stasso giorno.
        * Include due query, una per il caso di richiesta di creazione e una nel caso di modifica.
        * Restituisce true se non ci sono corrispondenze e quindi è possibile effettuare la prenotazione.*/
        boolean flag = false;
        try {
            conn = DaoResources.getConnection(connData);
            String query = "select *\n" + "from public.\"Bookings\"\n" + "where \"Bookings\".classroom = ? and " +
                    "\"Bookings\".date = ? and ((\"Bookings\".begin <= ? and \"Bookings\".end > ?)" +
                    " or\n" + "\t\t\t\t\t\t\t(\"Bookings\".begin > ? and \"Bookings\".end <= ?) or \n" +
                    "\t\t\t\t\t\t\t(\"Bookings\".begin <= ? and \"Bookings\".end > ?)or\n" +
                    "\t\t\t\t\t\t\t(? >= \"Bookings\".begin and ? < \"Bookings\".end))";

            /*La modifQuery fa il controllo escludendo la vecchia prenotazione, poichè, questa in caso affermativo,
            * verrà eliminata*/
            String modifQuery = "select *\n" + "from public.\"Bookings\"\n" + "where \"Bookings\".classroom = ? and " +
                    "\"Bookings\".date = ? and ((\"Bookings\".begin <= ? and \"Bookings\".end > ?)" +
                    " or\n" + "\t\t\t\t\t\t\t(\"Bookings\".begin > ? and \"Bookings\".end <= ?) or \n" +
                    "\t\t\t\t\t\t\t(\"Bookings\".begin <= ? and \"Bookings\".end > ?)or\n" +
                    "\t\t\t\t\t\t\t(? >= \"Bookings\".begin and ? < \"Bookings\".end)) and idbooking <> ?";
            PreparedStatement statement;
            if (cReq.getReq_id() == null) {
                /*Attraverso la presenza o meno dell'id nella richiesta viene distinto il tipo di richiesta:
                * se questo non è presente siamo nel caso della richiesta di creazione, altrimenti in quella di modifica*/
                statement = conn.prepareStatement(query);
                statement.setString(1,room);
                statement.setString(2,cReq.getDate());
                statement.setString(3,cReq.getBegin());
                statement.setString(4,cReq.getBegin());
                statement.setString(5,cReq.getBegin());
                statement.setString(6,cReq.getEnd());
                statement.setString(7,cReq.getEnd());
                statement.setString(8,cReq.getEnd());
                statement.setString(9,cReq.getBegin());
                statement.setString(10,cReq.getEnd());
            }else{
                statement = conn.prepareStatement(modifQuery);
                statement.setString(1,room);
                statement.setString(2,cReq.getDate());
                statement.setString(3,cReq.getBegin());
                statement.setString(4,cReq.getBegin());
                statement.setString(5,cReq.getBegin());
                statement.setString(6,cReq.getEnd());
                statement.setString(7,cReq.getEnd());
                statement.setString(8,cReq.getEnd());
                statement.setString(9,cReq.getBegin());
                statement.setString(10,cReq.getEnd());
                statement.setString(11,cReq.getReq_id());
            }
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {

                flag = true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
        return flag;
    }

    public void booking(List availableRooms, CreationRequest cReq) {
        /*Include una query di inserimento, quindi effettua, in definitiva, la prenotazione*/
        try {
            String insertQuery = "insert into public.\"Bookings\" values \n" +
                    "(?,?,?,?,?,?,?,?,?,?)";
            conn = DaoResources.getConnection(connData);

            String booker = cReq.getBooker();
            String date = cReq.getDate();
            String event_type = cReq.getEvent_type();
            String begin = cReq.getBegin();
            String end = cReq.getEnd();
            String id = idCreation(date, begin, booker);
            String session = cReq.getSession();
            String course = cReq.getCourse();
            for (Object availableRoom : availableRooms) {
                String classroom = (String) availableRoom;

                PreparedStatement statementInsertQuery = conn.prepareStatement(insertQuery);
                statementInsertQuery.setString(1, id);
                statementInsertQuery.setString(2, classroom);
                statementInsertQuery.setString(3, booker);
                statementInsertQuery.setString(4, date);
                statementInsertQuery.setString(5, event_type);
                statementInsertQuery.setString(6, begin);
                statementInsertQuery.setString(7, end);
                statementInsertQuery.setString(8, new SimpleDateFormat("yyyy-MM-dd")
                        .format(Calendar.getInstance().getTime()));
                statementInsertQuery.setString(9, session);
                statementInsertQuery.setString(10, course);

                statementInsertQuery.executeUpdate();
            }
            cReq.setReq_id(id);//In questa fase viene generato l'id univoco di prenotazione
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void deleteBook(DeleteRequest dReq){
        /*Include una semplice query di eliminazione*/
        try{
            conn = DaoResources.getConnection(connData);
            String deleteQuery = "delete from public.\"Bookings\"\n" + "where idbooking = ?";

            PreparedStatement statementInsertQuery = conn.prepareStatement(deleteQuery);
            statementInsertQuery.setString(1, dReq.getReq_id());
            statementInsertQuery.executeUpdate();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean bookingMatching(CreationRequest mReq) {
        /*Controlla che la richiesta di modifica sia effettuabile, ovvero il richiedente abbia i permessi, cioè
        * corrisponda con il creatore della prenotazione da modificare*/
        boolean flag = false;
        try{
            conn = DaoResources.getConnection(connData);
            String query = "select * from public.\"Bookings\"\n" + "where idbooking = ?";
            PreparedStatement statementInsertQuery = conn.prepareStatement(query);
            statementInsertQuery.setString(1, mReq.getReq_id());
            ResultSet rs = statementInsertQuery.executeQuery();
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean bookingExistence(CreationRequest mReq) {
        /*Controlla semplicemente l'esistenza della prenotazione da modificare*/
        boolean flag = false;
        try{
            conn = DaoResources.getConnection(connData);
            String query = "select * from public.\"Bookings\"\n" + "where idbooking = ? and booker = ?";
            PreparedStatement statementInsertQuery = conn.prepareStatement(query);
            statementInsertQuery.setString(1, mReq.getReq_id());
            statementInsertQuery.setString(2, mReq.getBooker());
            ResultSet rs = statementInsertQuery.executeQuery();
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
    private String idCreation(String date, String begin, String booker){
        /*Crea l'id univoco di prenotazione utilizzando data ora e id del prenotante. es: 201801011100001*/
        String formatDate[] = date.split("-");
        String formatBegin[] = begin.split(":");
        return formatDate[0] + formatDate[1] + formatDate[2] + formatBegin[0] + formatBegin[1] + booker;
    }
}
