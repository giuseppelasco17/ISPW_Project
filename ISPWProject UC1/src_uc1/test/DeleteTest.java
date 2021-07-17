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

public class DeleteTest {
    /**
     * Classe di test per richieste di cancellazione
     */
    private JSONArray jsonArray;
    private SS2Interaction ss2Interaction;
    private DaoTest daoTest = new DaoTest();

    @Before
    public void initialize(){
        /*Inizializza i campi jsonArray e ss2Interaction e inserisce l'elemento di test*/
        System.out.println("\nInserimento dati...");
        jsonArray = parseJson("C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources\\" +
                "test_requests.json");
        ss2Interaction = new SS2Interaction();
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(0));//Id 300001011100005
    }

    @Test
    public void test1(){
        //Test relativo all'effettiva cancellazione
        System.out.println("\nTest relativo all'effettiva cancellazione:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(6));//
        boolean flag = daoTest.bookingCheck("300001011100005");//Controllo che la prenotazione sia stata
        Assert.assertFalse(flag);// effettivamente eliminata
    }

    @Test
    public void test2(){
        //Test relativo ai permessi della cancellazione
        System.out.println("\nTest relativo ai permessi della cancellazione:");
        ss2Interaction.handleEvent((JSONObject) jsonArray.get(7));//Id: 300001011100005 idbooker: 004
        /*controllo che la prenotazione esista ancora poichè la richiesta di cancellazione viene tentata da un utente
          diverso dal prenotante*/
        boolean flag = daoTest.bookingCheck("300001011100005");
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
        System.out.println("\nCancellazione dati");
        daoTest.deleteBook("300001011100005");
    }
}