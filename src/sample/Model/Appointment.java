package sample.Model;

import java.sql.Timestamp;

/**
 * Appointment model class
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private String contact;

    public Appointment() {

    }

    /**
     * returns appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * sets appointment ID
     */
    public void setAppointmentId(int id) {
        this.appointmentId = id;
    }


    /**
     * returns title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * returns description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns location
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * returns type
     */
    public String getType() {
        return type;
    }

    /**
     * sets type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * returns start time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * sets start time
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * returns end time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * sets end time
     */
    public void setEnd(Timestamp end) {
        this.end = end;
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
     * returns createdBy
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
     * returns last update time
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * sets last update time
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * returns who last updated appointment
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * sets who last updated appointment
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * returns customer ID associated with appointment
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * sets customer ID associated with appointment
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * returns user ID associated with the appointment
     */
    public int getUserId() {
        return userId;
    }

    /**
     * sets user ID associated with the appointment
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * returns contact associated with the appointment
     */
    public String getContact() {
        return contact;
    }

    /**
     * sets contact associated with the appointment
     */
    public void setContact(String contact) {
        this.contact = contact;
    }


}
