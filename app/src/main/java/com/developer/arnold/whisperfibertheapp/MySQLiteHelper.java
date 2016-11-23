package com.developer.arnold.whisperfibertheapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.COLUMN_ID;

/**
 * Created by Arnold on 6/6/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static class FeedEntryLogs implements BaseColumns {
        //Basic info
        public static final String TABLE_NAME = "logs";
        public static final String COLUMN_NAME_LOG_NAME = "Name";
        public static final String COLUMN_NAME_LOG_DATE = "Date";
        public static final String COLUMN_NAME_LOG_TIME = "Time";
        public static final String COLUMN_NAME_LOG_BUILDING = "Building";
        public static final String COLUMN_NAME_LOG_UNIT = "Unit";
        public static final String COLUMN_NAME_LOG_TYPEOFSERVICE = "TypeofService";
        public static final String COLUMN_NAME_LOG_NOTES = "Notes";
        public static final String COLUMN_NAME_CUSTOMER = "Cust";
    }

    public static class FeedEntryCustomer implements BaseColumns {
        //Basic info
        public static final String TABLE_NAME = "customers";
        public static final String COLUMN_NAME_CUST_NAME = "Name";
        public static final String COLUMN_NAME_CUST_BUILDING = "Building";
        public static final String COLUMN_NAME_CUST_UNIT = "Unit";
        public static final String COLUMN_NAME_CUST_DATE = "Date";
        public static final String COLUMN_NAME_CUST_TIME = "Time";
        public static final String COLUMN_NAME_CUST_EMAIL = "Email";
        public static final String COLUMN_NAME_CUST_PHONE_NUMBER = "PhoneNumber";
        public static final String COLUMN_NAME_CUST_PAYMENT_METHOD = "PaymentMethod";
        public static final String COLUMN_NAME_CUST_ACTIVE = "Active";
        public static final String COLUMN_NAME_CUST_HARDDWARE_INSTALLED = "HardwareInstalled";
        public static final String COLUMN_NAME_CUST_HARDWARE_RETURNED = "HardwareReturned";
        public static final String COLUMN_NAME_CUST_NOTES = "Notes";
    }

    public static class FeedEntryCustomerHardware implements BaseColumns {
        //Basic info
        public static final String TABLE_NAME = "customerHardwareInformation";
        public static final String COLUMN_NAME_CUST_ID = "custId";
        public static final String COLUMN_NAME_CUST_SERIAL = "serial";
        public static final String COLUMN_NAME_CUST_SECPIN = "SecurityPin";
        public static final String COLUMN_NAME_CUST_MAC1 = "MAC1";
        public static final String COLUMN_NAME_CUST_MAC2 = "MAC2";

        String serial;
        String SecPin;
        String MAC1;
        String MAC2;
    }

    private static final String DATABASE_NAME = "whisperFiberDB.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_LOGS = "create table "
            + FeedEntryLogs.TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + FeedEntryLogs.COLUMN_NAME_LOG_NAME
            + " text, " + FeedEntryLogs.COLUMN_NAME_LOG_DATE
            + " text, " + FeedEntryLogs.COLUMN_NAME_LOG_TIME
            + " text, " + FeedEntryLogs.COLUMN_NAME_LOG_BUILDING
            + " text, " + FeedEntryLogs.COLUMN_NAME_LOG_UNIT
            + " text, " + FeedEntryLogs.COLUMN_NAME_LOG_TYPEOFSERVICE
            + " integer, " + FeedEntryLogs.COLUMN_NAME_LOG_NOTES
            + " text, " + FeedEntryLogs.COLUMN_NAME_CUSTOMER
            + " text);";

    private static final String DATABASE_CREATE_CUST = "create table "
            + FeedEntryCustomer.TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + FeedEntryCustomer.COLUMN_NAME_CUST_NAME
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_BUILDING
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_UNIT
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_DATE
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_TIME
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_EMAIL
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_PHONE_NUMBER
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_PAYMENT_METHOD
            + " text, " + FeedEntryCustomer.COLUMN_NAME_CUST_ACTIVE
            + " integer, " + FeedEntryCustomer.COLUMN_NAME_CUST_HARDDWARE_INSTALLED
            + " integer, " + FeedEntryCustomer.COLUMN_NAME_CUST_HARDWARE_RETURNED
            + " integer, " + FeedEntryCustomer.COLUMN_NAME_CUST_NOTES
            + " text);";

    private static final String DATABASE_CREATE_CUST_HARDWARE_INFORMATION = "create table "
            + FeedEntryCustomerHardware.TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID
            + " integer, " + FeedEntryCustomerHardware.COLUMN_NAME_CUST_SERIAL
            + " text, " + FeedEntryCustomerHardware.COLUMN_NAME_CUST_SECPIN
            + " text, " + FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC1
            + " text, " + FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC2
            + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_LOGS);
        database.execSQL(DATABASE_CREATE_CUST);
        database.execSQL(DATABASE_CREATE_CUST_HARDWARE_INFORMATION);
    }

    public void destroyDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void addLog(com.developer.arnold.whisperfibertheapp.Log logToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_NAME, logToAdd.logName);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_DATE, logToAdd.logDate);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_TIME, logToAdd.logTime);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_BUILDING, logToAdd.logBuilding);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_UNIT, logToAdd.logUnit);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_TYPEOFSERVICE, logToAdd.logTypeOfService);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_NOTES, logToAdd.logNotes);
        values.put(FeedEntryLogs.COLUMN_NAME_CUSTOMER, logToAdd.logCustomer);

        // Inserting Row
        db.insert(FeedEntryLogs.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void addCustomer(Customer customerToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_NAME, customerToAdd.custName);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_BUILDING, customerToAdd.custBuilding);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_UNIT, customerToAdd.custUnit);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_DATE, customerToAdd.custDate);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_TIME, customerToAdd.custTime);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_EMAIL, customerToAdd.custEmail);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_PHONE_NUMBER, customerToAdd.custPhoneNumber);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_PAYMENT_METHOD, customerToAdd.custPaymentMethod);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_ACTIVE, customerToAdd.custActiveInActive);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_HARDDWARE_INSTALLED, customerToAdd.custHardwareInstalled);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_HARDWARE_RETURNED, customerToAdd.custHardwareReturned);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_NOTES, customerToAdd.custNotes);

        // Inserting Row
        long insert = db.insert(FeedEntryCustomer.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void addHardwareInformation(HardwareInformation hardwareInformationToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID, hardwareInformationToAdd.custId);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_SERIAL, hardwareInformationToAdd.serial);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_SECPIN, hardwareInformationToAdd.secPin);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC1, hardwareInformationToAdd.MAC1);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC2, hardwareInformationToAdd.MAC2);

        // Inserting Row
        long insert = db.insert(FeedEntryCustomerHardware.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public  List<com.developer.arnold.whisperfibertheapp.Log> getAllLogs() {
        List<com.developer.arnold.whisperfibertheapp.Log> listOfLogs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + FeedEntryLogs.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

          // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
           do {
               com.developer.arnold.whisperfibertheapp.Log tempLog = new com.developer.arnold.whisperfibertheapp.Log();
               tempLog.logID = cursor.getInt(0);
               tempLog.logName = cursor.getString(1);
               tempLog.logDate = cursor.getString(2);
               tempLog.logTime = cursor.getString(3);
               tempLog.logBuilding = cursor.getString(4);
               tempLog.logUnit = cursor.getString(5);
               tempLog.logTypeOfService = cursor.getInt(6);
               tempLog.logNotes = cursor.getString(7);
               tempLog.logCustomer = cursor.getInt(8);

               listOfLogs.add(tempLog);
            } while (cursor.moveToNext());
        }

        // return log list
        return listOfLogs;
    }

    public  List<Customer> getAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<Customer>();
        String selectQuery = "SELECT * FROM " + FeedEntryCustomer.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer newCust = new Customer();
                newCust.custID = cursor.getInt(0);
                newCust.custName = cursor.getString(1);
                newCust.custBuilding = cursor.getString(2);
                newCust.custUnit = cursor.getString(3);
                newCust.custDate = cursor.getString(4);
                newCust.custTime = cursor.getString(5);
                newCust.custEmail = cursor.getString(6);
                newCust.custPhoneNumber = cursor.getString(7);
                newCust.custPaymentMethod = cursor.getString(8);
                newCust.custActiveInActive = cursor.getInt(9);
                newCust.custHardwareInstalled = cursor.getInt(10);
                newCust.custHardwareReturned = cursor.getInt(11);
                newCust.custNotes = cursor.getString(12);
                listOfCustomers.add(newCust);
            } while (cursor.moveToNext());
        }

        // return log list
        return listOfCustomers;
    }

    public com.developer.arnold.whisperfibertheapp.Log getLog(int logID) {
        SQLiteDatabase db = this.getReadableDatabase();

        com.developer.arnold.whisperfibertheapp.Log retrievedLog;

        Cursor cursor = db.query(FeedEntryLogs.TABLE_NAME, new String[] { COLUMN_ID,
                        FeedEntryLogs.COLUMN_NAME_LOG_NAME, FeedEntryLogs.COLUMN_NAME_LOG_DATE, FeedEntryLogs.COLUMN_NAME_LOG_TIME,
                        FeedEntryLogs.COLUMN_NAME_LOG_BUILDING, FeedEntryLogs.COLUMN_NAME_LOG_UNIT,
                        FeedEntryLogs.COLUMN_NAME_LOG_TYPEOFSERVICE, FeedEntryLogs.COLUMN_NAME_LOG_NOTES, FeedEntryLogs.COLUMN_NAME_CUSTOMER}, COLUMN_ID + "=?",
                new String[] { String.valueOf(logID) }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            retrievedLog = new com.developer.arnold.whisperfibertheapp.Log(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8));

            return retrievedLog;
        }

        cursor.close();

        return new com.developer.arnold.whisperfibertheapp.Log();
    }

    public Customer getCustomer(int customerID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Customer retrievedCustomer;

        Cursor cursor = db.query(FeedEntryCustomer.TABLE_NAME, new String[] { COLUMN_ID,
                        FeedEntryCustomer.COLUMN_NAME_CUST_NAME, FeedEntryCustomer.COLUMN_NAME_CUST_BUILDING, FeedEntryCustomer.COLUMN_NAME_CUST_UNIT,
                        FeedEntryCustomer.COLUMN_NAME_CUST_DATE, FeedEntryCustomer.COLUMN_NAME_CUST_TIME, FeedEntryCustomer.COLUMN_NAME_CUST_EMAIL,
                        FeedEntryCustomer.COLUMN_NAME_CUST_PHONE_NUMBER, FeedEntryCustomer.COLUMN_NAME_CUST_PAYMENT_METHOD, FeedEntryCustomer.COLUMN_NAME_CUST_ACTIVE,
                        FeedEntryCustomer.COLUMN_NAME_CUST_HARDDWARE_INSTALLED, FeedEntryCustomer.COLUMN_NAME_CUST_HARDWARE_RETURNED, FeedEntryCustomer.COLUMN_NAME_CUST_NOTES
                }, COLUMN_ID + "=?",
                new String[] { String.valueOf(customerID) }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            retrievedCustomer = new Customer(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getString(12));

            return retrievedCustomer;
        }

        cursor.close();

        return new Customer();
    }

    public HardwareInformation getHardwareInformation(int customerID) {
        SQLiteDatabase db = this.getReadableDatabase();

        HardwareInformation retrievedHardwareInformation;

        Cursor cursor = db.query(FeedEntryCustomerHardware.TABLE_NAME, new String[] { COLUMN_ID,
                        FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID, FeedEntryCustomerHardware.COLUMN_NAME_CUST_SERIAL, FeedEntryCustomerHardware.COLUMN_NAME_CUST_SECPIN,
                        FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC1, FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC2
                }, FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID + "=?",
                new String[] { String.valueOf(customerID) }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            retrievedHardwareInformation = new HardwareInformation(cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(1));

            return retrievedHardwareInformation;
        }

        cursor.close();

        return new HardwareInformation();
    }

    public int updateLog(com.developer.arnold.whisperfibertheapp.Log logToUpdate, int logID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_NAME, logToUpdate.logName);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_DATE, logToUpdate.logDate);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_TIME, logToUpdate.logTime);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_BUILDING, logToUpdate.logBuilding);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_UNIT, logToUpdate.logUnit);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_TYPEOFSERVICE, logToUpdate.logTypeOfService);
        values.put(FeedEntryLogs.COLUMN_NAME_LOG_NOTES, logToUpdate.logNotes);
        values.put(FeedEntryLogs.COLUMN_NAME_CUSTOMER, logToUpdate.logCustomer);

        // updating row
        return db.update(FeedEntryLogs.TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(logID)});
    }

    public int updateCustomer(Customer customerToUpdate, int customerID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_NAME, customerToUpdate.custName);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_BUILDING, customerToUpdate.custBuilding);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_UNIT, customerToUpdate.custUnit);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_DATE, customerToUpdate.custDate);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_TIME, customerToUpdate.custTime);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_EMAIL, customerToUpdate.custEmail);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_PHONE_NUMBER, customerToUpdate.custPhoneNumber);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_PAYMENT_METHOD, customerToUpdate.custPaymentMethod);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_ACTIVE, customerToUpdate.custActiveInActive);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_HARDDWARE_INSTALLED, customerToUpdate.custHardwareInstalled);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_HARDWARE_RETURNED, customerToUpdate.custHardwareReturned);
        values.put(FeedEntryCustomer.COLUMN_NAME_CUST_NOTES, customerToUpdate.custNotes);

        // updating row
        return db.update(FeedEntryCustomer.TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(customerID)});
    }

    public int updateHardwareInformation(HardwareInformation hardwareInformationToUpdate, int customerID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID, hardwareInformationToUpdate.custId);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_SERIAL, hardwareInformationToUpdate.serial);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_SECPIN, hardwareInformationToUpdate.secPin);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC1, hardwareInformationToUpdate.MAC1);
        values.put(FeedEntryCustomerHardware.COLUMN_NAME_CUST_MAC2, hardwareInformationToUpdate.MAC2);

        // updating row
        return db.update(FeedEntryCustomerHardware.TABLE_NAME, values, FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID + " = ?",
                new String[] { String.valueOf(customerID)});
    }

    public void deleteLog(int logID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedEntryLogs.TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(logID) });
        db.close();
    };

    public void deleteCustomer(int customerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedEntryCustomer.TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(customerID) });
        db.close();
    };

    public void deleteHardwareInformation(int customerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedEntryCustomerHardware.TABLE_NAME, FeedEntryCustomerHardware.COLUMN_NAME_CUST_ID + " = ?",
                new String[] { String.valueOf(customerID) });
        db.close();
    };

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntryLogs.TABLE_NAME);
        onCreate(db);
    }

    public void createDatabaseFromScratch(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_LOGS);
        db.execSQL(DATABASE_CREATE_CUST);
        db.execSQL(DATABASE_CREATE_CUST_HARDWARE_INFORMATION);
    }
}
