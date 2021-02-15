package com.example.lift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Movment movment;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables and layout elements initialization
        TextView text = findViewById(R.id.textView);
        Button but = findViewById(R.id.button);
        TextView text1 = findViewById(R.id.textView1);
        TextView text2 = findViewById(R.id.textView2);
        Button chartButton = findViewById(R.id.buttonChart);


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
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                accelerometer.on = false;
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("valuesX", movment.getXValues());
                intent.putExtra("valuesY", movment.getYValues());
                intent.putExtra("valuesZ", movment.getZValues());
                startActivity(intent);
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