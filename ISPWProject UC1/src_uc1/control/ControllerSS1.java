package control;

import dao.DaoSS1;
import entity.CreationRequest;
import utility.ReadConnFile;

import java.util.List;

public class ControllerSS1 {
    /**
     * Questa classe permette di controllare, attraverso il metodo findClassroom(), nel
     * caso di assenza di aule preferite, di verificare l'esistenza di aule la cui somma di posti è maggiore o uguale a
     * quella richiesta.
     * */
    private String filePath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC1\\src_uc1\\resources\\DBConnection.txt";
    private String connData[] = ReadConnFile.readConnFile(filePath);
    private DaoSS1 daoSS1 = new DaoSS1(connData);


    public List findClassroom(CreationRequest cReq, int i) {
        /*A seconda del tentativo, ovvero della variabile i, entra in uno dei casi e richiama le funzioni di ricerca
        * implementate nella classe DaoSS1*/
        List crooms = null;
        switch (i){
            case 1:
                crooms = daoSS1.findClass1(cReq);
                break;
            case 2:
                crooms = daoSS1.findClass2(cReq);
                break;
            case 3:
                crooms = daoSS1.findClass3(cReq);
                break;
        }
        return crooms;
    }

    public List findFavoriteClass(CreationRequest cReq){
        /*In presenza di classi preferite ignora la variabile i e chiama una funzione di ricerca implementata nella
         * classe DaoSS1, verificando l'esistenza fisica di tali aule*/
        List crooms = cReq.getClass_pref();
        for(int i = 0; i < crooms.size(); i++) {
            if (!daoSS1.findFavoriteClass((String)crooms.get(i))){
                return null;
            }
        }
        return crooms;
    }
}
