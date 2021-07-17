package dao;

import daoResources.DaoResources;
import entity.CreationRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoSS1 {
    /**
     * Classe che permette la comunicazione con il database del sottosistema 1. Esporta metodi che permettono di
     * verificare l'esistenza fisica delle aule e di controllare il numero di posti nel caso di prenotazioni senza aule
     * preferite
     */
    private String[] connData;
    private Connection conn;

    public DaoSS1(String[] connData) {
        this.connData = connData;
    }

    public List findClass1(CreationRequest cReq) {
        /*Include una query di ricerca di aule singole con numero di posti >= a quelli desiderati. Ritorna la lista
        * delle aule trovate*/
        List<String> crooms = new ArrayList<>();
        try {
            conn = DaoResources.getConnection(connData);
            String query = "select classroom from public.\"Classrooms\"\n" + "\twhere n_seats >= ?\n" +
                    "\torder by n_seats asc";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, (int)cReq.getN_seats());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                crooms.add(rs.getString(1));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return crooms;
    }

    public List findClass2(CreationRequest cReq) {
        /*Include una query di ricerca di coppie di aule aventi somma del numero di posti >= a quelli desiderati.
         * Ritorna la lista delle aule trovate*/
        List<String> crooms = new ArrayList<>();
        try {
            conn = DaoResources.getConnection(connData);
            String query = "select \"C1\".classroom as class1, \"C2\".classroom as class2\n" +
                    "\tfrom public.\"Classrooms\" as \"C1\" full join public.\"Classrooms\" as \"C2\" on " +
                    "\"C1\".classroom <> \"C2\".classroom\n" + "\twhere (\"C1\".n_seats + \"C2\".n_seats) >= ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, (int)cReq.getN_seats());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                crooms.add(rs.getString(1));
                crooms.add(rs.getString(2));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return crooms;
    }

    public List findClass3(CreationRequest cReq) {
        /*Include una query di ricerca di terne di aule aventi somma del numero di posti >= a quelli desiderati.
         * Ritorna la lista delle aule trovate*/
        List<String> crooms = new ArrayList<>();
        try {
            conn = DaoResources.getConnection(connData);
            String query = "select \"C1\".classroom as class1, \"C2\".classroom as class2, \"C3\".classroom as class3\n"
                    + "\tfrom public.\"Classrooms\" as \"C1\" full join public.\"Classrooms\" as \"C2\" on " +
                    "\"C1\".classroom <> \"C2\".classroom full join public.\"Classrooms\" as \"C3\" \n" +
                    "\ton \"C1\".classroom <> \"C3\".classroom and \"C2\".classroom <> \"C3\".classroom\n" +
                    "\twhere (\"C1\".n_seats + \"C2\".n_seats + \"C3\".n_seats) >= ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, (int)cReq.getN_seats());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                crooms.add(rs.getString(1));
                crooms.add(rs.getString(2));
                crooms.add(rs.getString(3));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return crooms;
    }

    public boolean findFavoriteClass(String classroom)
            /*Controlla l'esistenza fisica delle aule indicate nella richiesta*/
    {
        boolean flag = false;
        try {
            conn = DaoResources.getConnection(connData);
            String query = "select classroom from public.\"Classrooms\"\n" + "\twhere classroom = ?\n";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, classroom);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                flag = true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}

