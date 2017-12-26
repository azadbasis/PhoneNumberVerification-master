package com.sadi.smsdirection.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sadi.smsdirection.Operations;
import com.sadi.smsdirection.R;

public class Activity_SignIn extends AppCompatActivity {

    private EditText etPhoneNumber, etPassword;
    private TextView tvUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById();
    }

    private void findViewById() {
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvUserType = (TextView) findViewById(R.id.tvUserType);
        tvUserType.setText(Operations.getStringFromSharedPreference(getApplicationContext(),"radiovalue"));

    }

    public void goSignIn(View view) {
        String phoneNumber = etPhoneNumber.getText().toString();
        String password = etPassword.getText().toString();


        if (phoneNumber.equalsIgnoreCase(Operations.getStringFromSharedPreference(getApplicationContext(), "ePhoneNumber")) &&
                password.equalsIgnoreCase(Operations.getStringFromSharedPreference(getApplicationContext(), "password"))

                ) {
            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show();
        }
    }
}
