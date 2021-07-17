package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadConnFile {
    /**
     * Classe che esporta un unico metodo statico che permette la lettura del file di configurazione del database.
     * */
        public static String[] readConnFile(String filePath) {
            String connData[] = new String[3];
            try {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(filePath));
                String line = reader.readLine();
                int i = 0;
                while (line != null) {
                    connData[i] = line;
                    line = reader.readLine();
                    i++;
                }
                reader.close();
            } catch (IOException e){ e.printStackTrace(); }

            return connData;
        }

}
