package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.CustomersDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Customer;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * controller for the customer fxml file
 */
public class CustomersController implements Initializable {

    @FXML
    private Button addCustomerBtn;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, Timestamp> createDateCol;

    @FXML
    private TableColumn<Customer, String> createdByCol;

    @FXML
    private TableColumn<Customer, Integer> customerIdCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private Button deleteCustomerBtn;

    @FXML
    private TableColumn<Customer, String> divisionCol;

    @FXML
    private TableColumn<Customer, Timestamp> lastUpdateCol;

    @FXML
    private TableColumn<Customer, String> lastUpdatedByCol;

    @FXML
    private Button modifyCustomerBtn;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, String> postalCodeCol;

    @FXML
    private Button viewAppointmentsBtn;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button viewReportsBtn;

    Stage stage;
    Parent scene;

    /**
     * deletes the selected customer from the database and CustomersDAO customer list
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws Exception {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Helper.alertError("No customer is selected for deletion");
            return;
        }
        if (CustomersDAO.containsAssociatedAppointments(customer)) {
            Helper.alertError("This customer still has appointments associated to them." +
                    " Please delete all associated appointments before attempting deletion.");
            return;
        } else {
            CustomersDAO.deleteCustomer(customer);
            CustomersDAO.getAllCustomers().remove(customer);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer with ID " + customer.getCustomerId() + " has been deleted.");
            alert.showAndWait();
        }
    }

    /**
     * opens the add customer fxml file
     */
    @FXML
    void onActionOpenAddCustomer(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * opens the appointments fxml file
     */
    @FXML
    void onActionOpenAppointments(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * opens the modify customer fxml file with the selected customer values loaded
     */
    @FXML
    void onActionOpenModifyCustomer(ActionEvent event) throws Exception {
        if(customerTableView.getSelectionModel().getSelectedItem() == null){
            Helper.alertError("No customer is selected for modification.");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/ModifyCustomer.fxml"));
        loader.load();
        ModifyCustomerController mcc = loader.getController();
        mcc.displayCustomer(customerTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
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
     * initializes the customer table view
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            CustomersDAO.getAllCustomers().clear();
            CustomersDAO.initializeCustomers();
        } catch (Exception e) {
            System.out.println(e);
        }
        customerTableView.setItems(CustomersDAO.getAllCustomers());
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));

    }
}
