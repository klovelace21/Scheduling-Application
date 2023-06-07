package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.AppointmentsDAO;
import sample.DAO.CustomersDAO;
import sample.Helper;
import sample.Main;
import sample.Model.Appointment;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * this class is the controller for the add appointment fxml file
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private Button addAppointmentBtn;
    @FXML
    private TextField appointmentIdTxt;
    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private TextField customerIdTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeHr;
    @FXML
    private TextField endTimeMin;
    @FXML
    private TextField locationTxt;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeHr;
    @FXML
    private TextField startTimeMin;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private TextField userIdTxt;

    Stage stage;
    Parent scene;

    /**
     * adds the appointment to the database with provided input values
     * the lambda expression converts the hours and minutes provided in the application in a parsable string
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws Exception {
        //ensuring required information is present
        if(titleTxt.getText().isEmpty() || descriptionTxt.getText().isEmpty() ||
        locationTxt.getText().isEmpty() || contactComboBox.getValue().isEmpty() || typeTxt.getText().isEmpty() ||
        startDatePicker.getValue() == null || startTimeHr.getText().isEmpty() || startTimeMin.getText().isEmpty() ||
        endDatePicker.getValue() == null || endTimeHr.getText().isEmpty() || endTimeMin.getText().isEmpty() ||
        customerIdTxt.getText().isEmpty() || userIdTxt.getText().isEmpty()){
            Helper.alertError("Please ensure all required fields are filled out.");
            return;
        }

        //converts the hours and minutes provided into an acceptable string
        HoursMinutes hrMin = (String hour, String min) -> {

            if (startTimeHr.getText().length() < 2) {
                 hour = "0" + startTimeHr.getText();
            }
            if (startTimeMin.getText().length() < 2) {
                 min = "0" + startTimeMin.getText();
            }
            return hour + ":" + min + ":00";
        };


        //Obtaining startDateTime from input
        String startDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startDateTimeString = startDate + " " + hrMin.convertHoursMins(startTimeHr.getText(), startTimeMin.getText());
        LocalDateTime startDateTime = Helper.convertToLocalDateTime(startDateTimeString);
        Timestamp startDateTimestamp = Timestamp.valueOf(startDateTime);


        //Obtaining endDateTime from input
        String endDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDateTimeString = endDate + " " + hrMin.convertHoursMins(endTimeHr.getText(), endTimeMin.getText());
        LocalDateTime endDateTime = Helper.convertToLocalDateTime(endDateTimeString);
        Timestamp endDateTimeStamp = Timestamp.valueOf(endDateTime);

        //LocalDateTime zoned to the system default and converted to EST
        ZonedDateTime startTimeZoned = ZonedDateTime.of(startDateTime, ZoneId.systemDefault());
        ZonedDateTime endTimeZoned = ZonedDateTime.of(endDateTime, ZoneId.systemDefault());
        ZonedDateTime startTimeEST = startTimeZoned.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endTimeEst = endTimeZoned.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalTime startTime = startTimeEST.toLocalTime();
        LocalTime endTime = endTimeEst.toLocalTime();
        LocalTime businessOpens = LocalTime.of(8, 0, 0);
        LocalTime businessCloses = LocalTime.of(22, 0, 0);

        DayOfWeek startDay = startTimeEST.toLocalDate().getDayOfWeek();
        DayOfWeek endDay = endTimeEst.toLocalDate().getDayOfWeek();
        if (startDay.getValue() < DayOfWeek.MONDAY.getValue() || startDay.getValue() > DayOfWeek.FRIDAY.getValue()
                || endDay.getValue() < DayOfWeek.MONDAY.getValue() || endDay.getValue() > DayOfWeek.FRIDAY.getValue()) {
            Helper.alertError("Requested appointment date is outside of business operational days (Monday - Friday)");
            return;
        }
        if (startTime.isBefore(businessOpens) || startTime.isAfter(businessCloses) || endTime.isAfter(businessCloses) || endTime.isBefore(businessOpens)) {
            Helper.alertError("Requested appointment time is outside of operational hours. Acceptable Hours: 8:00am-10:00pm EST");
            return;
        }

        if (AppointmentsDAO.checkForOverlappingTimesAdd(Helper.getLocalDateTimeInTimestampUTC(startDateTime),
                Helper.getLocalDateTimeInTimestampUTC(endDateTime), Integer.valueOf(customerIdTxt.getText()))) {
            Helper.alertError("Appointment overlaps with another appointment already scheduled for this customer.");
            return;
        }

        Appointment appointment = new Appointment();

        appointment.setTitle(titleTxt.getText());
        appointment.setDescription(descriptionTxt.getText());
        appointment.setLocation(locationTxt.getText());
        appointment.setContact(contactComboBox.getValue());
        appointment.setType(typeTxt.getText());
        appointment.setStart(startDateTimestamp);
        appointment.setEnd(endDateTimeStamp);
        if (CustomersDAO.customerIdExists(Integer.valueOf(customerIdTxt.getText())) == false) {
            Helper.alertError("Customer ID " + customerIdTxt.getText() + " does not exist.");
            return;
        }
        appointment.setCustomerId(Integer.valueOf(customerIdTxt.getText()));
        appointment.setUserId(Integer.valueOf(userIdTxt.getText()));

        AppointmentsDAO.insertAppointmentIntoDatabase(appointment);

        appointment.setAppointmentId(AppointmentsDAO.getMaxAppointmentId());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * navigates back to the appointments fxml file
     */
    @FXML
    void onActionCancel(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * initializes the values in the contact combo box
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting contact names in combo box
        try {
            contactComboBox.setItems(Helper.getContactNames());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /** interface for converting the hours/minutes lambda expression*/
   interface HoursMinutes{
         String convertHoursMins(String hr, String min);
   }
}

