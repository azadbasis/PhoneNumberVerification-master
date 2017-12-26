package com.sadi.smsdirection.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sadi.smsdirection.Operations;
import com.sadi.smsdirection.R;
import com.sadi.smsdirection.emergency.SmsReceiver;

public class Activity_Register extends AppCompatActivity {

    EditText phoneNumberEt, etPassword;
    RadioGroup radioGrpUserType;
    Spinner sensibilitySp;
    Switch emergencyCallSw;
    int switchStatus;
    String ePhoneNumber, password;
    String sensibility;
    int indexOfSp;
    int shakeValue;
    View view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int random;
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

        sharedPreferencesData();
        switchStatus();
        switchClick();
        sensibilityTypeSpinner();

    }


    private void sensibilityTypeSpinner() {


        if (switchStatus == 1) {
            sensibilitySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sensibility = (String) parent.getItemAtPosition(position);
                    saveData();
                    sharedPreferencesData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void sharedPreferencesData() {

        sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        switchStatus = sharedPreferences.getInt("switchStatuss", 0);
        ePhoneNumber = sharedPreferences.getString("ePhoneNumber", "");
        sensibility = sharedPreferences.getString("sensibilitys", "Medium");


        if (sensibility.equals("Very High")) {
            indexOfSp = 0;
            shakeValue = 8;
        } else if (sensibility.equals("High")) {
            indexOfSp = 1;
            shakeValue = 11;
        } else if (sensibility.equals("Medium")) {
            indexOfSp = 2;
            shakeValue = 14;
        } else if (sensibility.equals("Low")) {
            indexOfSp = 3;
            shakeValue = 17;
        } else if (sensibility.equals("Very Low")) {
            indexOfSp = 4;
            shakeValue = 20;
        }

        editor.putInt("shakeValue", shakeValue);
        editor.commit();
    }

    private void saveData() {
        editor.putInt("switchStatuss", switchStatus);
        Operations.SaveToSharedPreference(getApplicationContext(), "ePhoneNumbers", ePhoneNumber);
        Operations.SaveToSharedPreference(getApplicationContext(), "sensibilitys", sensibility);
        phoneNumberEt.setText(ePhoneNumber);
    }

    private void switchClick() {

        emergencyCallSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    phoneNumberEt.setEnabled(true);
                    sensibilitySp.setEnabled(true);
                    switchStatus = 1;
                    phoneNumberEt.setText(ePhoneNumber);
                    sensibilitySp.setSelection(indexOfSp);

                    Operations.IntSaveToSharedPreference(getApplicationContext(), "switchStatuss", switchStatus);
                    Operations.SaveToSharedPreference(getApplicationContext(), "sensibilitys", sensibility);

                    Toast toast = Toast.makeText(Activity_Register.this, " Phone Verification Started ", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundColor(Color.GREEN);
                    v.setTextColor(Color.BLACK);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);

                    toast.show();


                } else {
                    phoneNumberEt.setEnabled(false);
                    sensibilitySp.setEnabled(false);
                    switchStatus = 0;

                    Operations.IntSaveToSharedPreference(getApplicationContext(), "switchStatuss", switchStatus);
                    Operations.SaveToSharedPreference(getApplicationContext(), "sensibilitys", sensibility);
                    Toast toast = Toast.makeText(Activity_Register.this, " Phone Verification Stopped ", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundColor(Color.RED);
                    v.setTextColor(Color.WHITE);
                    view = toast.getView();
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();

                }

            }
        });

    }

    private void switchStatus() {

        if (switchStatus == 0) {
            emergencyCallSw.setChecked(false);
            phoneNumberEt.setEnabled(false);
            sensibilitySp.setEnabled(false);


        } else if (switchStatus == 1) {
            emergencyCallSw.setChecked(true);
            phoneNumberEt.setEnabled(true);
            sensibilitySp.setEnabled(true);
            phoneNumberEt.setText(ePhoneNumber);
            sensibilitySp.setSelection(indexOfSp);

        }
    }

    private void initialize() {
        phoneNumberEt = (EditText) findViewById(R.id.ePhoneNumberEts);
        etPassword = (EditText) findViewById(R.id.etPassword);
        radioGrpUserType = (RadioGroup) findViewById(R.id.radioGrpUserType);
        emergencyCallSw = (Switch) findViewById(R.id.emergencyCallSws);
        sensibilitySp = (Spinner) findViewById(R.id.sensibilitySps);

        phoneNumberEt.setInputType(0);
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
                        sensibility = sensibilitySp.getSelectedItem().toString();
                        random = (int) (Math.random() * 1000 + 100);
                        String emergencyRandom = String.valueOf(random);
                        Operations.SaveToSharedPreference(getApplicationContext(), "emergencyRandom", emergencyRandom);

                        if (ePhoneNumber.length() > 0 && random > 0) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(ePhoneNumber, null, String.valueOf(random), null, null);
                            Operations.SaveToSharedPreference(getApplicationContext(), "ePhoneNumbers", ePhoneNumber);
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

    String selectedRadioButtonText="";
    public void goRegister(View view) {
        password = etPassword.getText().toString();
        radioGrpUserType = (RadioGroup) findViewById(R.id.radioGrpUserType);
      //  radiovalue = ((RadioButton) findViewById(radioGrpUserType.getCheckedRadioButtonId())).getText().toString();
        Operations.SaveToSharedPreference(getApplicationContext(), "password", password);
        int selectedRadioButtonID = radioGrpUserType.getCheckedRadioButtonId();

        // If nothing is selected from Radio Group, then it return -1
        if (selectedRadioButtonID != -1) {

            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            selectedRadioButtonText = selectedRadioButton.getText().toString();
            Operations.SaveToSharedPreference(getApplicationContext(), "radiovalue", selectedRadioButtonText);

           // tv_result.setText(selectedRadioButtonText + " selected.");
            Toast.makeText(this, selectedRadioButtonText + " selected.", Toast.LENGTH_SHORT).show();
        }
        else{
        //    tv_result.setText("Nothing selected from Radio Group.");
            Toast.makeText(this, "Nothing selected from User Type.", Toast.LENGTH_SHORT).show();
        }


        if (password.length() == 0) {
            etPassword.setError("Input password");
        }
       /* if(radiovalue.length()==0){
            Toast.makeText(this, "checked use!", Toast.LENGTH_SHORT).show();
        }*/
        if (ePhoneNumber.length() > 0 && password.length() > 0&&selectedRadioButtonText.length()>0)
            startActivity(new Intent(this, Activity_SignIn.class));
    }
}
