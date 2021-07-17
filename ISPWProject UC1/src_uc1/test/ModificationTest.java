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

public class ModificationTest {
    /**
     * Classe di test per richieste di modifica
     */
    private JSONArray jsonArray;
    private SS2Interaction ss2Interaction;
    private DaoTest daoTest = new DaoTest();

    @Before
    public void initialize(){
        /*Inizializza i campi jsonArray e ss2Interaction*/
        jsonArray = parseJson("C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources\\" +
                "test_requests.json");
        ss2Interaction = new SS2Interaction();
    }

    @Test
    public void test1(){
        //Test relativo all'impossibilità di modifica di una prenotazione inesistente
        System.out.println("Test relativo all'impossibilità di modifica di una prenotazione inesistente:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(8));//Modifica pren. inesistente con nuovo id 300001010900004
        Assert.assertFalse(daoTest.bookingCheck("300001050830004"));//Cerco l'ipotetica prenotazione modificata
    }

    @Test
    public void test2(){
        //Test relativo alla corretta esecuzione della richiesta di modifica
        System.out.println("Test relativo alla corretta esecuzione della richiesta di modifica:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(1));//Inserisco pren. con id 300001011200004
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(8));//Modifica pren. esistente con nouvo id 300001010900004
        Assert.assertTrue(daoTest.bookingCheck("300001050830004"));//Cerco l'ipotetica prenotazione modificata
        Assert.assertFalse(daoTest.bookingCheck("300001011200004"));//Mi assicuro che la vecchia pren. è stata eliminata
    }

    @Test
    public void test3(){
        //Test relativo ai permessi della modifica
        System.out.println("Test relativo ai permessi della modifica:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(1));//Inserisco pren. con id 300001011200004
        //Modifica pren. esistente con nuovo id 300001010900003 da parte di utente 003 invece che 004
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(9));
        Assert.assertFalse(daoTest.bookingCheck("300001050830003"));//Cerco l'ipotetica prenotazione modificata
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
        daoTest.deleteBook("300001050830004");
        daoTest.deleteBook("300001011200004");
    }
}
