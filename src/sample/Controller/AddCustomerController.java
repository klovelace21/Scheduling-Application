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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.CountriesDAO;
import sample.DAO.CustomersDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Country;
import sample.Model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class is the controller for the add customer fxml file
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Button addCustomerBtn;

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

    Stage stage;
    Parent scene;

    /**
     * adds customer to the database with provided input values
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws Exception {
        if(customerNameTxt.getText().isEmpty() || addressTxt.getText().isEmpty() ||
        postalCodeTxt.getText().isEmpty() || phoneNumberTxt.getText().isEmpty() || countryComboBox.getValue().isEmpty() ||
        divisionComboBox.getValue().isEmpty()) {
            Helper.alertError("Please ensure all required fields are filled out.");
            return;
        }
        Customer customer = new Customer();

        customer.setCustomerName(customerNameTxt.getText());
        customer.setAddress(addressTxt.getText());
        customer.setPostalCode(postalCodeTxt.getText());
        customer.setPhoneNumber(phoneNumberTxt.getText());
        customer.setDivision(divisionComboBox.getValue());

        CustomersDAO.insertCustomerIntoDatabase(customer);

        customer.setCustomerId(CustomersDAO.getMaxCustomerId());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

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
     * sets the division combo box based on the selected country combo box value
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
     * initializes the values in the countries combo box
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
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

