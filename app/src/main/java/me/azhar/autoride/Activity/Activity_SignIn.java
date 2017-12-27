package me.azhar.autoride.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.azhar.autoride.Utility.Operation;
import me.azhar.autoride.R;

public class Activity_SignIn extends AppCompatActivity {

    private EditText etPhoneNumber, etPassword;
    private TextView tvUserType;
    private String verifiedPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById();
        verifiedPhoneNumber = Operation.getStringFromSharedPreference(getApplicationContext(), "ePhoneNumber");
        etPhoneNumber.setText(verifiedPhoneNumber);

    }

    private void findViewById() {
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvUserType = (TextView) findViewById(R.id.tvUserType);
        tvUserType.setText(Operation.getStringFromSharedPreference(getApplicationContext(), "radiovalue"));

    }

    public void goSignIn(View view) {

        String phoneNumber = etPhoneNumber.getText().toString();
        String password = etPassword.getText().toString();
        if (phoneNumber.equalsIgnoreCase(verifiedPhoneNumber) && password.equalsIgnoreCase(Operation.getStringFromSharedPreference(getApplicationContext(), "password"))) {
            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Activity_Welcome.class));
            finish();
        } else {
            Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show();
        }
    }
}
