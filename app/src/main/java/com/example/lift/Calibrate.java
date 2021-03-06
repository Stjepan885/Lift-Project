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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Calibrate extends AppCompatActivity {

    private Button start;
    private Button save;
    private Button reset;
    private Button startTimeCalibration;
    private Button saveTime;
    private Button resetTime;

    private TextView maxAccText;
    private TextView minAccText;
    private TextView oldTimeTwoFloorsUp;
    private TextView oldTimeTwoFloorsDown;
    private TextView newTimeTwoFloorsUp;
    private TextView newTimeTwoFloorsDown;
    private TextView activeAcc;
    private TextView activeTime;

    private Switch timeSwitch;

    private Accelerometer accelerometer;

    private int counter = 0;

    //acceleration
    private float maxAcceleration = 0;
    private float minAcceleration = 0;
    private boolean onAcc = false;
    private  boolean accSet = false;

    //time
    private long oldTimeUp;
    private long oldTimeDown;
    private boolean timeSet = false;
    private boolean onTime = false;
    private boolean switchTime = false;

    private float sum = 0;

    private boolean onMove = false;
    private int upDown = 0; // up = 1, down = 2, stationary = 0
    private int upDownPrevious = 0;
    private boolean floorChange = false;

    private long floorStartTime;
    private long timeBetweenFloors;
    private long upTimeNew;
    private long downTimeNew;

    private float averMaxAmp;
    private float averMinAmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        setContentView(R.layout.activity_calibrate);

        start = (Button) findViewById(R.id.startButton);
        save = (Button) findViewById(R.id.saveButton);
        reset = findViewById(R.id.buttonReset);
        maxAccText = (TextView) findViewById(R.id.maxAccText);
        minAccText = (TextView) findViewById(R.id.minAccText);


        startTimeCalibration = findViewById(R.id.buttonStartTimeCalibration);
        saveTime = findViewById(R.id.saveTimeButton);
        resetTime = findViewById(R.id.buttonTimeReset);
        oldTimeTwoFloorsUp = findViewById(R.id.oldTimeTwoFloorsUp);
        oldTimeTwoFloorsDown = findViewById(R.id.oldTimeTwoFloorsDown);
        newTimeTwoFloorsUp = findViewById(R.id.newTimeTwoFloorsUp);
        newTimeTwoFloorsDown = findViewById(R.id.newTimeTwoFloorsDown);
        activeAcc = findViewById(R.id.textActiveAcc);
        activeTime = findViewById(R.id.textActiveTime);
        timeSwitch = findViewById(R.id.switch1);

        timeSetOff();
        getLiftAcceleration();
        getLiftTimeBetweenTwoFloors();

        accelerometer = new Accelerometer(this);

        onPause();

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tz) {
                if (onAcc) {
                    calibrateAcceleration(tz);
                }else if(onTime){
                    calibrateTime(tz);
                }
            }
        });

        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean x = timeSwitch.isChecked();
                if (x == false){
                    timeSetOff();
                    timeSet = false;
                    switchSave(false);
                }else if (x==true){
                    timeSet();
                    timeSet = true;
                    switchSave(true);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                timeSetOff();
                onAcc = true;
                minAcceleration = 0;
                maxAcceleration = 0;
                activeAcc.setText("Active");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcc = false;
                activeAcc.setText("Not active");
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (maxAcceleration > 0 && minAcceleration < 0){
                        editor.putFloat("MAX_ACC_KEY", maxAcceleration);
                        editor.putFloat("MIN_ACC_KEY", minAcceleration);
                        editor.commit();
                        Toast.makeText(Calibrate.this, "Acceleration information saved", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Calibrate.this, "No acceleration information", Toast.LENGTH_SHORT).show();
                    }

                    counter = 10;
                    onPause();
                    timeSet();
                    timeSet = true;

                }catch (Exception e){
                    Toast.makeText(Calibrate.this, "No acceleration information", Toast.LENGTH_SHORT).show();
                    timeSetOff();
                }


            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcc = false;
                timeSet = false;
                activeAcc.setText("Not Active");
                timeSetOff();
                onPause();
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putFloat("MAX_ACC_KEY", 0);
                    editor.putFloat("MIN_ACC_KEY", 0);
                    editor.commit();
                    Toast.makeText(Calibrate.this, "Acceleration data reset", Toast.LENGTH_SHORT).show();

                    maxAccText.setText(0+"");
                    minAccText.setText(0+"");
                    maxAcceleration = 0;
                    minAcceleration = 0;

                }catch (Exception e){
                    Toast.makeText(Calibrate.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startTimeCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTime.setText("Active");
                onResume();
                onTime = true;
                floorStartTime = System.currentTimeMillis();
                averMaxAmp = maxAcceleration * 0.8f;
                averMinAmp = minAcceleration * 0.8f;
                upDown = 0;
                sum = 0;
            }
        });

        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTime.setText("Not active");
                onTime = false;
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("TIME_TWO_FLOORS_UP_KEY", upTimeNew);
                    editor.putLong("TIME_TWO_FLOORS_DOWN_KEY", downTimeNew);
                    editor.commit();
                    Toast.makeText(Calibrate.this, "Time information saved", Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    Toast.makeText(Calibrate.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTime.setText("Not active");
                onTime = false;
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong("TIME_TWO_FLOORS_UP_KEY", 0);
                    editor.putLong("TIME_TWO_FLOORS_DOWN_KEY", 0);
                    editor.commit();
                    Toast.makeText(Calibrate.this, "Time information reset", Toast.LENGTH_SHORT).show();
                    newTimeTwoFloorsUp.setText(0+"");
                    newTimeTwoFloorsDown.setText(0+"");
                }catch (Exception e){
                    Toast.makeText(Calibrate.this, "Error", Toast.LENGTH_SHORT).show();
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

            if (maxAcceleration > 0 && minAcceleration < 0){
                onAcc = false;
            }
            Toast toast = Toast.makeText(Calibrate.this, "Acceleration value set", Toast.LENGTH_LONG);
            toast.show();

        }catch (Exception e){
            Toast toast = Toast.makeText(Calibrate.this, "No information", Toast.LENGTH_LONG);
            toast.show();
            timeSetOff();
        }
    }

    private void getLiftTimeBetweenTwoFloors() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            long timeUp = sharedPreferences.getLong("TIME_TWO_FLOORS_UP_KEY" , 0);
            long timeDown = sharedPreferences.getLong("TIME_TWO_FLOORS_DOWN_KEY" , 0);

            if (timeUp != 0){
                oldTimeTwoFloorsUp.setText(""+timeUp);
                oldTimeUp = timeUp;
            }
            if (timeDown != 0){
                oldTimeTwoFloorsDown.setText(""+timeDown);
                oldTimeDown = timeDown;
            }


        }catch (Exception e){
            Toast toast = Toast.makeText(Calibrate.this, "No saved data", Toast.LENGTH_LONG);
            toast.show();

        }
    }

    private void switchSave(boolean b) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("SWITCH_TIME_KEY", b);
            editor.commit();
            Toast.makeText(Calibrate.this, "Switch saved", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(Calibrate.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void calibrateAcceleration(float tz) {
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

    private void calibrateTime(float tz) {
        if (counter < 10){
            tz = 0;
            counter++;
        }
        sum += tz;


        if (upDown == 4 && sum < 0.01 && sum > -0.01){
            upDown = 0;
        }


        if (onMove == false && upDown == 0){
            //trip started +
            if (sum > averMaxAmp ){
                onMove = true;
                upDown = 1;
                floorStartTime = System.currentTimeMillis();
            }
            if (sum < averMinAmp){
                onMove = true;
                upDown = 2;
                floorStartTime = System.currentTimeMillis();
            }
        }else if (onMove == true){
            if (sum > averMaxAmp && upDown == 2){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = System.currentTimeMillis() - floorStartTime;
                floorChange = true;
            }
            if (sum < averMinAmp && upDown == 1){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = System.currentTimeMillis() - floorStartTime;
                floorChange = true;
            }
        }

        if (floorChange == true && upDownPrevious == 1){
            floorChange = false;
            upTimeNew = timeBetweenFloors;
            newTimeTwoFloorsUp.setText(upTimeNew+"");
        }else if (floorChange == true && upDownPrevious == 2){
            floorChange = false;
            downTimeNew = timeBetweenFloors;
            newTimeTwoFloorsDown.setText(downTimeNew+"");
        }

    }

    private void timeSet() {
        if (timeSet ==false){
            return;
        }
        startTimeCalibration.getBackground().setAlpha(255);
        saveTime.getBackground().setAlpha(255);
        resetTime.getBackground().setAlpha(255);
        startTimeCalibration.setClickable(true);
        saveTime.setClickable(true);
        resetTime.setClickable(true);
        getLiftTimeBetweenTwoFloors();
    }

    private void timeSetOff(){
        startTimeCalibration.getBackground().setAlpha(100);
        saveTime.getBackground().setAlpha(100);
        resetTime.getBackground().setAlpha(100);
        startTimeCalibration.setClickable(false);
        saveTime.setClickable(false);
        resetTime.setClickable(false);
        onTime = false;
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