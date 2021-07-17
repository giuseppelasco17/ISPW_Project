package boundary;

import entity.Booking;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class Uc4Visual {

    @FXML
    private TableView<Booking> booking_tv;

    @FXML
    private Button return_bt;

    @FXML private TableColumn<Booking, String> bookerName = new TableColumn<>("Nome");
    @FXML private TableColumn<Booking, String> bookerSurname = new TableColumn<>("Cognome");
    @FXML private TableColumn<Booking, String> date = new TableColumn<>("Data");
    @FXML private TableColumn<Booking, String> classroom = new TableColumn<>("Aula");
    @FXML private TableColumn<Booking, String> course = new TableColumn<>("Materia");
    @FXML private TableColumn<Booking, String> begin = new TableColumn<>("Ora inizio");
    @FXML private TableColumn<Booking, String> end = new TableColumn<>("Ora fine");
    @FXML private TableColumn<Booking, String> session = new TableColumn<>("Sessione");


    void setBooking(List<Booking> bookings) {
        ObservableList<Booking> items = FXCollections.observableArrayList(bookings);
        bookerName.setCellValueFactory(new PropertyValueFactory<>("bookerName"));
        bookerSurname.setCellValueFactory(new PropertyValueFactory<>("bookerSurname"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        classroom.setCellValueFactory(new PropertyValueFactory<>("classroom"));
        course.setCellValueFactory(new PropertyValueFactory<>("course"));
        session.setCellValueFactory(new PropertyValueFactory<>("session"));
        begin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        booking_tv.setItems(items);
    }

    @FXML
    void onReturnClick(ActionEvent event) {
        Stage stage;
        stage = (Stage) return_bt.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundary/Uc4_home.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = null;
        if (root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
    }

}
