package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * controller for the modify appointment fxml file
 */
public class ModifyAppointmentController {

    @FXML
    private Button ModifyAppointmentBtn;

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
     * modifies selected appointment with the provided values
     * the lambda expression converts the hours and minutes provided in the application in a parsable string
     */
    @FXML
    void onActionModifyAppointment(ActionEvent event) {
        //ensuring required information is present
        if(titleTxt.getText().isEmpty() || descriptionTxt.getText().isEmpty() ||
                locationTxt.getText().isEmpty() || contactComboBox.getValue().isEmpty() || typeTxt.getText().isEmpty() ||
                startDatePicker.getValue() == null || startTimeHr.getText().isEmpty() || startTimeMin.getText().isEmpty() ||
                endDatePicker.getValue() == null || endTimeHr.getText().isEmpty() || endTimeMin.getText().isEmpty() ||
                customerIdTxt.getText().isEmpty() || userIdTxt.getText().isEmpty()){
            Helper.alertError("Please ensure all text fields are filled out.");
            return;
        }

        //converts the hours and minutes provided into an acceptable string
        AddAppointmentController.HoursMinutes hrMin = (String hour, String min) -> {

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
        String endDateTimeString = endDate + " " + hrMin.convertHoursMins(endTimeHr.getText(), startTimeMin.getText());
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
        int appointmentId = Integer.valueOf(appointmentIdTxt.getText());
        if (AppointmentsDAO.checkForOverlappingTimesModify(Helper.getLocalDateTimeInTimestampUTC(startDateTime),
                Helper.getLocalDateTimeInTimestampUTC(endDateTime), Integer.valueOf(customerIdTxt.getText()), appointmentId)) {
            Helper.alertError("Appointment overlaps with another appointment already scheduled for this customer.");
            return;
        }
        Appointment app = AppointmentsDAO.getAppointment(appointmentId);
        app.setTitle(titleTxt.getText());
        app.setDescription(descriptionTxt.getText());
        app.setLocation(locationTxt.getText());
        app.setType(typeTxt.getText());
        app.setStart(startDateTimestamp);
        app.setEnd(endDateTimeStamp);
        app.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
        app.setLastUpdatedBy(Helper.getUser());

        try {
            if (CustomersDAO.customerIdExists(Integer.valueOf(customerIdTxt.getText())) == false) {
                Helper.alertError("Customer ID " + customerIdTxt.getText() + " does not exist.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        app.setCustomerId(Integer.valueOf(customerIdTxt.getText()));
        app.setUserId(Integer.valueOf(userIdTxt.getText()));
        app.setContact(contactComboBox.getValue());

        AppointmentsDAO.modifyAppointment(app);

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Main.class.getResource("View/Appointments.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
        }

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
     * method used to display the selected appointments values
     */
    public void displayAppointment(Appointment appointment) throws Exception {
        appointmentIdTxt.setText(String.valueOf(appointment.getAppointmentId()));
        titleTxt.setText(appointment.getTitle());
        descriptionTxt.setText(appointment.getDescription());
        locationTxt.setText(appointment.getLocation());
        contactComboBox.setItems(Helper.getContactNames());
        contactComboBox.setValue(appointment.getContact());
        typeTxt.setText(appointment.getType());
        startDatePicker.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        endDatePicker.setValue(appointment.getEnd().toLocalDateTime().toLocalDate());
        customerIdTxt.setText(String.valueOf(appointment.getCustomerId()));
        userIdTxt.setText(String.valueOf(appointment.getUserId()));
        startTimeHr.setText(String.valueOf(appointment.getStart().toLocalDateTime().getHour()));
        startTimeMin.setText(String.valueOf(appointment.getStart().toLocalDateTime().getMinute()));
        endTimeHr.setText(String.valueOf(appointment.getEnd().toLocalDateTime().getHour()));
        endTimeMin.setText(String.valueOf(appointment.getEnd().toLocalDateTime().getMinute()));
    }

}
