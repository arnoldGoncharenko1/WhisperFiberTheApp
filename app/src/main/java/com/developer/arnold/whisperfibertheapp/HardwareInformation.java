package com.developer.arnold.whisperfibertheapp;

/**
 * Created by Arnold on 11/19/2016.
 */

public class HardwareInformation{
    String serial;
    String secPin;
    String MAC1;
    String MAC2;
    String custId;

    public HardwareInformation() {
        serial = "";
        secPin = "";
        MAC1 = "";
        MAC2 = "";
        custId = "";
    }

    public HardwareInformation(String serial, String secPin, String MAC1, String MAC2, String custId) {
        this.serial = serial;
        this.secPin = secPin;
        this.MAC1 = MAC1;
        this.MAC2 = MAC2;
        this.custId = custId;
    }
}