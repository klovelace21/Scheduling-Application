package sample.Model;

import java.sql.Timestamp;

/**
 * Country model class
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;
    private String division;

    public Customer() {

    }

    /**
     * returns customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * sets customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * returns customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * sets customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * returns address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * sets postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * returns phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * sets phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * returns create date
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * sets create date
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * sets createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * returns time this customer was last updated
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * sets last time this customer was updated
     */
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * returns who last updated this customer
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * sets who last updated this customer
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * returns this customers division
     */
    public String getDivision() {
        return division;
    }

    /**
     * sets this customers division
     */
    public void setDivision(String division) {
        this.division = division;
    }
}
