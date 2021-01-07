package com.example.lift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Accelerometer accelerometer;
    private  Gyroscope gyroscope;
    private float max = 0;
    private float min = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables and layout elements initialization
        TextView text = findViewById(R.id.textView);
        Button but = findViewById(R.id.button);
        TextView text1 = findViewById(R.id.textView1);
        TextView text2 = findViewById(R.id.textView2);


        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);

        //sensor listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                text.setText("" + String.format("%.2f", tx));
                if (max < tx ){
                    max = tx;
                    text1.setText("" + String.format("%.2f", max));
                }
                if (min > tx){
                    min = tx;
                    text2.setText("" + String.format("%.2f", min));
                }
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
}