import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import utility.ThreadRequest;

public class Main {
    /**
     * Entry point dell'applicazione. Estrae gli oggetti JSON dal JSONArray e gli affida a ThreadRequest*/

    private static void splitJson(String path) {
        JSONParser parser = new JSONParser();
        ThreadRequest tReq;
        try {
            JSONArray jsonArray =  (JSONArray) parser.parse
                    (new FileReader(path));
            for(Object o : jsonArray) {
                JSONObject jsonRequest = (JSONObject) o;
                tReq = new ThreadRequest(jsonRequest);
                tReq.start();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("System starting...");
        String path = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources\\requests.json";
        splitJson(path);
    }

}
