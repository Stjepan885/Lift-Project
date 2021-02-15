package com.example.lift;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private ArrayList<Entry> xValues = new ArrayList<>();
    private ArrayList<Entry> yValues = new ArrayList<>();
    private ArrayList<Entry> zValues = new ArrayList<>();
    float counter = 0;

    public void Prati(float x, float y, float z){
        xValues.add(new Entry(counter,x));
        yValues.add(new Entry(counter,y));
        zValues.add(new Entry(counter,z));
        //Log.i("s",""+ yValues.get(Math.round(counter)));
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

}
