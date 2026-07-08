
package com.mycompany.cuvaproject.models;

/**
 *
 * @author FFIT221
 */
public class Binnacle {
    private int logId;
    private String idType;
    private String idNumber;
    private String firstName;
    private String lastName;
    private String role;
    private String dateTime; 



    public Binnacle(int logId, String idType, String idNumber, String firstName, String lastName, String role, String dateTime) {
        this.logId = logId;
        this.idType = idType;
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.dateTime = dateTime;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    
    
}
