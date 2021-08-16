package com.example.lift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    private Movment movment;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }


        //variables and layout elements initialization
        TextView text = findViewById(R.id.textView);
        Button but = findViewById(R.id.buttonPause);
        TextView text1 = findViewById(R.id.textView1);
        TextView text2 = findViewById(R.id.textView2);
        Button chartButton = findViewById(R.id.buttonChart);
        EditText nazivFile = findViewById(R.id.floorNumber);
        Button saveButton = findViewById(R.id.saveButton);

        int NbOfFloors;

        //preference attributes
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String stringNbOfFloorsPref = sharedPref.getString(SettingsActivity.KEY_PREF_FLOOR_NUMBER, "0");
        //NbOfFloors = Integer.parseInt(stringNbOfFloorsPref);

        //Toast.makeText(this, "" + NbOfFloors, Toast.LENGTH_LONG).show();

        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        movment = new Movment();


        //sensor listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                text.setText("" + String.format("%.2f", tx));
                text1.setText("" + String.format("%.2f", ty));
                text2.setText("" + String.format("%.2f", tz));
                movment.Prati(tx,ty,tz);
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
            }
        });

        //other activity
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accelerometer.on){
                    onPause();
                    accelerometer.on = false;
                }else{
                    onResume();
                    accelerometer.on = true;
                }
                long time= System.currentTimeMillis();
                text.setText("" + time);
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                accelerometer.on = false;
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                //intent.putExtra("valuesX", movment.getXValues());
                //intent.putExtra("valuesY", movment.getYValues());
                //intent.putExtra("valuesZ", movment.getZValues());
                intent.putExtra("values", movment.getValues());
                startActivity(intent);
            }
        });




        saveButton.setOnClickListener(new View.OnClickListener() {
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