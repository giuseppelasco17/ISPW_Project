package control;

import dao.Dao;
import entity.Booking;
import entity.Event;
import utility.ReadConnFile;
import utility.ReadTeacher;

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
     * Controller grafico della pagina di ricerca da parte di un docente.
     * Dato che il nostro sistema si interfaccia con un altro sottosistema che ha il compito di esportare la pagina di
     * login,assumiamo che il docente sia già loggato e simuliamo ciò attraverso l'uso di un file in cui è registrata
     * la chiave del docente in questione. Tale file è modificabile con qualsiasi chiave registrata nel database*/
    private String filePath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC5\\src\\resources\\DBConnection.txt";
    private String connData[] = ReadConnFile.readConnFile(filePath);
    private Event event;
    private String idTheacherPath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC5\\src" +
            "\\resources\\teacherId";

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
        /*Funzione che fa il parsing delle date*/
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
        content.add(ReadTeacher.readTeacher(idTheacherPath));
        createEvent(content);
        Dao dao = new Dao(connData);
        List<Booking> bookingList = new ArrayList<Booking>();
        try {
            if(event.getSession() == null && event.getCourse() == null){
                bookingList = dao.req1(event);
            }
            else if(event.getSession() != null && event.getCourse() != null){
                bookingList = dao.req2(event);
            }
            else if(event.getSession() != null && event.getCourse() == null){
                bookingList = dao.req3(event);
            }
            else if(event.getSession() == null && event.getCourse() != null){
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
        if(!content.get(1).isEmpty()) event.setCourse(content.get(1));
        if(!content.get(2).isEmpty()) event.setSession(content.get(2));
        event.setDate(content.get(3));
        if(!content.get(4).isEmpty()) event.setBegin(content.get(4));
        if(!content.get(5).isEmpty()) event.setEnd(content.get(5));
        event.setTeacherId(content.get(6));
    }

    public String firstLetterUp(String string){
        /*Semplice funzione che rende maiascola la prima lettera di una stringa, utilizzata nel l'oggetto di ritorno per
        * una migliore visualizzazione dei dati*/
        if (!string.isEmpty()) {
            string = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
        }
        return string;
    }

    public String currentDate(){
        /*Acquisisce la data del sistema. Utilizzata nel caso di mancato inserimento della data*/
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }
}
