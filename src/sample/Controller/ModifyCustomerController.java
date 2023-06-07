package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.AppointmentsDAO;
import sample.DAO.CountriesDAO;
import sample.DAO.CustomersDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Country;
import sample.Model.Customer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * controller for the modify customer fxml file
 */
public class ModifyCustomerController {

    @FXML
    private TextField addressTxt;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField customerNameTxt;

    @FXML
    private ComboBox<String> divisionComboBox;

    @FXML
    private TextField phoneNumberTxt;

    @FXML
    private TextField postalCodeTxt;

    @FXML
    private Button saveCustomerBtn;

    Stage stage;
    Parent scene;

    /**
     * navigates back to the customers fxml file
     */
    @FXML
    void onActionCancel(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * modifies the selected customer with the provided input values
     */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws Exception {
        if(customerNameTxt.getText().isEmpty() || addressTxt.getText().isEmpty() ||
                postalCodeTxt.getText().isEmpty() || phoneNumberTxt.getText().isEmpty() || countryComboBox.getValue().isEmpty() ||
                divisionComboBox.getValue().isEmpty()) {
            Helper.alertError("Please ensure all required fields are filled out.");
            return;
        }
        int appointmentId = Integer.valueOf(customerIdTxt.getText());
        Customer customer = CustomersDAO.getCustomer(appointmentId);
        customer.setCustomerName(customerNameTxt.getText());
        customer.setAddress(addressTxt.getText());
        customer.setPostalCode(postalCodeTxt.getText());
        customer.setAddress(addressTxt.getText());
        customer.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
        customer.setLastUpdatedBy(Helper.getUser());
        customer.setDivision(String.valueOf(divisionComboBox.getValue()));
        CustomersDAO.modifyCustomer(customer);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * sets values in the division combo box based on the country combo box selected value
     */
    @FXML
    void onActionSetDivisions(ActionEvent event) {
        if (countryComboBox.getSelectionModel().getSelectedItem().equals("U.S")) {
            divisionComboBox.setItems(CountriesDAO.getCountry("U.S").getDivisions());
        } else if (countryComboBox.getSelectionModel().getSelectedItem().equals("UK")) {
            divisionComboBox.setItems(CountriesDAO.getCountry("UK").getDivisions());
        } else if (countryComboBox.getSelectionModel().getSelectedItem().equals("Canada")) {
            divisionComboBox.setItems(CountriesDAO.getCountry("Canada").getDivisions());
        }
    }

    /**
     * method used to load the selected customers values
     */
    public void displayCustomer(Customer customer) throws Exception {
        customerIdTxt.setText(String.valueOf(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        addressTxt.setText(customer.getAddress());
        postalCodeTxt.setText(customer.getPostalCode());
        phoneNumberTxt.setText(customer.getPhoneNumber());
        CountriesDAO.getCountries().clear();
        CountriesDAO.initializeCountries();

        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for (Country country : CountriesDAO.getCountries()) {
            countryNames.add(country.getName());
        }
        countryComboBox.setItems(countryNames);
        int country = CustomersDAO.convertDivIdToCountryId(CustomersDAO.convertNameToDivId(customer.getDivision()));

        if (country == 1) {
            countryComboBox.setValue(countryNames.get(0));
            divisionComboBox.setItems(CountriesDAO.getCountry("U.S").getDivisions());

        } else if (country == 2) {
            countryComboBox.setValue(countryNames.get(1));
            divisionComboBox.setItems(CountriesDAO.getCountry("UK").getDivisions());

        } else if (country == 3) {
            countryComboBox.setValue(countryNames.get(2));
            divisionComboBox.setItems(CountriesDAO.getCountry("Canada").getDivisions());
        }
        divisionComboBox.setValue(customer.getDivision());
    }


}
