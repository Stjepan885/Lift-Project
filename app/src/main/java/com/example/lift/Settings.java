package com.example.lift;

import static com.example.lift.Calibrate.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button saveButton = findViewById(R.id.buttonSaveSettings);
        Button startButton = findViewById(R.id.buttonStartSettings);
        Button backButton = findViewById(R.id.buttonBackSettings);

        EditText firstFloor = findViewById(R.id.fFloorText);
        EditText lastFloor = findViewById(R.id.fFloorText);
        EditText startFloor = findViewById(R.id.fFloorText);
/*
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        try {
            int fF = sharedPreferences.getInt("NUMBER_OF_FLOORS_KEY" , Integer.parseInt("0"));
            startFloor.setText(fF+"");

        }catch (Exception e){
            Toast toast = Toast.makeText(Settings.this, "No saved data", Toast.LENGTH_LONG);
            toast.show();
        }

*/


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                int fFloor;
                int lFloor;
                int sFloor;
                int nbOfFloors;

                try {
                    fFloor = Integer.parseInt(firstFloor.getText().toString());
                    lFloor = Integer.parseInt(lastFloor.getText().toString());
                    sFloor = Integer.parseInt(startFloor.getText().toString());

                    nbOfFloors = fFloor + lFloor;

                    editor.putInt("NUMBER_OF_FLOORS_KEY" , nbOfFloors);
                    editor.putInt("START_FLOOR_KEY", sFloor);
                    editor.apply();


                    Toast toast = Toast.makeText(Settings.this, "Saved", Toast.LENGTH_LONG);
                    toast.show();

                }catch (Exception e){
                    Toast toast = Toast.makeText(Settings.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}