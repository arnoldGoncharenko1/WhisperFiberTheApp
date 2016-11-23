package com.developer.arnold.whisperfibertheapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class CustomerAdd extends AppCompatActivity implements Validator.ValidationListener, HardwareInformationDialog.SaveHardwareInformationListener {

    Customer cust;
    TextView timeChosen;
    TextView dateChosen;

    EditText custNotes;

    @NotEmpty
    EditText custName;

    @NotEmpty
    EditText custBuilding;

    @NotEmpty
    EditText custUnit;

    @NotEmpty
    @Email
    EditText custEmail;

    @NotEmpty
    EditText custPhone;

    @NotEmpty
    EditText custPaymentMethod;

    CheckBox custActiveInactive;
    CheckBox custHardwareReturned;
    CheckBox custHardwareInstalled;
    Button removeCustomer;

    int ModeType;
    int ModeTypeHardwareInfo;
    int CustomerID;
    boolean hardwareInformationEntered;
    boolean formValid;
    boolean dateEntered = false;
    boolean timeEntered = false;
    Validator validator;

    HardwareInformation custHardwareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        Intent intent = getIntent();
        ModeType = intent.getIntExtra("Mode", 0);
        CustomerID = intent.getIntExtra("ID", 0);
        ModeTypeHardwareInfo = intent.getIntExtra("Mode", 0);

        timeChosen = (TextView) findViewById(R.id.lblTimeChosen);
        dateChosen = (TextView) findViewById(R.id.lblDateChosen);

        custName = (EditText) findViewById(R.id.edtAddCustomerName);
        custBuilding = (EditText) findViewById(R.id.edtAddCustomerBuilding);
        custUnit = (EditText) findViewById(R.id.edtAddCustomerUnit);
        custNotes = (EditText) findViewById(R.id.edtNotes);
        custEmail = (EditText) findViewById(R.id.edtAddCustomerEmail);
        custPhone = (EditText) findViewById(R.id.edtAddCustomerPhoneNumber);
        custPaymentMethod = (EditText) findViewById(R.id.edtAddCustomerPaymentMethod);
        custActiveInactive = (CheckBox) findViewById(R.id.chActive);
        custHardwareReturned = (CheckBox) findViewById(R.id.chHardwareReturned);
        custHardwareInstalled = (CheckBox) findViewById(R.id.chHardwareInstalled);
        removeCustomer = (Button) findViewById(R.id.btnRemoveCustomer);

        custActiveInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(custActiveInactive.isChecked()){
                    custHardwareReturned.setChecked(false);
                    custHardwareReturned.setEnabled(false);
                    custHardwareInstalled.setEnabled(true);
                    cust.custActiveInActive = 1;
                }else{
                    custHardwareInstalled.setChecked(false);
                    custHardwareInstalled.setEnabled(false);
                    custHardwareReturned.setEnabled(true);
                    cust.custActiveInActive = 0;
                }
            }
    });

        if (ModeType == 0) {
            cust = new Customer();
            cust.custActiveInActive = 0;
            cust.custHardwareInstalled = 0;
            cust.custHardwareReturned = 1;
            removeCustomer.setVisibility(View.INVISIBLE);
            custHardwareReturned.setChecked(true);
            hardwareInformationEntered = false;
            custHardwareInstalled.setEnabled(false);
            custHardwareInfo = new HardwareInformation();
            custHardwareInfo.custId = String.valueOf(CustomerID);
        }
        else if (ModeType == 1) {
            Button saveButton = (Button) findViewById(R.id.btnSaveCustomer);
            saveButton.setText("Save");

            MySQLiteHelper db = new MySQLiteHelper(this);

            cust = db.getCustomer(CustomerID);

            timeChosen.setText(cust.custTime);
            dateChosen.setText(cust.custDate);
            custName.setText(cust.custName);
            custBuilding.setText(cust.custBuilding);
            custUnit.setText(cust.custUnit);
            custNotes.setText(cust.custNotes);
            custEmail.setText(cust.custEmail);
            custPhone.setText(cust.custPhoneNumber);
            custPaymentMethod.setText(cust.custPaymentMethod);

            if (cust.custActiveInActive == 1)
                custActiveInactive.setChecked(true);

            if (cust.custHardwareInstalled == 1) {
                custHardwareInstalled.setChecked(true);
            }
            else if (cust.custHardwareReturned == 1) {
                custHardwareReturned.setChecked(true);
            }

            hardwareInformationEntered = true;
            dateEntered = true;
            timeEntered = true;

            if(custActiveInactive.isChecked()){
                custHardwareReturned.setChecked(false);
                custHardwareReturned.setEnabled(false);
                custHardwareInstalled.setEnabled(true);
                cust.custActiveInActive = 1;
            }else{
                custHardwareInstalled.setChecked(false);
                custHardwareInstalled.setEnabled(false);
                custHardwareReturned.setEnabled(true);
                cust.custActiveInActive = 0;
            }

            custHardwareInfo = db.getHardwareInformation(CustomerID);
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        custPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(custPhone.getText().toString())) {
                    custPhone.setError("Incorrect phone number format (Should be XXX-XXX-XXXX)");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(custPhone.getText().toString())) {
                    custPhone.setError("Incorrect phone number format (Only numbers and - allowed)");
                }
            }
        });
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
            ((CustomerAdd) getActivity()).cust.custTime = newTime;
            ((CustomerAdd) getActivity()).timeChosen.setText(newTime);
            ((CustomerAdd) getActivity()).timeEntered = true;
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new CustomerAdd.TimePickerFragment();
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
            ((CustomerAdd) getActivity()).cust.custDate = String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
            ((CustomerAdd) getActivity()).dateChosen.setText(String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
            ((CustomerAdd) getActivity()).dateEntered = true;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new CustomerAdd.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addCustomer(View v) {
        validator.validate();
        utils errorDialog = new utils();
        if (formValid && hardwareInformationEntered && PhoneNumberUtils.isGlobalPhoneNumber(custPhone.getText().toString()) && dateEntered && timeEntered) {
            cust.custName = custName.getText().toString();
            cust.custBuilding = custBuilding.getText().toString();
            cust.custUnit = custUnit.getText().toString();
            cust.custNotes = custNotes.getText().toString();
            cust.custEmail = custEmail.getText().toString();
            cust.custPhoneNumber = custPhone.getText().toString();
            cust.custPaymentMethod = custPaymentMethod.getText().toString();

            cust.custHardwareInstalled = 0;
            cust.custHardwareReturned = 0;

            if (custHardwareReturned.isChecked()) {
                cust.custHardwareReturned = 1;
            }
            else if (custHardwareInstalled.isChecked()) {
                cust.custHardwareInstalled = 1;
            }

            MySQLiteHelper db = new MySQLiteHelper(this);

            if (ModeType == 0) {
                db.addCustomer(cust);
                db.addHardwareInformation(custHardwareInfo);
            }
            else if (ModeType == 1) {
                db.updateCustomer(cust, CustomerID);
                db.updateHardwareInformation(custHardwareInfo, CustomerID);
            }

            setResult(RESULT_OK);
            finish();
        }
        else if (!hardwareInformationEntered) {
            errorDialog.createErrorDialog("Hardware information not saved", "Please add hardware information to customer!", this);
        }
        else if (!PhoneNumberUtils.isGlobalPhoneNumber(custPhone.getText().toString())) {
            errorDialog.createErrorDialog("Incorrect phone number", "Please enter a proper phone number", this);
            custPhone.setError("Incorrect phone number format (Should be XXX-XXX-XXXX)");
        }
        else if (!dateEntered || !timeEntered) {
            errorDialog.createErrorDialog("No time/Date chosen", "Please choose a date and/or time", this);
        }
    }

    public void removeCustomer(View v) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.deleteCustomer(CustomerID);
        db.deleteHardwareInformation(CustomerID);

        setResult(RESULT_OK);
        finish();
    }

    public void goToHardwareInformation(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("serial", custHardwareInfo.serial);
        bundle.putString("pin", custHardwareInfo.secPin);
        bundle.putString("MAC1", custHardwareInfo.MAC1);
        bundle.putString("MAC2", custHardwareInfo.MAC2);
        DialogFragment hardwareInfoAdd = new HardwareInformationDialog();
        hardwareInfoAdd.setArguments(bundle);
        hardwareInfoAdd.show(getSupportFragmentManager(), "Hardware Information");
    }

    @Override
    public void saveHardware(DialogFragment dialog, HardwareInformation savedHardware, boolean hardwareInfoEntered) {
        custHardwareInfo.serial = savedHardware.serial;
        custHardwareInfo.secPin = savedHardware.secPin;
        custHardwareInfo.MAC1 = savedHardware.MAC1;
        custHardwareInfo.MAC2 = savedHardware.MAC2;

        hardwareInformationEntered = hardwareInfoEntered;
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
