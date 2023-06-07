package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;
import sample.Model.Country;
import sample.Model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DAO class for the country model class
 */
public class CountriesDAO {
    /**
     * list of countries from the database
     */
    private static ObservableList<Country> countries = FXCollections.observableArrayList();

    /**
     * initializes the countries from the database into the countries list
     */
    public static void initializeCountries() throws Exception {
        Country US = new Country("U.S", 1);
        Country UK = new Country("UK", 2);
        Country Canada = new Country("Canada", 3);
        countries.add(US);
        countries.add(UK);
        countries.add(Canada);
        for (Country country : countries) {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, country.getCountryId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                country.getDivisions().add(rs.getString("Division"));
            }
        }
    }

    /**
     * returns the countries list
     */
    public static ObservableList<Country> getCountries() {
        return countries;
    }

    /**
     * returns country from countries list with provided name
     */
    public static Country getCountry(String name) {

        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }

    /** calculates number of customers located in given country*/
    public static Integer calculateNumberOfCustomers(Country country){
        int result = 0;
        for(Customer customer: CustomersDAO.getAllCustomers()){
            if(country.getDivisions().contains(customer.getDivision())){
                result++;
            }
        }
        return result;
    }
}
