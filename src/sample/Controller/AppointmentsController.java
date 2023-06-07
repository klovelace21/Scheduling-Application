package sample.Controller;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.DAO.AppointmentsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Helper;
import sample.Main;
import sample.Model.Appointment;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * this class is the controller for the appointments fxml file
 */
public class AppointmentsController implements Initializable {

    @FXML
    private Button addAppointmentBtn;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;

    @FXML
    private RadioButton unsortedViewRad;

    @FXML
    private Button deleteAppointmentBtn;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, Timestamp> endTimeCol;

    @FXML
    private ToggleGroup grp1;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private Button modifyAppointmentBtn;

    @FXML
    private TableColumn<Appointment, Timestamp> startTimeCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, Integer> userIdCol;

    @FXML
    private RadioButton viewByMonthRad;

    @FXML
    private RadioButton viewByWeekRad;

    @FXML
    private Button viewCustomersBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button viewReportsBtn;


    Stage stage;
    Parent scene;



    /**
     * deletes the selected appointment from the database/AppointmentsDAO appointment list
     */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws Exception {
        Appointment app = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (app == null) {
            Helper.alertError("No appointment is selected for deletion");
            return;
        }
        AppointmentsDAO.deleteAppointment(app);
        AppointmentsDAO.getAllAppointments().remove(app);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Appointment with ID " + app.getAppointmentId() + " and type " + app.getType() + " has been deleted.");
        alert.showAndWait();
    }


    /**
     * opens the add appointment fxml file
     */
    @FXML
    void onActionOpenAddAppointment(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * opens the customers fxml file
     */
    @FXML
    void onActionOpenCustomers(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * opens the modify appointment fxml file with the values loaded of the selected appointment
     */
    @FXML
    void onActionOpenModifyAppointment(ActionEvent event) throws Exception {
        if(appointmentsTableView.getSelectionModel().getSelectedItem() == null){
            Helper.alertError("No appointment is selected for modification.");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/ModifyAppointment.fxml"));
        loader.load();
        ModifyAppointmentController mac = loader.getController();
        mac.displayAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** changes contents of the table based on the selected radio button*/
    @FXML
    void onActionGrp1(ActionEvent event) {
        if(unsortedViewRad.isSelected()){
            appointmentsTableView.setItems(AppointmentsDAO.getAllAppointments());
        } else if(viewByMonthRad.isSelected()){
            try {
                appointmentsTableView.setItems(AppointmentsDAO.viewByMonth());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(viewByWeekRad.isSelected()) {
            try {
                appointmentsTableView.setItems(AppointmentsDAO.viewByWeek());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /** closes program*/
    @FXML
    void onActionLogout(ActionEvent event) {
    System.exit(0);
    }
    /** opens reports fxml file*/
    @FXML
    void onActionOpenReports(ActionEvent event) throws Exception{
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * initializes the appointments table view
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            AppointmentsDAO.getAllAppointments().clear();
            AppointmentsDAO.initializeAppointments();
        }catch (Exception e){
            System.out.println(e);
        }


        appointmentsTableView.setItems(AppointmentsDAO.getAllAppointments());
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
}
