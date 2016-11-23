package com.developer.arnold.whisperfibertheapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class LogAdd extends AppCompatActivity implements Validator.ValidationListener {

    Log Log;
    TextView timeChosen;
    TextView dateChosen;

    @NotEmpty
    EditText logName;

    @NotEmpty
    EditText logBuilding;

    @NotEmpty
    EditText logUnit;
    EditText logNotes;
    Spinner logCustomer;

    Spinner logService;
    Button removeLog;

    int ModeType;
    int LogID;
    boolean formValid;
    boolean dateEntered = false;
    boolean timeEntered = false;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_add);

        MySQLiteHelper db = new MySQLiteHelper(this);
        Intent intent = getIntent();
        ModeType = intent.getIntExtra("Mode", 0);

        timeChosen = (TextView) findViewById(R.id.lblTimeChosen);
        dateChosen = (TextView) findViewById(R.id.lblDateChosen);

        logName = (EditText) findViewById(R.id.edtAddLogName);
        logBuilding = (EditText) findViewById(R.id.edtAddLogBuilding);
        logUnit = (EditText) findViewById(R.id.edtAddLogUnit);
        logNotes = (EditText) findViewById(R.id.edtNotes);
        logCustomer = (Spinner) findViewById(R.id.spnCustomer);
        logService = (Spinner) findViewById(R.id.spnService);
        removeLog = (Button) findViewById(R.id.btnRemoveLog);

        List<String> listOfCustomerNames = new ArrayList<String>();

        List<Customer> listOfCustomers = db.getAllCustomers();

        listOfCustomerNames.add("<No Customer>");
        for (int i = 0; i < listOfCustomers.size(); i++) {
            listOfCustomerNames.add(listOfCustomers.get(i).custName);
        }

        ArrayAdapter<String> dataAdapterCust = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listOfCustomerNames);
        dataAdapterCust.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logCustomer.setAdapter(dataAdapterCust);

        List<String> serviceList = new ArrayList<String>();
        serviceList.add("<type of service>");
        serviceList.add("Install");
        serviceList.add("Uninstall");
        serviceList.add("Pickup");
        serviceList.add("Troubleshoot");
        serviceList.add("Call");
        ArrayAdapter<String> dataAdapterService = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, serviceList);
        dataAdapterService.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logService.setAdapter(dataAdapterService);

        if (ModeType == 0) {
            Log = new Log();
            removeLog.setVisibility(View.INVISIBLE);
        } else if (ModeType == 1) {
            LogID = intent.getIntExtra("ID", 0);
            Button saveButton = (Button) findViewById(R.id.btnSaveLog);
            saveButton.setText("Save");

            Log = db.getLog(LogID);

            timeChosen.setText(Log.logTime);
            dateChosen.setText(Log.logDate);
            logName.setText(Log.logName);
            logBuilding.setText(Log.logBuilding);
            logUnit.setText(Log.logUnit);
            logNotes.setText(Log.logNotes);

            Customer tempCustId = db.getCustomer(Log.logCustomer);
            if (tempCustId.custID == 0) {
                logCustomer.setSelection(0);
            } else {
                logCustomer.setSelection(Log.logCustomer);
            }

            logService.setSelection(Log.logTypeOfService);

            dateEntered = true;
            timeEntered = true;
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

        public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            boolean isPM = (hourOfDay >= 12);
            String newTime = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
            ((LogAdd) getActivity()).Log.logTime = newTime;
            ((LogAdd) getActivity()).timeChosen.setText(newTime);
            ((LogAdd) getActivity()).timeEntered = true;
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((LogAdd) getActivity()).Log.logDate = String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
            ((LogAdd) getActivity()).dateChosen.setText(String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
            ((LogAdd) getActivity()).dateEntered = true;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addLog(View v) {
        validator.validate();
        utils errorDialog = new utils();
        if (formValid && dateEntered && timeEntered && logService.getSelectedItemPosition() != 0) {
            Log.logName = logName.getText().toString();
            Log.logBuilding = logBuilding.getText().toString();
            Log.logUnit = logUnit.getText().toString();
            Log.logNotes = logNotes.getText().toString();
            Log.logCustomer = logCustomer.getSelectedItemPosition();
            Log.logTypeOfService = logService.getSelectedItemPosition();

            MySQLiteHelper db = new MySQLiteHelper(this);

            if (ModeType == 0) {
                db.addLog(Log);
            }
            else if (ModeType == 1) {
                db.updateLog(Log, LogID);
            }

            setResult(RESULT_OK);
            finish();
        }
        else if(!dateEntered || !timeEntered) {
            errorDialog.createErrorDialog("No time/Date chosen", "Please choose a date and/or time", this);
        }
        else if (logService.getSelectedItemPosition() == 0) {
            errorDialog.createErrorDialog("Incorrect service", "Please choose a service for this log!", this);
        }
    }

    public void removeLog(View v) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.deleteLog(LogID);

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onValidationSucceeded() {
        formValid = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        formValid = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
