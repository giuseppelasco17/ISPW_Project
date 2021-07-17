package daoResources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoResources {
    /**
     * Classe che esporta un unico metodo che acquisisce le credenziali di accesso al database e ne permette la
     * connessione.
     **/
    public static Connection getConnection(String[] connData) throws ClassNotFoundException, SQLException {
        String url = connData[0];
        String username = connData[1];
        String password = connData[2];
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}
