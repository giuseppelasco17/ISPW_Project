package daoResources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoResources {
    /**
     * Classe che esporta un metodo statico che restituisce la connessione al database attraverso le credenziali di
     * accesso che gli vengono passate
     */

    public static Connection getConnection(String[] connData) throws ClassNotFoundException, SQLException {
        String url = connData[0];
        String username = connData[1];
        String password = connData[2];
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}