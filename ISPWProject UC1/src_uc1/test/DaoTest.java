package test;

import daoResources.DaoResources;
import utility.ReadConnFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


class DaoTest {
    private String filePath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC5\\src\\resources\\DBConnection.txt";
    private String[] connData = ReadConnFile.readConnFile(filePath);
    private Connection conn;

    boolean bookingCheck(String idbooking){
        boolean flag = false;
        try{
            conn = DaoResources.getConnection(connData);
            String query = "select * from public.\"Bookings\"\n" + "where idbooking = ?";
            PreparedStatement statementInsertQuery = conn.prepareStatement(query);
            statementInsertQuery.setString(1, idbooking);
            ResultSet rs = statementInsertQuery.executeQuery();
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    boolean preExistenceCheck(String idbooking, String classroom){
        boolean flag = false;
        try{
            conn = DaoResources.getConnection(connData);
            String query = "select * from public.\"Bookings\"\n" + "where idbooking = ? and classroom = ?";
            PreparedStatement statementInsertQuery = conn.prepareStatement(query);
            statementInsertQuery.setString(1, idbooking);
            statementInsertQuery.setString(2, classroom);
            ResultSet rs = statementInsertQuery.executeQuery();
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    int findClassrooms(String idbooking){
        int n_seats = 0;
        try{
            conn = DaoResources.getConnection(connData);
            String query = "select sum(n_seats)\n" + "from public.\"Bookings\" natural join public.\"Classrooms\" \n"
                    + "where idbooking = ?";
            PreparedStatement statementInsertQuery = conn.prepareStatement(query);
            statementInsertQuery.setString(1, idbooking);
            ResultSet rs = statementInsertQuery.executeQuery();
            if(rs.next()){
                n_seats = rs.getInt(1);
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n_seats;
    }

    void deleteBook(String id){
        try{
            conn = DaoResources.getConnection(connData);
            String deleteQuery = "delete from public.\"Bookings\"\n" + "where idbooking = ?";

            PreparedStatement statementInsertQuery = conn.prepareStatement(deleteQuery);
            statementInsertQuery.setString(1, id);
            statementInsertQuery.executeUpdate();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection not found");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
