package test;

import control.Controller;
import entity.Booking;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SearchTest {

    private Controller controller = new Controller();
    private DaoTest daoTest = new DaoTest();

    @Before
    public void insertData(){
        System.out.println("Inserimento dati...");
        ArrayList<String> classrooms = new ArrayList<>();
        classrooms.add("A1"); classrooms.add("A2");
        //Inserimento relativo alla tipologia esame
        daoTest.booking(classrooms, "001", "2000-01-01", "esame", "12:00", "15:00",
                "200001011200001", "invernale", "ISPW");
        daoTest.booking(classrooms, "001", "2050-01-01", "esame", "12:00", "15:00",
                "205001011200001", "invernale", "ISPW");
        //Inserimento relativo alla tipologia conferenza
        daoTest.booking(classrooms, "001", "2000-01-02", "conferenza", "12:00", "15:00",
                "200001021200001", "", "");
        daoTest.booking(classrooms, "001", "2050-01-02", "conferenza", "12:00", "15:00",
                "205001021200001", "", "");
    }

    @Test
    public void emptyTest(){
        System.out.println("Test con campi vuoti");
        ArrayList<String> content = initializeContent("esame","","","","","","");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("205001011200001")){
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void fullTest(){
        System.out.println("Test con campi pieni");
        ArrayList<String> content = initializeContent("esame","fernando","manco","ISPW",
                "2000-01-01","12:00","15:00");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001011200001")){
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void test1(){
        //Inserendo data vuota viene considerata quella odierna e la ricerca parte da quest'ultima,
        //controllo che prenotazioni con date precedenti non vengano visualizzati
        System.out.println("Test con data vuota");
        ArrayList<String> content = initializeContent("esame","fernando","manco","ISPW",
                "","12:00","15:00");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001011200001")){
                flag = true;
                break;
            }
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void test2(){
        //Test con orari vuoti; viene considerato l'intero giorno
        System.out.println("Test con orari vuoti");
        ArrayList<String> content = initializeContent("esame","fernando","manco","ISPW",
                "2000-01-01","","");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001011200001")){
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void test3(){
        //Test con ora di inizio inserita > dell'ora di inizio dell'evento
        System.out.println("Test con ora di inizio inserita > dell'ora di inizio dell'evento");
        ArrayList<String> content = initializeContent("conferenza","fernando","manco","",
                "2000-01-02","14:00","");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001021200001")){
                flag = true;
                break;
            }
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void test4(){
        //Test con ora di inizio inserita < dell'ora di inizio dell'evento
        System.out.println("Test con ora di inizio inserita < dell'ora di inizio dell'evento");
        ArrayList<String> content = initializeContent("conferenza","fernando","manco","",
                "2000-01-02","11:59","");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001021200001")){
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void test5(){
        //Test con tipo di evento conferenza, controllo che non venga restituito un evento di tipo esame
        System.out.println("Test con tipo di evento conferenza");
        ArrayList<String> content = initializeContent("conferenza","fernando","manco","",
                "","","");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("205001011200001")){
                flag = true;
                break;
            }
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void test6(){
        //Test con fascia oraria dell'evento inclusa nella fascia oraria richiesta
        System.out.println("Test con fascia oraria dell'evento inclusa nella fascia oraria richiesta");
        ArrayList<String> content = initializeContent("conferenza","fernando","manco","",
                "2000-01-02","11:59","15:01");
        List<Booking> bookings = controller.handleFilter(content);
        boolean flag = false;
        for (Booking b:bookings) {
            if(b.getIdBooking().equals("200001021200001")){
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }


    private ArrayList<String> initializeContent(String type, String name, String surname, String course, String date,
                                                String begin, String end){
        ArrayList<String> content = new ArrayList<>();
        content.add(type);
        content.add(name);
        content.add(surname);
        content.add(course);
        content.add(date);
        content.add(begin);
        content.add(end);
        return content;
    }

    @After
    public void delete(){
        System.out.println("Cancellazione dati");
        daoTest.deleteBook("200001011200001");
        daoTest.deleteBook("205001011200001");
        daoTest.deleteBook("200001021200001");
        daoTest.deleteBook("205001021200001");
    }
}

