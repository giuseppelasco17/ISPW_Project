package factory;


import control.Controller;
import entity.CreationRequest;
import org.json.simple.JSONObject;
import utility.InstantiateRequest;

public class CRequest implements Request{
/**
 * Implementa l'interfaccia Request e permette la realizzazione della factory. Da qui parte il processo di creazione
 * della prenotazione delegando il carico di lavoro al controller
 */

    @Override
    public void handleRequest(JSONObject jsonRequest) {
        InstantiateRequest inReq = new InstantiateRequest();
        Controller controller = new Controller();
        CreationRequest cReq = inReq.initializeCreation(jsonRequest);
        controller.requestHandler(cReq, "Booking");
    }
}
