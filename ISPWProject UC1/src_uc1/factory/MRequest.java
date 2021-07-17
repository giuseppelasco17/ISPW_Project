package factory;

import control.Controller;
import entity.CreationRequest;
import org.json.simple.JSONObject;
import utility.InstantiateRequest;

public class MRequest implements Request {
    /**
     * Implementa l'interfaccia Request e permette la realizzazione della factory. Da qui parte il processo di modifica
     * della prenotazione delegando il carico di lavoro al controller
     */

    @Override
    public void handleRequest(JSONObject jsonRequest) {
        /*Dopo aver effettuato controlli di esistenza della prenotazione da modificare e controlli relativi ai permessi,
        * chiama il metodo del controller requestHandler() che effettua la modifica*/
        Controller controller = new Controller();
        InstantiateRequest inReq = new InstantiateRequest();
        CreationRequest mReq = inReq.initializeModify(jsonRequest);
        if(!controller.bookingMatching(mReq)){
            System.out.println("\nBooking not found");
        }else if(!controller.bookingExistence(mReq)){
            System.out.println("\nPermission denied, booking doesn't belong to booker");
        }else{
            controller.requestHandler(mReq, "Modification");
        }
    }
}
