package com.example.lift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Calibrate extends AppCompatActivity {

    private Button start;
    private Button save;

    private Accelerometer accelerometer;

    private float maxAcceleration = 0;
    private float minAcceleration = 999;
    private int counter = 10;
    private boolean on = false;

    public static final float MAX_ACC = 0;
    public static final float MIN_ACC = 999;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        start.findViewById(R.id.startButton);
        save.findViewById(R.id.saveButton);

        accelerometer = new Accelerometer(this);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if (on) {
                    if (counter == 0) {
                        if (maxAcceleration < tz) {
                            maxAcceleration = tz;
                        }
                        if (minAcceleration > tz) {
                            minAcceleration = tz;
                        }
                    } else {
                        counter--;
                    }
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putFloat(String.valueOf(MAX_ACC), maxAcceleration);
                editor.putFloat(String.valueOf(MIN_ACC), minAcceleration);

                editor.apply();

                Toast.makeText(Calibrate.this, "Data saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}