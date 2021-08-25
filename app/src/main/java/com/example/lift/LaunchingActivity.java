package com.example.lift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LaunchingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        Button startButton = findViewById(R.id.startButtonL);
        Button preferencesButton = findViewById(R.id.preferenceButtonL);
        Button calibrateButton = findViewById(R.id.calibrateButtonL);
        Button exitButton = findViewById(R.id.exitButtonL);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    int nF = sharedPreferences.getInt("NUMBER_OF_FLOORS_KEY" , 0);
                    int sF = sharedPreferences.getInt("START_FLOOR_KEY" , 0);

                    if (nF != 0 && sF != 0){
                        Intent preferenceIntent = new Intent(LaunchingActivity.this, MainActivity.class);
                        LaunchingActivity.this.startActivity(preferenceIntent);
                    }
                }catch (Exception e){
                    Toast toast = Toast.makeText(LaunchingActivity.this, "No saved data", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferenceIntent = new Intent(LaunchingActivity.this, Settings.class);
                LaunchingActivity.this.startActivity(preferenceIntent);
            }
        });

        calibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferenceIntent = new Intent(LaunchingActivity.this, Calibrate.class);
                LaunchingActivity.this.startActivity(preferenceIntent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}