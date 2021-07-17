package utility;

import boundary.SS2Interaction;
import org.json.simple.JSONObject;

public class ThreadRequest extends Thread {
    /**
     * Un ciclo nel main richiama questa classe che crea thread in grado di gestire le richieste in parallelo
     */
    private JSONObject jsonRequest;

    public ThreadRequest(JSONObject jsonRequest) {
        this.jsonRequest = jsonRequest;
    }
    public void run() {
        System.out.println("\n" + this.getName() + " is running " + "\"" +
                jsonRequest.get("req type") + "\""+" request...");
        SS2Interaction ss2Interaction = new SS2Interaction();
        ss2Interaction.handleEvent(jsonRequest);
    }
}
