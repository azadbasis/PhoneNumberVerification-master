package me.azhar.autoride.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.azhar.autoride.BroadcastReceiver.SmsReceiver;
import me.azhar.autoride.R;
import me.azhar.autoride.Utility.Operation;

public class Activity_Register extends AppCompatActivity {

    EditText phoneNumberEt, etPassword;
    RadioGroup radioGrpUserType;
    Switch emergencyCallSw;
    int switchStatus;
    String verifiedPhoneNumber, ePhoneNumber, password;
    View view;
    int random;
    String selectedRadioButtonText = "";


    SmsReceiver receiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("AutoRide Register");
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        initialize();
      //  sharedPreferencesData();
        switchStatus();
        verifiedPhoneNumber = Operation.getStringFromSharedPreference(getApplicationContext(), "ePhoneNumber");
        switchStatus= Operation.getIntegerSharedPreference(getApplicationContext(),"switchStatuss",0);
        switchClick();
        sensibilityTypeSpinner();

    }

    private void sensibilityTypeSpinner() {

        if (switchStatus == 1) {
            Operation.IntSaveToSharedPreference(getApplicationContext(), "switchStatuss", switchStatus);
            phoneNumberEt.setText(verifiedPhoneNumber);
        }

    }

    private void switchClick() {

        emergencyCallSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    phoneNumberEt.setEnabled(true);
                    switchStatus = 1;
                    phoneNumberEt.setText(verifiedPhoneNumber);
                    Operation.IntSaveToSharedPreference(getApplicationContext(), "switchStatuss", switchStatus);
                    makeToast(" Phone Verification Started ");

                } else {
                    phoneNumberEt.setEnabled(false);
                    switchStatus = 0;
                    Operation.IntSaveToSharedPreference(getApplicationContext(), "switchStatuss", switchStatus);
                    makeToast(" Phone Verification Stopped ");


                }

            }
        });

    }

    private void makeToast(String message) {

        Toast toast = Toast.makeText(Activity_Register.this, message, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        toast.getView().setBackgroundColor(Color.TRANSPARENT);
        v.setTextColor(Color.WHITE);
        view = toast.getView();
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void switchStatus() {

        if (switchStatus == 0) {
            emergencyCallSw.setChecked(false);
            phoneNumberEt.setEnabled(false);

        } else if (switchStatus == 1) {
            emergencyCallSw.setChecked(true);
            phoneNumberEt.setEnabled(true);
            phoneNumberEt.setText(verifiedPhoneNumber);
        }
    }

    public void verifyUserPhone(View view) {
        callVerify();
    }

    public void callVerify() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_Register.this);
        alertDialog.setTitle("Autoride Number");
        alertDialog.setMessage("Set Autoride Number:");
        final EditText input = new EditText(Activity_Register.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_action_warning);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ePhoneNumber = input.getText().toString();
                        random = (int) (Math.random() * 1000 + 1000);
                        String emergencyRandom = String.valueOf(random);
                        Operation.SaveToSharedPreference(getApplicationContext(), "emergencyRandom", emergencyRandom);

                        if (ePhoneNumber.length() > 0 && random > 0) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(ePhoneNumber, null, String.valueOf(random), null, null);
                            Operation.SaveToSharedPreference(getApplicationContext(), "ePhoneNumbers", ePhoneNumber);
                            showMessage("Please Wait...!", "to Verify your PhoneNumber");
                            return;
                        } else {
                            Toast.makeText(Activity_Register.this, "Blank Phone Number!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void initialize() {
        phoneNumberEt = (EditText) findViewById(R.id.ePhoneNumberEts);
        etPassword = (EditText) findViewById(R.id.etPassword);
        radioGrpUserType = (RadioGroup) findViewById(R.id.radioGrpUserType);
        emergencyCallSw = (Switch) findViewById(R.id.emergencyCallSws);
        phoneNumberEt.setInputType(0);
    }

    public void goRegister(View view) {
        password = etPassword.getText().toString();
        radioGrpUserType = (RadioGroup) findViewById(R.id.radioGrpUserType);

        Operation.SaveToSharedPreference(getApplicationContext(), "password", password);
        int selectedRadioButtonID = radioGrpUserType.getCheckedRadioButtonId();

        if (selectedRadioButtonID != -1) {

            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            selectedRadioButtonText = selectedRadioButton.getText().toString();
            Operation.SaveToSharedPreference(getApplicationContext(), "radiovalue", selectedRadioButtonText);

            Toast.makeText(this, selectedRadioButtonText + " selected.", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Nothing selected from User Type.", Toast.LENGTH_SHORT).show();
        }


        if (password.length() == 0) {
            etPassword.setError("Input password");
        }

        if (verifiedPhoneNumber.length() > 0 && password.length() > 0 && selectedRadioButtonText.length() > 0) {
            startActivity(new Intent(this, Activity_SignIn.class));
            finish();
        } else {
            Toast.makeText(this, "use valid credential", Toast.LENGTH_SHORT).show();
        }
    }
}
