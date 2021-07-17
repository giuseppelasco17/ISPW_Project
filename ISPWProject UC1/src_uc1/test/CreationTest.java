package test;

import boundary.SS2Interaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.FileReader;
import java.io.IOException;

public class CreationTest {
    /**
     * Classe di test per richieste di creazione
     */


    private JSONArray jsonArray;
    private SS2Interaction ss2Interaction;
    private DaoTest daoTest = new DaoTest();

    @Before
    public void initialize(){
        /*Inizializza i campi jsonArray e ss2Interaction*/
        jsonArray = parseJson("C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources" +
                "\\test_requests.json");
        ss2Interaction = new SS2Interaction();
    }

    @Test
    public void test1(){
        //Test relativo all'inserimento di prenotazioni sulla stessa aula con orari accavallati
        System.out.println("\nTest relativo all'inserimento di prenotazioni sulla stessa aula con orari accavallati:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(0));//Prenotazione da comparare ora inizio: 11:00 ora fine 13:00
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(1));//Id 300001011200004 ora inizio: 12:00 ora fine 15:00
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(2));//Id 300001011000004 ora inizio: 10:00 ora fine 12:00
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(3));//Id 300001011050004 ora inizio: 10:50 ora fine 14:00
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(4));//Id 300001011130004 ora inizio: 11:30 ora fine 12:30
        boolean flag = true;
        //Controllo che tutte le prenotazioni accavallate non sono state effettuate
        if(!daoTest.bookingCheck("300001011200004") &&
                !daoTest.bookingCheck("300001011000004") &&
                !daoTest.bookingCheck("300001011050004") &&
                !daoTest.bookingCheck("300001011130004"))
            flag = false;
        Assert.assertFalse(flag);
    }

    @Test
    public void test2(){
        //Test relativo a prenotazioni identiche
        System.out.println("\nTest relativo a prenotazioni identiche:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(0));
        Assert.assertTrue(daoTest.preExistenceCheck("300001011100005", "A1"));
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(0));
    }

    @Test
    public void test3(){
        //Test relativo al numero di posti su richieste senza aula preferita
        System.out.println("\nTest relativo al numero di posti su richieste senza aula preferita:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(5));//Prenotazione senza aule preferite
        int n_seats = daoTest.findClassrooms("300001011130004");//Cerco la somma del num di posti
        boolean flag = false;
        if(n_seats >= (long)(((JSONObject) jsonArray.get(5)).get("n. seats"))){//Controllo che sia >= quelli richiesti
            flag = true;
        }
        Assert.assertTrue(flag);
    }

    private JSONArray parseJson(String path) {
        /*Restituisce il JSONArray presente nel documento JSON locato al path passato*/
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray =  (JSONArray) parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @After
    public void delete(){
        /*Elimina le prenotazioni al termine dei test per ristabilire le condizioni precedenti al database*/
        daoTest.deleteBook("300001011200004");
        daoTest.deleteBook("300001011100005");
        daoTest.deleteBook("300001011000004");
        daoTest.deleteBook("300001011050004");
        daoTest.deleteBook("300001011130004");
    }


}
