package com.example.lift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Calibrate extends AppCompatActivity {

    private Button start;
    private Button save;
    private TextView maxAccText;
    private TextView minAccText;

    private Accelerometer accelerometer;

    private float maxAcceleration = 0;
    private float minAcceleration = 999;
    private int counter = 10;
    private boolean on = false;
    private float sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        start = (Button) findViewById(R.id.startButton);
        save = (Button) findViewById(R.id.saveButton);
        maxAccText = (TextView) findViewById(R.id.maxAccText);
        minAccText = (TextView) findViewById(R.id.minAccText);

        getLiftAcceleration();

        accelerometer = new Accelerometer(this);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tz) {
                if (on) {
                    if (counter == 0) {
                        sum += tz;
                        if (maxAcceleration < sum) {
                            maxAcceleration = sum;
                            maxAccText.setText(sum+"");
                        }
                        if (minAcceleration > sum) {
                            minAcceleration = sum;
                            minAccText.setText(sum+"");
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
                on = false;

                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (maxAcceleration > 0 && minAcceleration < 999){
                        editor.putFloat("MAX_ACC_KEY", maxAcceleration);
                        editor.putFloat("MIN_ACC_KEY", minAcceleration);
                        editor.commit();
                        Toast.makeText(Calibrate.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Calibrate.this, "No data", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(Calibrate.this, "No data", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void getLiftAcceleration() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            float max = sharedPreferences.getFloat("MAX_ACC_KEY" , 0);
            float min = sharedPreferences.getFloat("MIN_ACC_KEY" , 0);
            maxAccText.setText(max+"");
            minAccText.setText(min+"");
            maxAcceleration = max;
            minAcceleration = min;

        }catch (Exception e){
            Toast toast = Toast.makeText(Calibrate.this, "No saved data", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometer.unregister();
    }
}