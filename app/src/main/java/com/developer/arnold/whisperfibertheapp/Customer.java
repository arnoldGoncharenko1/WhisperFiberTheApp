package com.developer.arnold.whisperfibertheapp;

/**
 * Created by Arnold on 11/12/2016.
 */

public class Customer {
    int custID;
    String custName;
    String custBuilding;
    String custUnit;
    String custDate;
    String custTime;
    String custEmail;
    String custPhoneNumber;
    String custPaymentMethod;
    int custActiveInActive;
    int custHardwareInstalled;
    int custHardwareReturned;
    String custNotes;

    public Customer() {
        custID = 0;
        this.custName = "";
        this.custBuilding = "";
        this.custUnit = "";
        this.custDate = "";
        this.custTime = "";
        this.custEmail = "";
        this.custPhoneNumber = "";
        this.custPaymentMethod = "";
        this.custActiveInActive = 0;
        this.custHardwareInstalled = 0;
        this.custHardwareReturned = 0;
        this.custNotes = "";
    }

    public Customer(int custID, String custName, String custBuilding, String custUnit, String custDate, String custTime, String custEmail, String custPhoneNumber, String custPaymentMethod, int custActiveInActive, int custHardwareInstalled, int custHardwareReturned, String custNotes) {
        this.custID = custID;
        this.custName = custName;
        this.custBuilding = custBuilding;
        this.custUnit = custUnit;
        this.custDate = custDate;
        this.custTime = custTime;
        this.custEmail = custEmail;
        this.custPhoneNumber = custPhoneNumber;
        this.custPaymentMethod = custPaymentMethod;
        this.custActiveInActive = custActiveInActive;
        this.custHardwareInstalled = custHardwareInstalled;
        this.custHardwareReturned = custHardwareReturned;
        this.custNotes = custNotes;
    }
}
