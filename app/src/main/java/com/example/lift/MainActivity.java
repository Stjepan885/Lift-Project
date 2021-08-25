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

    private long overallTime;

    TextView accelerationTextActivity;
    TextView speedTextActivity;
    TextView trackingStatusTextActivity;
    TextView currentFloorTextActivity;
    TextView upDownTextActivity;

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "fales", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
*/
        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        movement = new Movement();

        //variables and layout elements initialization
        accelerationTextActivity = (TextView) findViewById(R.id.accelerationText);
        speedTextActivity = (TextView) findViewById(R.id.speedText);
        trackingStatusTextActivity = (TextView) findViewById(R.id.trackingStatus);
        currentFloorTextActivity = (TextView) findViewById(R.id.timeText);
        upDownTextActivity = (TextView) findViewById(R.id.upDownText);

        Button chartButton = findViewById(R.id.buttonChart);
        //Button saveButton = findViewById(R.id.saveButton);
        Button resetButton = findViewById(R.id.buttonReset);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        getLiftInformation();
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
        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                String numbers = new String();
                ArrayList<Float> values = movment.getValues();
                for (int i = 0; i < values.size(); i++){
                    String n =  String.format("%.3f" , values.get(i));
                    numbers += String.format("%.3f", values.get(i))   + "\n";
                }

                Log.i("dsa", ""+ Environment.getExternalStorageDirectory().toString());
                String fileName = nazivFile.getText() + ".txt";
                Log.i("fd", "" + fileName);
                File file = new File(Environment.getExternalStorageDirectory().toString()+"/"+fileName);
                try {

                    file.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(numbers.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "fileNot Found", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                onPause();
                accelerometer.on = false;
                movement.resetAll();
                accelerationTextActivity.setText(""+0);
                trackingStatusTextActivity.setText("Not Active");
                currentFloorTextActivity.setText("" + startFloor);
                speedTextActivity.setText(""+0);
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
                Toast.makeText(MainActivity.this, "Tracking stoped" , Toast.LENGTH_LONG).show();
                saveCurrentStatus();
                trackingStatusTextActivity.setText("Not Active");
            }
        });
    }

    private void saveCurrentStatus() {
        endFloor = movement.getCurrentFloor();
        overallTime = movement.getOverallTime();
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

        }catch (Exception e){
            Toast toast = Toast.makeText(MainActivity.this, "No saved data", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void checkIfSet() {
        if (nbOfFloors > 1 && startFloor != 999 && maxAcceleration > 0 && minAcceleration < 999){
            set = true;
            Toast.makeText(this, "Ready to start tracking", Toast.LENGTH_LONG).show();
            movement.setNbOfFloors(nbOfFloors);
            movement.setStartFloor(startFloor);
            movement.setMaxAmp(maxAcceleration);
            movement.setMinAmp(minAcceleration);
            movement.initializeArray();
        }
    }

    private void updateVariables() {
        speedTextActivity.setText(movement.getSpeed()+"");
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