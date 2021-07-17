package boundary;

import control.ControllerSS1;
import entity.CreationRequest;
import java.util.List;

public class SS1Interaction {
    /**
     * Classe di boundary che comunica con il sottosistema 1. Quest'ultimo viene simulato attraverso le classi
     * SS1Interaction, ControllerSS1 e DaoSS1*/
    private ControllerSS1 controllerSS1 = new ControllerSS1();
    public List availabilityCheck(CreationRequest cReq, int i){
        /*Distingue la richiesta di prenotazione a seconda della presenza o meno delle classi preferite. Se non risultano
        * classi preferite, considera il numero di posti desiderato.
        * Ritorna una lista di classi materialmente disponibili*/
        List crooms = null;
        if(cReq.getClass_pref().isEmpty()){
            crooms = controllerSS1.findClassroom(cReq, i);
        }
        else{
            crooms = controllerSS1.findFavoriteClass(cReq);
        }
        return crooms;
    }
}
