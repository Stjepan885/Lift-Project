package com.example.lift;

import java.util.ArrayList;

public class Movement {

    private int nbOfFloors;
    private int startFloor;
    private int endFloor;
    private long timeBetweenFloors;
    private long overallTime;
    private boolean on = false;
    private boolean onMove = false;
    private long zeroSec;


    private ArrayList<Float> accValues = new ArrayList<>();
    private ArrayList<Float> sumValues = new ArrayList<>();
    float counter = 0;
    float sum = 0;
    private float speed = 0;

    private ArrayList<Float> levels = new ArrayList<>();


    public void Prati(float x, float y, float z){
        if (counter < 10){
            z = 0;
        }
        accValues.add(z);
        sum += z;
        counter++;
        //speed = speed + (counter) * z;
        //speed *= 0.1;
        sumValues.add(sum);
    }

    public ArrayList<Float> getAccValues() {return accValues;}
    public ArrayList<Float> getSumValues () {return sumValues;}


    public void setNbOfFloors(int nbOfFloors) {
        this.nbOfFloors = nbOfFloors;
    }

    public void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    public float getSpeed() {
        return speed;
    }

    public void resetAll(){
        accValues.clear();
        sumValues.clear();
        counter=0;
        zeroSec = System.currentTimeMillis()/1000;
        sum = 0;
    }


    public void setZeroSec() {
        this.zeroSec = System.currentTimeMillis()/1000;
    }
}
