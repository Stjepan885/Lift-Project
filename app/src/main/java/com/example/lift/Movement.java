package com.example.lift;

import java.util.ArrayList;

public class Movement {

    private boolean floorChange = false;
    private boolean on = false;
    private boolean onMove = false;
    private int upDown = 0; // up = 1, down = 2, stationary = 0

    private int nbOfFloors;
    private int startFloor;
    private int endFloor;
    private int currentFloor;

    private long timeBetweenFloors;
    private long overallTime;
    private long floorStartTime;
    private long zeroSec;

    private float maxAmp;

    float counter = 0;
    float sum = 0;
    float speed = 0;

    private ArrayList<Float> accValues = new ArrayList<>();
    private ArrayList<Float> sumValues = new ArrayList<>();
    private ArrayList<Float> speedValues = new ArrayList<>();

    private ArrayList<Float> levels = new ArrayList<>();
    private float[] array;




    public void Prati(float x, float y, float z){
        if (counter < 10){
            z = 0;
        }
        counter++;
        speed = speed + (((System.currentTimeMillis()/1000)%60)-zeroSec) * z;
        sum += z;
        accValues.add(z);
        speedValues.add(speed);
        sumValues.add(sum);


        if (onMove == false){
            //trip started +
            if (sum > 2.0f ){
                onMove = true;
                upDown = 1;
                floorStartTime = ((System.currentTimeMillis()/1000)%60);
            }
            if (sum < -2.0f){
                onMove = true;
                upDown = 2;
                floorStartTime = ((System.currentTimeMillis()/1000)%60);
            }
        }else if (onMove == true){
            if (sum > 2.0f){
                onMove = false;
                upDown = 0;
                timeBetweenFloors = ((System.currentTimeMillis()/1000)%60) - floorStartTime;
                floorChange = true;
            }
            if (sum < -2.0f){
                onMove = false;
                upDown = 0;
                timeBetweenFloors = ((System.currentTimeMillis()/1000)%60) - floorStartTime;
                floorChange = true;
            }
        }
/*
        if (floorChange == true){
            floorChange = false;
            for (int i = 0; i < nbOfFloors; i++){
                if (timeBetweenFloors > (array[i] + 10) && ){

                }
            }
        }
*/
    }


    public void setNbOfFloors(int nbOfFloors) { this.nbOfFloors = nbOfFloors; }

    public void setStartFloor(int startFloor) { this.startFloor = startFloor; }

    public float getSpeed() {
        return speed;
    }

    public void resetAll(){
        accValues.clear();
        sumValues.clear();
        speedValues.clear();
        counter=0;
        zeroSec = System.currentTimeMillis()/1000;
        sum = 0;
        speed = 0;
    }


    public void setZeroSec() {
        this.zeroSec = (System.currentTimeMillis()/1000)%60;
        on = true;
    }

    public int getUpDown() { return upDown; }

    public ArrayList<Float> getSpeedValues() { return speedValues; }
    public ArrayList<Float> getAccValues() {return accValues;}
    public ArrayList<Float> getSumValues () {return sumValues;}

    public void initializeArray(){
        array = new float[nbOfFloors-1];
        for (int i = 0; i < nbOfFloors; i++){
            array[i] = 0;
        }
    }


}
