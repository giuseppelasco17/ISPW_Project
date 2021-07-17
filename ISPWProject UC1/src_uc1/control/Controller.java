package control;

import boundary.SS1Interaction;
import dao.Dao;
import entity.CreationRequest;
import entity.DeleteRequest;
import factory.DRequest;
import utility.ReadConnFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Controller {
    /**
     * Questa classe si occupa di comunicare con boundary, dao ed entity.
     * */
    private String filePath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources\\DBConnection.txt";
    private String connData[] = ReadConnFile.readConnFile(filePath);
    private SS1Interaction ss1Int = new SS1Interaction();
    private Dao dao = new Dao(connData);
    private List availableRooms;

    public void requestHandler (CreationRequest req, String reqType){
        /*Metodo chiamato da MRequest e CRequest che permette di effetturare i controlli necessari sull'esistenza
        * fisica delle aule, interfacciandosi con l'SS1, e la disponibilà di quest'ultime interrogando il databse.*/
        int i = 0;
        boolean flag = false;
        /*Controllo se sono state richieste aule preferite*/
        if(req.getClass_pref().isEmpty()){
            /*In questo caso verifico la disponibilità considerando il numero di posti specificati nella richiesta*/
            while(i >= 0 && i < 3 && !flag) {
                /*Nel caso peggiore vengono fatti 3 tentativi. Il primo considerando il numero di posti di una singola
                * aula, il secondo considerando il numero di posti come somma dei posti di due aule, stesso discorso
                * per il terzo con tre aule. Se anche il terzo tentativo fallisce, l'applicazione termine con un avviso*/
                i++;
                flag = checkAndBook(i, req);
            }
        }else{
            /*Altrimenti faccio una singola verifica sulle aule preferite*/
            flag = checkAndBook(req.getClass_pref().size(), req);
        }
        if(!flag){
            System.out.println("\nThere are no matches with the required characteristics. " + reqType + " not completed.");
        }else{
            System.out.print("\n" + reqType + " completed:\n Id: "+ req.getReq_id() +"\n Classrooms:");
            if (availableRooms != null) {
                for(int j = 0; j < availableRooms.size(); j++){
                    System.out.print(" " + availableRooms.get(j));
                    if(j!= availableRooms.size()-1){
                        System.out.print(",");
                    }
                }
            }System.out.println("\n Day: "+ req.getDate() +"\n From: "+ req.getBegin() +"\n To: "+ req.getEnd());
        }
    }

    private boolean checkAndBook(int i, CreationRequest req){
        /*Questa funzione riceve come parametro un intero che rappresenta il tentativo da effettuare, ovvero il numero
        * di aule da ricercare.*/
        List crooms;
        /*Verifica la disponibilità fisica delle aule (preferite o meno) interaggendo con SS1. crooms conterrà un insieme
        * di aule verificabili (per esempio, se il parametro i è uguale ad uno, la lista conterrà una serie di aule
        * che singolarmente soddisfano la richiesta. Se invece il parametro i è uguale a due, la lista conterrà aule
        * che due a due devono essere verificate*/
        crooms = ss1Int.availabilityCheck(req, i);
        boolean flag = false;
        if(!crooms.isEmpty()){
            availableRooms = new ArrayList();
            /*Una volta acquisita la lista di aule fisicamente esistenti, viene verificata la disponibilità sul DB*/
            availableRooms = dao.availabilityRooms(crooms, i, req);
            if (!availableRooms.isEmpty()) {
                /*Nel caso in cui si tratti di una richiesta di modifica, effettua la cancellazione preventiva della
                   * vecchia prenotazione*/
                if(req.getReq_id() != null) {
                    DRequest dRequest = new DRequest();
                    dRequest.handleRequest(req);
                }
                /*Prenotazione vera e propria nel DB*/
                dao.booking(availableRooms, req);
                flag = true;
            }
        }
        return flag;
    }


    public boolean bookingExistence(CreationRequest req){
        /*Utilizzato da MRequest per verificare l'esistenza della prenotazione*/
        return dao.bookingExistence(req);
    }

    public boolean bookingMatching(CreationRequest req){
        /*Utilizzato per verificare che la prenotazione da modificare è stata creata dal modificante*/
        return dao.bookingMatching(req);
    }

    public void deleteBook(DeleteRequest req) {
        /*Metodo chiamato da DRequest per eliminare una prenotazione presente nel DB*/
        dao.deleteBook(req);
    }
}
