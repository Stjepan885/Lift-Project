package com.example.lift;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private LineChart mChart;
    private ArrayList<Entry> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mChart = (LineChart) findViewById(R.id.lineChart);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Float> accValues = (ArrayList<Float>) getIntent().getSerializableExtra("values");
        ArrayList<Float> sumValues = (ArrayList<Float>) getIntent().getSerializableExtra("sumValues");
        ArrayList<Float> speedValues = (ArrayList<Float>) getIntent().getSerializableExtra("speedValues");

        ArrayList<Entry> accValuesChart = new ArrayList<>();
        ArrayList<Entry> sumValuesChart = new ArrayList<>();
        ArrayList<Entry> speedValuesChart = new ArrayList<>();


        for(int i = 5; i < accValues.size(); i++){

            accValuesChart.add(new Entry(i, accValues.get(i)));
            sumValuesChart.add(new Entry(i, sumValues.get(i)));
            speedValuesChart.add(new Entry(i, speedValues.get(i)));

       }


        LineDataSet setX = new LineDataSet(accValuesChart,"Acceleration");
        setX.setFillAlpha(110);
        setX.setCircleColor(Color.RED);
        setX.setColors(Color.RED);

        LineDataSet setY = new LineDataSet(sumValuesChart,"Sum");
        setY.setFillAlpha(110);
        setY.setCircleColor(Color.BLUE);

        LineDataSet setZ = new LineDataSet(speedValuesChart,"Velocity");
        setZ.setFillAlpha(110);
        setZ.setCircleColor(Color.GREEN);
        setZ.setColors(Color.GREEN);


        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(setX);
        dataSet.add(setY);
        dataSet.add(setZ);


        LineData data = new LineData(dataSet);

        mChart.setData(data);
        int a;

    }

}
