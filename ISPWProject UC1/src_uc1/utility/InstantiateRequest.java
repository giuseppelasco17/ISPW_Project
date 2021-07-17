package utility;

import entity.CreationRequest;
import entity.DeleteRequest;
import entity.ModificationRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;


public class InstantiateRequest {
    /**
     * Classe che permette la semplice inizializzazione degli oggetti di tipo richiesta estraendo i dati dal JSONObject
     */
    public CreationRequest initializeCreation(JSONObject jsonRequest) {
        CreationRequest cReq = new CreationRequest();
        if(!jsonRequest.get("begin").equals(""))
            cReq.setBegin((String) jsonRequest.get("begin"));
        cReq.setBooker((String)jsonRequest.get("booker"));
        cReq.setDate((String)jsonRequest.get("date"));
        cReq.setEnd((String) jsonRequest.get("end"));
        cReq.setEvent_type((String)jsonRequest.get("event type"));
        cReq.setSession((String) jsonRequest.get("session"));
        cReq.setCourse((String) jsonRequest.get("course"));
        cReq.setN_seats((long)jsonRequest.get("n. seats"));
        cReq.setClass_pref(JsonToArray((JSONArray) jsonRequest.get("class pref")));
        cReq.setFeatures(JsonToArray((JSONArray) jsonRequest.get("features")));

        return cReq;
    }



    public CreationRequest initializeModify(JSONObject jsonRequest) {
        CreationRequest mReq = new ModificationRequest();
        if(!jsonRequest.get("begin").equals(""))
            mReq.setBegin((String) jsonRequest.get("begin"));
        mReq.setBooker((String) jsonRequest.get("booker"));
        mReq.setReq_id((String) jsonRequest.get("req id"));
        mReq.setDate((String) jsonRequest.get("date"));
        mReq.setEvent_type((String) jsonRequest.get("event type"));
        mReq.setSession((String) jsonRequest.get("session"));
        mReq.setCourse((String) jsonRequest.get("course"));
        mReq.setEnd((String) jsonRequest.get("end"));
        mReq.setN_seats((long) jsonRequest.get("n. seats"));
        mReq.setClass_pref(JsonToArray((JSONArray) jsonRequest.get("class pref")));
        mReq.setFeatures(JsonToArray((JSONArray) jsonRequest.get("features")));

        return mReq;
    }

    public DeleteRequest initializeDeleteByJson(JSONObject jsonRequest) {
        DeleteRequest dReq = new DeleteRequest();
        dReq.setReq_id((String) jsonRequest.get("req id"));
        dReq.setBooker((String) jsonRequest.get("booker"));
        return dReq;
    }

    private ArrayList JsonToArray(JSONArray jsonItem){
        /*Prende un campo array dell'oggetto JSON e lo restituisce in un'arrayList*/
        ArrayList<String> arrayItem = new ArrayList<>(jsonItem.size());
        for (Object aJsonItem : jsonItem) {
            arrayItem.add((String) aJsonItem);
        }
        return arrayItem;
    }

    public DeleteRequest initializeDeleteByCreation(CreationRequest cReq) {
        DeleteRequest dReq = new DeleteRequest();
        dReq.setReq_id(cReq.getReq_id());
        dReq.setBooker(cReq.getBooker());
        return dReq;
    }
}
