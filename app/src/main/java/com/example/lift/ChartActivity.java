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

        ArrayList<Entry> yValues = (ArrayList<Entry>) getIntent().getSerializableExtra("values");

        LineDataSet set1 = new LineDataSet(yValues,"Data set 1");

        set1.setFillAlpha(110);
        set1.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(set1);

        LineData data = new LineData(dataSet);

        mChart.setData(data);
    }
}
