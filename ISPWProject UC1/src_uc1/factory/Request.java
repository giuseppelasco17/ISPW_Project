package factory;

import org.json.simple.JSONObject;

public interface Request {
    /**
     * Interfaccia che esporta l'operazione handleRequest implementata in CRequest, DRequest e MRequest
     */
    void handleRequest(JSONObject jsonRequest);
}
