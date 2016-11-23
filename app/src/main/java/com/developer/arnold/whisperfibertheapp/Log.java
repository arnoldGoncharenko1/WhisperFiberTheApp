package com.developer.arnold.whisperfibertheapp;

/**
 * Created by Arnold on 11/12/2016.
 */

public class Log {
    int logID;
    String logName;
    String logDate;
    String logTime;
    String logBuilding;
    String logUnit;
    int logTypeOfService;
    String logNotes;
    int logCustomer;

    public Log() {
        logID = 0;
        logName = "";
        logDate = "";
        logTime = "";
        logBuilding = "";
        logUnit = "";
        logTypeOfService = Constants.Install;
        logNotes = "";
        logCustomer = 0;
    }

    public Log (int tempLogID, String tempName, String tempDate, String tempTime, String tempBuilding, String tempUnit, int tempTypeOfService, String tempNotes, int tempCustomer) {
        logID = tempLogID;
        logName = tempName;
        logDate = tempDate;
        logTime = tempTime;
        logBuilding = tempBuilding;
        logUnit = tempUnit;
        logTypeOfService = tempTypeOfService;
        logNotes = tempNotes;
        logCustomer = tempCustomer;
    }
}
