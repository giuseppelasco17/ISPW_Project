package boundary;

import control.Controller;
import entity.Booking;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Uc5Home {
    /**
     * Uc5Home rappresenta il controller grafico della pagina di ricerca di un docente.
     */
    private Controller controller = new Controller();

    private ObservableList<String> choiceList = FXCollections.observableArrayList("Esame","Conferenza");


    @FXML
    private DatePicker date_pk;

    @FXML
    private ChoiceBox<String> type_choicebox;

    @FXML
    private TextField end_tf;

    @FXML
    private TextField session_tf;

    @FXML
    private Button search_bt;

    @FXML
    private TextField course_tf;

    @FXML
    private TextField begin_tf;

    @FXML
    private Label errorDate_lb;

    @FXML
    private Label errorBegin_lb;

    @FXML
    private Label errorEnd_lb;

    @FXML
    private Label errorTeacher_lb;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @FXML
    private  void initialize(){
        /* Prima funzione a partire appena viene caricata la grafica ed inizializza le varie componenti grafiche*/
        type_choicebox.setValue(choiceList.get(0));
        type_choicebox.setItems(choiceList);
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            if(newValue.equals(choiceList.get(1))) {
                course_tf.setDisable(true);
                session_tf.setDisable(true);
            }else {
                course_tf.setDisable(false);
                session_tf.setDisable(false);
            }
        };
        // cambio di selezione nella box di scelta.
        type_choicebox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }
    @FXML
    public void onSearchClick(ActionEvent event) {
        /*Handler del tasto 'cerca' che raccoglie i dati in input e rende visibili i risultati mediante il lancio
        * della pagina di visualizzazione. */
        try {
            date_pk.setValue(date_pk.getConverter().fromString(date_pk.getEditor().getText()));
        }catch (java.time.format.DateTimeParseException e){
            errorDate_lb.setVisible(true);
            errorDate_lb.setText("Data non valida");
        }
        errorBegin_lb.setVisible(false);
        errorEnd_lb.setVisible(false);
        errorDate_lb.setVisible(false);
        errorTeacher_lb.setVisible(false);
        String date;
        if (date_pk.getValue() == null) {
            date = "";
        } else {
            date = date_pk.getValue().format(dateFormatter);
        }
        ArrayList<String> content;
        if (validateInput(begin_tf.getText(), end_tf.getText(), date)) {
            /*Dopo la validazione dell'input, prepara i dati (raccolti in una lista) da consegnare ad un metodo del
            * controller*/
            content = new ArrayList<>();
            content.add(type_choicebox.getValue().toLowerCase());
            content.add(session_tf.getText().toLowerCase());
            content.add(course_tf.getText().toLowerCase());
            content.add(date);
            content.add(begin_tf.getText());
            content.add(end_tf.getText());
            List<Booking> bookings = controller.handleFilter(content);
            Stage stage;
            stage = (Stage) search_bt.getScene().getWindow();
            /*Una volta acquisiti i risultati, lancio la schermata di visualizzazione*/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundary/Uc5_visual.fxml"));
            Pane root;
            try {
                root = loader.load();
            Uc5Visual uc5Visual = loader.getController();
            uc5Visual.getPrenotation(bookings);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean validateInput(String begin, String end, String date){
        /*Funzione che valida i dati inseriti in input. In caso di dati non validi, setta le opportune label di errore*/
        boolean flag = true;

        if(!date.isEmpty()) {
            if (!controller.dateParse(date)) {
                errorDate_lb.setVisible(true);
                errorDate_lb.setText("Data non valida");
                flag = false;
            }
        }
        if(!begin.isEmpty()) {
            if (!controller.timeParse(begin)) {
                errorBegin_lb.setVisible(true);
                errorBegin_lb.setText("Orario di inizio non valido");
                flag = false;
            }
        }
        if(!end.isEmpty()) {
            if (!controller.timeParse(end)) {
                errorEnd_lb.setVisible(true);
                errorEnd_lb.setText("Orario di fine non valido");
                flag = false;
            }
        }
        if(flag && !begin.isEmpty() && !end.isEmpty()) {
            if(end.compareTo(begin) < 0) {
                flag = false;
            }
        }
        return flag;
    }

}
