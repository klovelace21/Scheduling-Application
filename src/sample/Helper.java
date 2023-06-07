package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * hosts useful functions used throughout the program
 */
public class Helper {
    /**
     * returns the user that's logged into the program
     */
    public static String user;

    /**
     * generates an alert with content of provided string
     */
    public static void alertError(String errorToBeSaid) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setContentText(errorToBeSaid);
        alert.showAndWait();
    }



    /**
     * parses a LocalDateTime from a string
     */
    public static LocalDateTime convertToLocalDateTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * returns list of contact names from the database
     */
    public static ObservableList<String> getContactNames() throws Exception {
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        while (rs.next()) {
            contactNames.add(rs.getString("Contact_Name"));
        }
        return contactNames;
    }

    /**
     * returns the user logged in to the application
     */
    public static String getUser() {
        return user;
    }

    /**
     * converts LocalDateTime to Timestamp in UTC timezone
     */
    public static Timestamp getLocalDateTimeInTimestampUTC(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        zdt = ZonedDateTime.ofInstant(zdt.toInstant(), ZoneId.of("UTC"));
        return Timestamp.valueOf(zdt.toLocalDateTime());
    }




    /**
     * writes to the login_activity.txt file whether the login was successful or not, along with a timestamp
     */
    public static void writeToLoginActivity(String string) {
        try {
            FileWriter file = new FileWriter("login_activity.txt", true);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(string);
            writer.newLine();
            writer.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** generates information alert with provided string*/
    public static void generateInformationAlert(String toBeSaid){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(toBeSaid);
        alert.showAndWait();
    }
}

