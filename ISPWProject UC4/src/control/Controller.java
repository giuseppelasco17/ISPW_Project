package control;

import dao.Dao;
import entity.Booking;
import entity.Event;
import utility.ReadConnFile;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Controller {
    /**
     * Controller grafico della pagina di ricerca da parte di uno studente*/
    private Event event;

    public boolean timeParse(String time){
        /*Funzione che fa il parsing degli orari*/
        if(!time.isEmpty()) {
            DateFormat format = new SimpleDateFormat("HH:mm");
            format.setLenient(false);
            try {
                format.parse(time);
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    public boolean dateParse(String date){
        /*Funzione che fa il parsing della data*/
        if(!date.isEmpty()) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            try {
                format.parse(date);
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    public List<Booking> handleFilter(ArrayList<String> content){
        /*Funzione che riconosce la tipologia di filtro applicata, ovvero le combinazioni di inserimento, e interroga
         * il database; restutuisce una lista di prenotazioni che rispecchiano il filtro imposto*/
        createEvent(content);
        Dao dao = Dao.getDao();
        List<Booking> bookingList = new ArrayList<Booking>();
        try {

            if(event.getTeacherName().isEmpty() && event.getCourse() == null){
                bookingList = dao.req1(event);
            }
            else if(!event.getTeacherName().isEmpty() && event.getCourse() != null){
                bookingList = dao.req2(event);
            }
            else if(!event.getTeacherName().isEmpty() && event.getCourse() == null){
                bookingList = dao.req3(event);
            }
            else if(event.getTeacherName().isEmpty() && event.getCourse() != null){
                bookingList = dao.req4(event);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bookingList;
    }


    private void createEvent(ArrayList<String> content ) {
        /*Crea un oggetto di tipo event inizializzandolo con i valori della lista content
         */
        event = new Event();
        event.setEvent_type(content.get(0));
        event.setTeacherName(content.get(1));
        event.setTeacherSurname(content.get(2));
        if(!content.get(3).isEmpty()) event.setCourse(content.get(3));
        event.setDate(content.get(4));
        if(!content.get(5).isEmpty()) event.setBegin(content.get(5));
        if(!content.get(6).isEmpty()) event.setEnd(content.get(6));
    }

    public String firstLetterUp(String string){
        /*Semplice funzione che rende maiascola la prima lettera di una stringa, utilizzata nel l'oggetto di ritorno per
         * una migliore visualizzazione dei dati*/
        if (!string.isEmpty()) {
            string = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
        }
        return string;
    }

    public List<String> splitTeacher(String teacher){
        /*Divide il nome e cognome inseriti in input come un'unica stringa e ne restituisce una lista*/
        String[] strings = teacher.split(" ");
        return new ArrayList<>(Arrays.asList(strings));
    }

    public boolean validateTeacher(List<String> teacher) {
        /*Controllo che il nome e cognome sia stato inserito correttamente. In caso di mancato inserimento del cognome,
        * restituisce false*/
        if (teacher.size() == 1) {
            return false;
        }
        return true;
    }

    public String currentDate(){
        /*Acquisisce la data del sistema. Utilizzata nel caso di mancato inserimento della data*/
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }
}
