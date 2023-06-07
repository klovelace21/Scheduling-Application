package sample.DAO;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Helper;
import sample.JDBC;
import sample.Model.Appointment;

import javax.print.DocFlavor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * DAO class for the appointment model class
 */
public class AppointmentsDAO {
    /**
     * list of all appointments in the database
     */
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * initializes all appointments from the database into the appointments list
     */
    public static void initializeAppointments() throws Exception {
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getInt("Appointment_ID"));
            appointment.setTitle(rs.getString("Title"));
            appointment.setDescription(rs.getString("Description"));
            appointment.setLocation(rs.getString("Location"));
            appointment.setType(rs.getString("Type"));
            appointment.setStart(rs.getTimestamp("Start"));
            appointment.setEnd(rs.getTimestamp("End"));
            appointment.setCreateDate(rs.getTimestamp("Create_Date"));
            appointment.setCreatedBy(rs.getString("Created_By"));
            appointment.setLastUpdate(rs.getTimestamp("Last_Update"));
            appointment.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            appointment.setCustomerId(rs.getInt("Customer_ID"));
            appointment.setUserId(rs.getInt("User_ID"));
            appointment.setContact(convertIdToContactName(rs.getInt("Contact_Id")));
            appointments.add(appointment);
        }
    }

    /**
     * returns appointments list
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return appointments;
    }

    /**
     * returns appointment associated with provided appointment ID
     */
    public static Appointment getAppointment(int appointmentId) {
        for (Appointment app : appointments) {
            if (app.getAppointmentId() == appointmentId) {
                return app;
            }
        }
        return null;
    }

    /**
     * provides contact name associated with the provided contact ID
     */
    public static String convertIdToContactName(int id) throws Exception {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String name = "n";
        while (rs.next()) {
            name = rs.getString("Contact_Name");
        }
        return name;
    }

    /**
     * deletes provided appointment from the database
     */
    public static int deleteAppointment(Appointment app) throws Exception {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, app.getAppointmentId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * modifies the provided appointment with given values
     */
    public static int modifyAppointment(Appointment app) {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, app.getTitle());
            ps.setString(2, app.getDescription());
            ps.setString(3, app.getLocation());
            ps.setString(4, app.getType());
            ps.setTimestamp(5, app.getStart());
            ps.setTimestamp(6, app.getEnd());
            ps.setTimestamp(7, app.getLastUpdate());
            ps.setString(8, app.getLastUpdatedBy());
            ps.setInt(9, app.getCustomerId());
            ps.setInt(10, app.getUserId());
            ps.setInt(11, convertNameToContactID(app.getContact()));
            ps.setInt(12, app.getAppointmentId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    /**
     * returns the max appointment ID from the database
     */
    public static int getMaxAppointmentId() throws Exception {
        String sql = "SELECT MAX(Appointment_ID) FROM appointments";
        int returnObj = 0;

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            returnObj = rs.getInt("MAX(Appointment_ID)");
        }

        return returnObj;
    }

    /**
     * inserts provided appointment into the database
     */
    public static int insertAppointmentIntoDatabase(Appointment appointment) throws Exception {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        LocalDateTime now = LocalDateTime.now();
        ps.setTimestamp(7, Timestamp.valueOf(now));
        ps.setString(8, Helper.getUser());
        ps.setTimestamp(9, Timestamp.valueOf(now));
        ps.setString(10, Helper.getUser());
        ps.setInt(11, appointment.getCustomerId());
        ps.setInt(12, appointment.getUserId());
        ps.setInt(13, convertNameToContactID(appointment.getContact()));
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * provides contactId associated with the given name
     */
    public static int convertNameToContactID(String name) throws Exception {

        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        int id = 0;
        while (rs.next()) {
            id = rs.getInt("Contact_ID");
        }
        return id;
    }

    /**
     * checks for overlapping times used in the add appointment controller
     */
    public static boolean checkForOverlappingTimesAdd(Timestamp st, Timestamp et, int custId) {
        String sql = "SELECT Customer_ID, Start, End FROM APPOINTMENTS";

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //converting sql values back to UTC since they come out in local time
                LocalDateTime rsStart = rs.getTimestamp("Start").toLocalDateTime();
                Timestamp rsStartTime = Helper.getLocalDateTimeInTimestampUTC(rsStart);
                LocalDateTime rsEnd = rs.getTimestamp("End").toLocalDateTime();
                Timestamp rsEndTime = Helper.getLocalDateTimeInTimestampUTC(rsEnd);
                if (st.after(rsStartTime) && st.before(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
                if (et.after(rsStartTime) && et.before(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
                //checks a slew of edge cases
                if (st.equals(rsStartTime) && et.equals(rsEndTime) || st.equals(rsStartTime) && et.after(rsEndTime) || et.equals(rsEndTime) && st.before(rsStartTime)
                        || st.before(rsStartTime) && et.after(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * checks for overlapping times used in the modify appointment controller
     */
    public static boolean checkForOverlappingTimesModify(Timestamp st, Timestamp et, int custId, int appointmentId) {
        String sql = "SELECT Customer_ID, Start, End FROM APPOINTMENTS WHERE NOT Appointment_ID = ?";


        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //converting sql values back to UTC since they come out in local time
                LocalDateTime rsStart = rs.getTimestamp("Start").toLocalDateTime();
                Timestamp rsStartTime = Helper.getLocalDateTimeInTimestampUTC(rsStart);
                LocalDateTime rsEnd = rs.getTimestamp("End").toLocalDateTime();
                Timestamp rsEndTime = Helper.getLocalDateTimeInTimestampUTC(rsEnd);
                if (st.after(rsStartTime) && st.before(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
                if (et.after(rsStartTime) && et.before(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
                //checks a slew of edge cases
                if (st.equals(rsStartTime) && et.equals(rsEndTime) || st.equals(rsStartTime) && et.after(rsEndTime) || et.equals(rsEndTime) && st.before(rsStartTime)
                        || st.before(rsStartTime) && et.after(rsEndTime)) {
                    if (rs.getInt("Customer_ID") == custId) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * returns a list of appointments that share the same month as the current date
     */
    public static ObservableList<Appointment> viewByMonth() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID, MONTH(Start) as Month FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        LocalDateTime now = LocalDateTime.now();
        while (rs.next()) {

            if (rs.getInt("Month") == now.getMonthValue()) {
                appointments.add(AppointmentsDAO.getAppointment(rs.getInt("Appointment_ID")));
            }
        }
        return appointments;
    }

    /**
     * returns list of appointments that share the same week as the current date
     */
    public static ObservableList<Appointment> viewByWeek() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID, WEEK(Start) as Week FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        LocalDateTime now = LocalDateTime.now();
        TemporalField week = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekValue = now.get(week);
        while (rs.next()) {

            if (rs.getInt("Week") == weekValue) {
                appointments.add(AppointmentsDAO.getAppointment(rs.getInt("Appointment_ID")));
            }
        }
        return appointments;
        }
    /** returns list of appointments that start within 15 minutes of the current time*/
    public static ObservableList<Appointment> within15Minutes(ObservableList<Appointment> appointmentList){
        ObservableList<Appointment> appsToReturn = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        for(Appointment app: appointmentList) {
            if(ChronoUnit.MINUTES.between(now, app.getStart().toLocalDateTime()) <= 15 && ChronoUnit.MINUTES.between(now, app.getStart().toLocalDateTime()) >= 0){
                appsToReturn.add(app);
            }
        }
        return appsToReturn;
    }
    /** returns a list of unique appointment types found in the database*/
    public static ObservableList<String> getListOfTypesPresent(){
        ObservableList<String> types = FXCollections.observableArrayList();
        for(Appointment appointment: AppointmentsDAO.getAllAppointments()){
            if(types.contains(appointment.getType()) == false){
                types.add(appointment.getType());
            }
        }
        return types;
    }
    /** returns number of customers by given month and country*/
    public static int calculateNumberOfAppointments(Month month, String type){
        int numberOfAppointments = 0;
        for(Appointment app: AppointmentsDAO.getAllAppointments()){
            if(app.getStart().toLocalDateTime().getMonth() == month && app.getType().equals(type)){
                numberOfAppointments++;
            }
        }
        return numberOfAppointments;
    }
    /** returns a list of appointments associated with given contact*/
    public static ObservableList<Appointment> getAppointmentsAssociatedWithContact(String contact){
        ObservableList<Appointment> apps = FXCollections.observableArrayList();
        for(Appointment app: AppointmentsDAO.getAllAppointments()){
            if(app.getContact().equals(contact)){
                apps.add(app);
            }
        }
        return apps;
    }

}

