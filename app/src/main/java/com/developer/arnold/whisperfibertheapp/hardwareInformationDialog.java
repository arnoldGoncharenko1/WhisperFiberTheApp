package com.developer.arnold.whisperfibertheapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class HardwareInformationDialog extends DialogFragment implements Validator.ValidationListener {
    public interface SaveHardwareInformationListener {
        public void saveHardware(DialogFragment saveHardwareFragment, HardwareInformation newHardwareInfo, boolean hardwareInfoEntered);
    }

    SaveHardwareInformationListener saveHardwareListener;

    @NotEmpty
    EditText addHardwareSerial;

    @NotEmpty
    EditText addSecurityPin;

    @NotEmpty
    EditText addMAC1;

    @NotEmpty
    EditText addMAC2;

    Validator validator;
    boolean formValid;
    View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            saveHardwareListener = (SaveHardwareInformationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SaveHardwareInformationListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        rootView = inflater.inflate(R.layout.activity_hardware_information_add, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton("Save Hardware Information", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        addHardwareSerial = (EditText) rootView.findViewById(R.id.edtAddHardwareSerial);
        addSecurityPin = (EditText) rootView.findViewById(R.id.edtAddHardwareSecPin);
        addMAC1 = (EditText) rootView.findViewById(R.id.edtAddHardwareMAC1);
        addMAC2 = (EditText) rootView.findViewById(R.id.edtAddHardwareMAC2);

        addHardwareSerial.setText(getArguments().getString("serial"));
        addSecurityPin.setText(getArguments().getString("pin"));
        addMAC1.setText(getArguments().getString("MAC1"));
        addMAC2.setText(getArguments().getString("MAC2"));

        validator = new Validator(this);
        validator.setValidationListener(this);

        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    validator.validate();
                    if (formValid) {
                        HardwareInformation savedHardwareInfo = new HardwareInformation();

                        savedHardwareInfo.serial = addHardwareSerial.getText().toString();
                        savedHardwareInfo.secPin = addSecurityPin.getText().toString();
                        savedHardwareInfo.MAC1 = addMAC1.getText().toString();
                        savedHardwareInfo.MAC2 = addMAC2.getText().toString();

                        saveHardwareListener.saveHardware(HardwareInformationDialog.this, savedHardwareInfo, true);
                        wantToCloseDialog = true;
                    }

                    if(wantToCloseDialog)
                        dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
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
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
