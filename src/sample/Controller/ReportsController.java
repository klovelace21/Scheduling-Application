package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.DAO.AppointmentsDAO;
import sample.DAO.CountriesDAO;
import sample.DAO.CustomersDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Country;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * this class is the controller for the reports fxml file
 */
public class ReportsController implements Initializable {

    @FXML
    private Button viewAppointmentsBtn;

    @FXML
    private Button viewCustomersBtn;

    @FXML
    private Button calculateBtn;

    @FXML
    private ComboBox<Month> monthComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Label numberOfAppointmentsLbl;

    @FXML
    private ComboBox<String> contactComboBox;

    @FXML
    private Button viewScheduleBtn;

    @FXML
    private Label numberOfCustomersLbl;

    @FXML
    private ComboBox<String> countryComboBox;

    Stage stage;
    Parent scene;

    /**
     * opens the selected contacts schedule
     */
    @FXML
    void onActionViewSchedule(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Schedules.fxml"));
        loader.load();
        SchedulesController sc = loader.getController();
        sc.displayContactsAppointments(contactComboBox.getValue());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * calculates number of appointments for specified month and type
     */
    @FXML
    void onActionCalculate(ActionEvent event) {
        numberOfAppointmentsLbl.setText(String.valueOf(AppointmentsDAO.calculateNumberOfAppointments(monthComboBox.getValue(), typeComboBox.getValue())));
    }

    /**
     * opens appointments fxml file
     */
    @FXML
    void onActionViewAppointments(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * opens customers fxml file
     */
    @FXML
    void onActionViewCustomers(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** displays number of customers located in selected country*/
    @FXML
    void onActionCalculateCustomersPerCountry(ActionEvent event) {
        int toDisplay =CountriesDAO.calculateNumberOfCustomers(CountriesDAO.getCountry(countryComboBox.getValue()));
        numberOfCustomersLbl.setText(String.valueOf(toDisplay));
    }

    /**
     * initializes reports fxml file values
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Month> months = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            months.add(month);
        }
        monthComboBox.setItems(months);
        typeComboBox.setItems(AppointmentsDAO.getListOfTypesPresent());
        try {
            contactComboBox.setItems(Helper.getContactNames());

            CustomersDAO.getAllCustomers().clear();
            CustomersDAO.initializeCustomers();

            CountriesDAO.getCountries().clear();
            CountriesDAO.initializeCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            for (Country country : CountriesDAO.getCountries()) {
                countryNames.add(country.getName());
            }
            countryComboBox.setItems(countryNames);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
