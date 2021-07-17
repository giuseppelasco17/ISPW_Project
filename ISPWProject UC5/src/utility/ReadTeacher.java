package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadTeacher {
    /**
     * Classe che esporta un unico metodo statico, in grado di acquisire la chiave del docente che effettua la ricerca
     * nel database attraverso un file
     * */
    public static String readTeacher(String path) {
        String teacherId = null;
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(path));
            teacherId = reader.readLine();
            reader.close();
        } catch (IOException e){ e.printStackTrace(); }

        return teacherId;
    }
}
