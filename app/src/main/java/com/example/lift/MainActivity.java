package com.example.lift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Movement movement;
    private Accelerometer accelerometer;

    private int nbOfFloors = 0;
    private int startFloor = 999;
    private int endFloor;

    private float maxAcceleration;
    private float minAcceleration;

    private boolean set = false;
    private boolean startSet = true;
    private boolean active = false;

    private long timeUp;
    private long timeDown;
    private long overallTime;

    private boolean timeModeSwitch = false;

    TextView accelerationTextActivity;
    TextView trackingStatusTextActivity;
    TextView currentFloorTextActivity;
    TextView upDownTextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        movement = new Movement();

        //variables and layout elements initialization
        accelerationTextActivity = (TextView) findViewById(R.id.accelerationText);
        trackingStatusTextActivity = (TextView) findViewById(R.id.trackingStatus);
        currentFloorTextActivity = (TextView) findViewById(R.id.timeText);
        upDownTextActivity = (TextView) findViewById(R.id.upDownText);


        Button chartButton = findViewById(R.id.buttonChart);
        Button resetButton = findViewById(R.id.buttonReset);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        getLiftInformation();
        checkTimeSwitch();
        checkIfSet();

        //sensor listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tz) {
                if (set == true && startSet == true) {
                    accelerationTextActivity.setText("" + String.format("%.2f", tz));
                    movement.Prati(tz);
                    updateVariables();
                }
            }
        });

        //Buttons

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                if (set == false && startSet == false){
                    Toast.makeText(MainActivity.this, "Error" , Toast.LENGTH_LONG).show();
                }else if (startSet == false){
                    Toast.makeText(MainActivity.this, "Reset" , Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Tracking started" , Toast.LENGTH_LONG).show();
                    trackingStatusTextActivity.setText("Active");
                    movement.setZeroSec();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                movement.resetAll();
                accelerationTextActivity.setText(""+0);
                trackingStatusTextActivity.setText("Not Active");
                currentFloorTextActivity.setText("" + startFloor);
                upDownTextActivity.setText("Stationary");
                startSet = true;
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                accelerometer.on = false;
                startSet = false;
                Toast.makeText(MainActivity.this, "Tracking stopped" , Toast.LENGTH_LONG).show();
                saveCurrentStatus();
                trackingStatusTextActivity.setText("Not Active");
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {                                     //CHART button
            @Override
            public void onClick(View v) {

                if (active == false) {
                    Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                    intent.putExtra("values", movement.getAccValues());
                    intent.putExtra("sumValues", movement.getSumValues());
                    intent.putExtra("speedValues", movement.getSpeedValues());
                    startActivity(intent);
                    trackingStatusTextActivity.setText("Active");
                }
            }
        });
    }

    private void getLiftInformation() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            int nF = sharedPreferences.getInt("NUMBER_OF_FLOORS_KEY" , 0);
            int sF = sharedPreferences.getInt("START_FLOOR_KEY" , 0);
            nbOfFloors = nF;
            startFloor = sF;

            float max = sharedPreferences.getFloat("MAX_ACC_KEY" , 0);
            float min = sharedPreferences.getFloat("MIN_ACC_KEY" , 0);
            maxAcceleration = max;
            minAcceleration = min;

            long timeU = sharedPreferences.getLong("TIME_TWO_FLOORS_UP_KEY",0);
            long timeD = sharedPreferences.getLong("TIME_TWO_FLOORS_DOWN_KEY", 0);
            timeUp = timeU;
            timeDown = timeD;


        }catch (Exception e){
            Toast toast = Toast.makeText(MainActivity.this, "No lift information", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void checkTimeSwitch() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean bo = sharedPreferences.getBoolean("SWITCH_TIME_KEY", false);

            timeModeSwitch = bo;

        }catch (Exception e){
            Toast toast = Toast.makeText(MainActivity.this, "No lift information", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void checkIfSet() {
        if (nbOfFloors > 1 && startFloor != 999 && maxAcceleration > 0 && minAcceleration < 0 && timeUp != 0 && timeDown != 0){
            set = true;
            Toast.makeText(this, "Ready to start tracking", Toast.LENGTH_LONG).show();
            movement.setNbOfFloors(nbOfFloors);
            movement.setStartFloor(startFloor);
            movement.setMaxAmp(maxAcceleration);
            movement.setMinAmp(minAcceleration);
            movement.setTimeUp(timeUp);
            movement.setTimeDown(timeDown);
            movement.setSwitchModeTime(timeModeSwitch);
            movement.initializeArray();
        }
    }

    private void updateVariables() {
        currentFloorTextActivity.setText(movement.getCurrentFloor() + "");

        switch (movement.getUpDown()){
            case 0:
                upDownTextActivity.setText("Stationary");
                break;
            case 1:
                upDownTextActivity.setText("Going up");
                break;
            case 2:
                upDownTextActivity.setText("Going down");
                break;
            case 4:
                upDownTextActivity.setText("Braking");
                break;
        }
    }

    private void saveCurrentStatus() {
        endFloor = movement.getCurrentFloor();
        overallTime = movement.getOverallTime();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


}