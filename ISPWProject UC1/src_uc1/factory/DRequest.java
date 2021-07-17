package factory;

import control.Controller;
import entity.CreationRequest;
import entity.DeleteRequest;
import org.json.simple.JSONObject;
import utility.InstantiateRequest;

public class DRequest implements Request {
    /**
     * Implementa l'interfaccia Request e permette la realizzazione della factory. Da qui parte il processo di cancellazione
     * della prenotazione delegando il carico di lavoro al controller
     */
    private Controller controller = new Controller();
    private InstantiateRequest inReq = new InstantiateRequest();

    @Override
    public void handleRequest(JSONObject jsonRequest) {
        DeleteRequest dReq = inReq.initializeDeleteByJson(jsonRequest);
        doDelete(dReq);
    }

    public void handleRequest(CreationRequest cReq) {
        DeleteRequest dReq = inReq.initializeDeleteByCreation(cReq);
        doDelete(dReq);
    }

    private void doDelete(DeleteRequest dReq){
        /*Dopo aver effetuato controlli sui permessi di cancellazione chiama il metodo del controller deleteBook()
         *che effettua la cancellazione*/
        String id = dReq.getReq_id().substring(12);
        if(id.equals(dReq.getBooker())) {
            controller.deleteBook(dReq);
            System.out.println("\nBooking " + dReq.getReq_id() + " deleted");
        }else
            System.out.println("\nPermission denied");
    }

}

