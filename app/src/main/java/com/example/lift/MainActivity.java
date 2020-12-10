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
    private Movment kretanje1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables and layout elements initialization
        TextView text = findViewById(R.id.textView);
        Button but = findViewById(R.id.button);

        final float[] max = {0};


        //sensor and activities initialization
        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);

        kretanje1 = new Movment();

        //sensor listeners
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                text.setText("tx: " + Math.round(tx) + "ty: " + Math.round(ty) +"tz: "+ Math.round(tz));

                if (tx < -5.00){

                    kretanje1.Prati(tx);
                    Log.i("dodan -", "tx: "+ tx);

                }else if(tx > 5.00) {
                    kretanje1.Prati(tx);
                    Log.i("dodan +", "tx: "+ tx);
                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                //text.setText("tx: " + Math.round(rx) + "ty: " + Math.round(ry) +"tz: "+ Math.round(rz));
            }
        });



        //other activity
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accelerometer.on){
                    onPause();
                    accelerometer.on = false;

                    Log.i("ds", " " + kretanje1.getLength());

                    for (int i = 0; i <  kretanje1.getLength(); i++){

                        Log.i("ovo", kretanje1.getFloat(i) + " ");
                    }

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