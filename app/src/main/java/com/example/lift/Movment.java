package com.example.lift;

import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private ArrayList<Entry> xValues = new ArrayList<>();
    private ArrayList<Entry> yValues = new ArrayList<>();
    private ArrayList<Entry> zValues = new ArrayList<>();
    private ArrayList<Float> values = new ArrayList<>();
    private ArrayList<Float> sumValues = new ArrayList<>();
    float counter = 0;
    float sum = 0;
    long zeroMill = System.currentTimeMillis()/1000;
    private int nbOfFloors;
    private float speed = 0;

    private ArrayList<Float> levels = new ArrayList<>();



    public void Prati(float x, float y, float z){
        //xValues.add(new Entry(counter,x));
        //yValues.add(new Entry(counter,y));
        //zValues.add(new Entry(counter,z));
        //Log.i("s",""+ yValues.get(Math.round(counter)));
        if (counter < 10){
            z = 0;
        }
        values.add(z);
        sum += z;
        sumValues.add(sum);
        counter++;
        speed = speed + (System.currentTimeMillis()/1000-zeroMill) * z;
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
    public ArrayList<Float> getSumValues () {return sumValues;}

    public int getNbOfFloors() {
        return nbOfFloors;
    }

    public void setNbOfFloors(int nbOfFloors) {
        this.nbOfFloors = nbOfFloors;
    }

    public float getSpeed() {
        return speed;
    }

    public void resetAll(){
        values.clear();
        sumValues.clear();
        counter=0;
        zeroMill = System.currentTimeMillis()/1000;
        sum = 0;
    }

}
