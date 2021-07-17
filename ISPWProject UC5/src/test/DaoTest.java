package test;

import daoResources.DaoResources;
import utility.ReadConnFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

class DaoTest {
    private String filePath = "C:\\Users\\Utente\\IdeaProjects\\ISPWProject UC5\\src\\resources\\DBConnection.txt";
    private String[] connData = ReadConnFile.readConnFile(filePath);
    private Connection conn;

    void booking(List availableRooms, String booker, String date, String event_type, String begin,
                 String end, String id, String session, String course) {
        try {
            String insertQuery = "insert into public.\"Bookings\" values \n" +
                    "(?,?,?,?,?,?,?,?,?,?)";
            conn = DaoResources.getConnection(connData);

            for (Object availableRoom : availableRooms) {
                String classroom = (String) availableRoom;

                PreparedStatement statementInsertQuery = conn.prepareStatement(insertQuery);
                statementInsertQuery.setString(1, id);
                statementInsertQuery.setString(2, classroom);
                statementInsertQuery.setString(3, booker);
                statementInsertQuery.setString(4, date);
                statementInsertQuery.setString(5, event_type);
                statementInsertQuery.setString(6, begin);
                statementInsertQuery.setString(7, end);
                statementInsertQuery.setString(8, new SimpleDateFormat("yyyy-MM-dd")
                        .format(Calendar.getInstance().getTime()));
                statementInsertQuery.setString(9, session);
                statementInsertQuery.setString(10, course);

                statementInsertQuery.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
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
