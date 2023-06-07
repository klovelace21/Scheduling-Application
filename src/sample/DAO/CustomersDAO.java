package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Helper;
import sample.JDBC;
import sample.Model.Appointment;
import sample.Model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * DAO class for the customer model class
 */
public class CustomersDAO {
    /**
     * list of customers from the database
     */
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * initializes the customers from the database into the customers list
     */
    public static void initializeCustomers() throws Exception {
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer cust = new Customer();
            cust.setCustomerId(rs.getInt("Customer_ID"));
            cust.setCustomerName(rs.getString("Customer_Name"));
            cust.setAddress(rs.getString("Address"));
            cust.setPostalCode(rs.getString("Postal_Code"));
            cust.setPhoneNumber(rs.getString("Phone"));
            cust.setCreateDate(rs.getTimestamp("Create_Date"));
            cust.setCreatedBy(rs.getString("Created_By"));
            cust.setLastUpdated(rs.getTimestamp("Last_Update"));
            cust.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            String divName = convertDivIdToName(rs.getInt("Division_ID"));
            cust.setDivision(divName);
            customers.add(cust);
        }
    }

    /**
     * returns the customers list
     */
    public static ObservableList<Customer> getAllCustomers() {
        return customers;
    }

    /**
     * inserts the given customer into the database
     */
    public static int insertCustomerIntoDatabase(Customer customer) throws Exception {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhoneNumber());
        ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, Helper.getUser());
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, Helper.getUser());
        ps.setInt(9, convertNameToDivId(customer.getDivision()));
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * provides division ID associated with provided name
     */
    public static int convertNameToDivId(String name) throws Exception {
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("Division_ID");
        }
        return 0;
    }

    /**
     * provides max customer ID from the database
     */
    public static int getMaxCustomerId() {
        String sql = "SELECT MAX(Customer_ID) FROM customers";
        int returnObj = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                returnObj = rs.getInt("MAX(Customer_ID)");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnObj;
    }

    /**
     * provides name associated with given division id
     */
    public static String convertDivIdToName(int id) throws Exception {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getString("Division");
        }
        return null;
    }

    /**
     * provides country ID associated with given division ID
     */
    public static int convertDivIdToCountryId(int id) throws Exception {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("Country_ID");
        }
        return -1;
    }

    /**
     * modifies the provided customers values in the database
     */
    public static int modifyCustomer(Customer customer) throws Exception {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?," +
                " Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhoneNumber());
        ps.setTimestamp(5, customer.getLastUpdated());
        ps.setString(6, customer.getLastUpdatedBy());
        ps.setInt(7, convertNameToDivId(customer.getDivision()));
        ps.setInt(8, customer.getCustomerId());
        return ps.executeUpdate();
    }

    /**
     * returns customer object associated with the provided ID
     */
    public static Customer getCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    /**
     * checks to the see if the provided customer is associated with any appointments in the database.
     */
    public static boolean containsAssociatedAppointments(Customer customer) throws Exception {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.getCustomerId());
        ObservableList<Integer> relatedAppointments = FXCollections.observableArrayList();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            relatedAppointments.add(rs.getInt("Appointment_ID"));
        }
        if (relatedAppointments.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * deletes the given customer from the database.
     */
    public static int deleteCustomer(Customer customer) throws Exception {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.getCustomerId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * verifies the provided customer ID exists within the database
     */
    public static boolean customerIdExists(int id) throws Exception {
        String sql = "Select Customer_ID from customers";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (id == rs.getInt("Customer_ID")) {
                return true;
            }
        }
        return false;
    }


}
