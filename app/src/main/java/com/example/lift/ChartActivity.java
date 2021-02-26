package com.example.lift;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private Movment movment;
    private LineChart mChart;
    private ArrayList<Entry> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mChart = (LineChart) findViewById(R.id.lineChart);
        movment = new Movment();

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        /*
        ArrayList<Entry> xValues = (ArrayList<Entry>) getIntent().getSerializableExtra("valuesX");
        ArrayList<Entry> yValues = (ArrayList<Entry>) getIntent().getSerializableExtra("valuesY");
        ArrayList<Entry> zValues = (ArrayList<Entry>) getIntent().getSerializableExtra("valuesZ");
        */
        ArrayList<Float> values = (ArrayList<Float>) getIntent().getSerializableExtra("values");
        ArrayList<Entry> zValues = new ArrayList<>();

        for(int i = 0; i < values.size(); i++){
            zValues.add(new Entry(i,values.get(i)));
        }


        LineDataSet setX = new LineDataSet(zValues,"Data set X");
        setX.setFillAlpha(110);
        setX.setCircleColor(Color.RED);
        setX.setColors(Color.RED);
        /*
        LineDataSet setY = new LineDataSet(yValues,"Data set Y");
        setY.setFillAlpha(110);
        setY.setCircleColor(Color.BLUE);
        LineDataSet setZ = new LineDataSet(zValues,"Data set Z");
        setZ.setFillAlpha(110);
        setZ.setCircleColor(Color.GREEN);
        setZ.setColors(Color.GREEN);
        */

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(setX);
        //dataSet.add(setY);
        //dataSet.add(setZ);


        LineData data = new LineData(dataSet);

        mChart.setData(data);
        int a;
    }
}
