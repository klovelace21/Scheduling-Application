package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.AppointmentsDAO;
import sample.Helper;
import sample.JDBC;
import sample.Main;
import sample.Model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * controller for the login fxml file
 */
public class LoginController implements Initializable {
    Stage stage;
    Parent scene;


    @FXML
    private Button loginBtn;

    @FXML
    private Label passwordLbl;

    @FXML
    private TextField passwordTxtField;

    @FXML
    private Label timeZoneField;

    @FXML
    private Label timeZoneLbl;

    @FXML
    private Label usernameLbl;

    @FXML
    private TextField usernameTxtField;

    private String loginAlertError = "Invalid Username or Password";

    private String loginAlertTitle = "Error Dialog";

    /**
     * verifies the provided username/password and either opens the application or displays alert if information is wrong. Attempt is logged either way.
     * the lambda expression ensures the given username and password matches a users information in the database
     */
    @FXML
    public void onActionLogin(ActionEvent event) throws IOException {
        ZonedDateTime zdt = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Helper.user = usernameTxtField.getText();
        //confirms users login information
        ConfirmLogin confirmLogin = (String user, String pass) -> {
            boolean toReturn = false;
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            try {
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                toReturn = rs.next();
            } catch (Exception e) {
                System.out.println(e);
            }
            return toReturn;
        };

        try {

            if (confirmLogin.Login(usernameTxtField.getText(), passwordTxtField.getText())) {


                Helper.writeToLoginActivity("User " + Helper.getUser() + " successfully logged in at " + zdt);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                if(AppointmentsDAO.within15Minutes(AppointmentsDAO.getAllAppointments()).isEmpty()){
                    Helper.generateInformationAlert("There are no appointments within 15 minutes");
                } else{
                    String appsWithin15 = "Appointments within 15 minutes: \n";
                    for(Appointment app: AppointmentsDAO.within15Minutes(AppointmentsDAO.getAllAppointments())){
                        appsWithin15 += "Appointment ID: " + app.getAppointmentId() + " Start Time: " + app.getStart() + "\n";
                    }
                    Helper.generateInformationAlert(appsWithin15);
                }
            } else {
                Helper.alertError(loginAlertError);
                Helper.writeToLoginActivity("User " + Helper.getUser() + " gave invalid log-in at " + zdt);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * changes the language of the login screen based on the language settings of the users computer, as well as displays the users time zone
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = ResourceBundle.getBundle("sample/LanguageBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            usernameLbl.setText(rb.getString("Username"));
            passwordLbl.setText(rb.getString("Password"));
            timeZoneLbl.setText(rb.getString("Time") + " " + rb.getString("Zone"));
            loginBtn.setText(rb.getString("Login"));
            loginAlertError = rb.getString("Invalid") + " " + rb.getString("Username") + " " + rb.getString("or") + " " + rb.getString("Password");
            loginAlertTitle = rb.getString("Error") + " " + rb.getString("Dialog");
        }
        //getting time zone and setting label text
        ZoneId z = ZoneId.systemDefault();
        timeZoneField.setText(String.valueOf(z));

    }
    /** interface for confirm login lambda expression*/
    interface ConfirmLogin {
        boolean Login (String username, String password);
    }

}
