package com.example.lift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchingActivity extends AppCompatActivity {

    private Button startButton;
    private Button preferencesButton;
    private Button settingsButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        startButton.findViewById(R.id.startButton);
        preferencesButton.findViewById(R.id.preferenceButton);
        settingsButton.findViewById(R.id.settings);
        exitButton.findViewById(R.id.exitButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent preferenceIntent = new Intent(LaunchingActivity.this, MainActivity.class);
                //LaunchingActivity.this.startActivity(preferenceIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}