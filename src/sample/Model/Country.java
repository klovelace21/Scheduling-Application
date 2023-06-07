package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Country model class
 */
public class Country {
    private String name;
    private int countryId;
    private ObservableList<String> divisions = FXCollections.observableArrayList();

    /**
     * constructor for the country class
     */
    public Country(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;

    }

    /**
     * returns name
     */
    public String getName() {
        return name;
    }

    /**
     * sets name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * sets country ID
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * returns divisions associated with this country
     */
    public ObservableList<String> getDivisions() {
        return divisions;
    }

    /**
     * sets divisions associated with this country
     */
    public void setDivisions(ObservableList<String> divisions) {
        this.divisions = divisions;
    }
}
