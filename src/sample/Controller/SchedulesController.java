package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.AppointmentsDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Appointment;
import javafx.event.ActionEvent;
import java.sql.Timestamp;

public class SchedulesController {

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, Timestamp> endTimeCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, Timestamp> startTimeCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, Integer> userIdCol;

    @FXML
    private Button returnBtn;

    Stage stage;
    Parent scene;
    /** displays appointments associated with given contact*/
    public void displayContactsAppointments(String contact){
        appointmentsTableView.setItems(AppointmentsDAO.getAppointmentsAssociatedWithContact(contact));
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }
    @FXML
    void onActionOpenReports(ActionEvent event) throws Exception{
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}