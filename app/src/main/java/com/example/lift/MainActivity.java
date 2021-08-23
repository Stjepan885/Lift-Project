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

public class MainActivity extends AppCompatActivity {

    private Movement movement;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    private int nbOfFloors = 0;
    private int startFloor = 999;
    private boolean set = false;
    private boolean startSet = false;
    private boolean active = false;

    private String maxAccStr;
    private String minAccStr;
    private float maxAcc;
    private float minAcc;

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

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
*/

        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        movement = new Movement();

        //variables and layout elements initialization
        TextView text2 = findViewById(R.id.accelerationText);
        TextView speed = findViewById(R.id.speedText);
        TextView trackingStatus = findViewById(R.id.trackingStatus);
        TextView durationTime = findViewById(R.id.timeText);
        TextView upDown = findViewById(R.id.upDownText);

        Button chartButton = findViewById(R.id.buttonChart);
        //Button saveButton = findViewById(R.id.saveButton);
        Button resetButton = findViewById(R.id.buttonReset);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);


        //preference attributes
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String stringNbOfFloorsPref = sharedPref.getString(SettingsActivity.KEY_PREF_FLOOR_NUMBER, "enter");
        String stringStartFloorPref = sharedPref.getString(SettingsActivity.KEY_PREF_START_FLOOR, "enter");

        try {
            nbOfFloors = Integer.parseInt(stringNbOfFloorsPref);
            movement.setNbOfFloors(nbOfFloors);
        }catch (Exception e){
            nbOfFloors = 0;
            Toast.makeText(this, "Error, please set Number of floors again", Toast.LENGTH_LONG).show();
        }

        try {
            startFloor = Integer.parseInt(stringStartFloorPref);
            movement.setStartFloor(nbOfFloors);
        }catch (Exception e){
            startFloor = 0;
            Toast.makeText(this, "Error, please set start floors again", Toast.LENGTH_LONG).show();
        }

        //maxAccStr = sharedPref.getString("MAX_ACC", "");
        //minAccStr = sharedPref.getString("MIN_ACC", "");

        //maxAcc = Float.parseFloat(maxAccStr);
        //maxAcc = Float.parseFloat(minAccStr);

        if (nbOfFloors > 1 && startFloor != 999){
            set = true;
            Toast.makeText(this, "Ready to start tracking" , Toast.LENGTH_LONG).show();
            movement.setNbOfFloors(nbOfFloors);
            movement.setStartFloor(startFloor);
            //movement.
        }


        //sensor listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if (set == true && startSet == true) {
                    text2.setText("" + String.format("%.2f", tz));
                    movement.Prati(tx, ty, tz);
                    speed.setText(movement.getSpeed()+"");
                    upDown.setText(movement.getUpDown()+"");
                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
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
                    trackingStatus.setText("Active"+ movement.getMaxAmp());
                }
               // Toast.makeText(MainActivity.this, "Ready" + movement.getAccValues().size()+ " "+ movement.getSumValues().size() +" " + movement.getSpeedValues().size(), Toast.LENGTH_LONG).show();
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
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onPause();
                accelerometer.on = false;
                movement.resetAll();
                text2.setText(""+0);
                speed.setText(""+0);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSet = true;
                if (set == false){
                    Toast.makeText(MainActivity.this, "Error" , Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Tracking started" , Toast.LENGTH_LONG).show();
                    //trackingStatus.setText("Active");
                    movement.setZeroSec();
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                accelerometer.on = false;
                startSet = false;
                Toast.makeText(MainActivity.this, "Tracking stoped" , Toast.LENGTH_LONG).show();
                trackingStatus.setText("Not Active");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        accelerometer.register();
        gyroscope.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

        accelerometer.unregister();
        gyroscope.unregister();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intentSettings);
        }
        return super.onOptionsItemSelected(item);
    }

}