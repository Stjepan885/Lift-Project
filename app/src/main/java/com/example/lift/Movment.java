package com.example.lift;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private ArrayList<Entry> xValues = new ArrayList<>();
    private ArrayList<Entry> yValues = new ArrayList<>();
    private ArrayList<Entry> zValues = new ArrayList<>();
    private ArrayList<Float> values = new ArrayList<>();
    float counter = 0;
    long zeroMill = System.currentTimeMillis();



    public void Prati(float x, float y, float z){
        //xValues.add(new Entry(counter,x));
        //yValues.add(new Entry(counter,y));
        //zValues.add(new Entry(counter,z));
        //Log.i("s",""+ yValues.get(Math.round(counter)));
        values.add(z);
        counter++;
    }

    public ArrayList<Entry> getXValues() {
        return xValues;
    }
    public ArrayList<Entry> getYValues() {
        return yValues;
    }
    public ArrayList<Entry> getZValues() {
        return zValues;
    }
    public ArrayList<Float> getValues() {return values;}

}
