package me.azhar.autoride.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.azhar.autoride.Utility.Operation;
import me.azhar.autoride.R;

public class Activity_Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__welcome);
        TextView tvWelcome=(TextView)findViewById(R.id.tvWelcome);
        tvWelcome.setText("Hello "+ Operation.getStringFromSharedPreference(getApplicationContext(),"radiovalue"));
    }
}
