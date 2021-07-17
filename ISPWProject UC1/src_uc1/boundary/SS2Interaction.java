package boundary;

import factory.Request;
import factory.RequestFactory;
import org.json.simple.JSONObject;

public class SS2Interaction {
    /**
     * Classe con un unico metodo che riceve l'oggetto JSON e ne preleva il tipo di richiesta. Viene creata l'istanza
     * della Factory a cui passo il tipo di richiesta. Questa restituisce l'oggeto creato e richiama il metodo
     * dell'interfaccia Request. A seconda di quale oggetto è stato instanziato, ne esegue l'implementazione
     * associata*/

    public void handleEvent(JSONObject jsonRequest) {
        RequestFactory requestFactory = new RequestFactory();
        try {
            Request request = requestFactory.createRequest((String) jsonRequest.get("req type"));
            request.handleRequest(jsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

