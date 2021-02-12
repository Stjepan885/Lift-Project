package com.example.lift;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private ArrayList<Entry> yValues = new ArrayList<>();
    float counter = 0;

    public void Prati(float x){
            yValues.add(new Entry(counter,x));
            Log.i("s",""+ yValues.get(Math.round(counter)));
            counter++;
    }

    public ArrayList<Entry> getyValues() {
        return yValues;
    }


}
