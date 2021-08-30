package com.example.lift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setContentView(R.layout.activity_settings);

        Button saveButton = findViewById(R.id.buttonSaveSettings);
        Button startButton = findViewById(R.id.buttonStartSettings);
        Button backButton = findViewById(R.id.buttonBackSettings);

        EditText nbOfFloorsText = findViewById(R.id.fFloorText);
        EditText startFloorText = findViewById(R.id.sFloorText);

        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            int nF = sharedPreferences.getInt("NUMBER_OF_FLOORS_KEY" , 0);
            int sF = sharedPreferences.getInt("START_FLOOR_KEY" , 0);
            nbOfFloorsText.setText(nF+"");
            startFloorText.setText(sF+"");
        }catch (Exception e){
            Toast toast = Toast.makeText(Settings.this, "No information on numbers of floors", Toast.LENGTH_LONG);
            toast.show();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sFloor;
                int nbOfFloors;

                try {
                    nbOfFloors = Integer.parseInt(nbOfFloorsText.getText().toString());
                    sFloor = Integer.parseInt(startFloorText.getText().toString());

                    if (nbOfFloors >= sFloor){


                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt("NUMBER_OF_FLOORS_KEY" , nbOfFloors);
                        editor.putInt("START_FLOOR_KEY", sFloor);
                        editor.commit();

                        Toast toast1 = Toast.makeText(Settings.this, "Data saved", Toast.LENGTH_LONG);
                        toast1.show();

                        saved = true;
                    }else{
                        Toast toast = Toast.makeText(Settings.this, "Number of floor must be grater or equal than starting floor", Toast.LENGTH_LONG);
                        toast.show();
                        saved = false;
                    }

                }catch (Exception e){
                    Toast toast = Toast.makeText(Settings.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved){
                    Intent preferenceIntent = new Intent(Settings.this, MainActivity.class);
                    Settings.this.startActivity(preferenceIntent);
                }else{
                    Toast toast = Toast.makeText(Settings.this, "Incorrect information about lift", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferenceIntent = new Intent(Settings.this, LaunchingActivity.class);
                Settings.this.startActivity(preferenceIntent);
            }
        });
    }
}