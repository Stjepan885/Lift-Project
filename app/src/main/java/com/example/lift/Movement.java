package com.example.lift;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Movement {

    private int nbOfFloors;
    private int currentFloor;

    private boolean onMove = false;
    private int upDown = 0; // up = 1, down = 2, stationary = 0
    private int upDownPrevious = 0;
    private boolean floorChange = false;

    private long startTime;
    private long floorStartTime;
    private long timeBetweenFloors;
    private float timeLimitUp;
    private float timeLimitDown;

    private long timeUp;
    private long timeDown;

    private float averMaxAmp;
    private float averMinAmp;

    float counter = 0;
    float sum = 0;
    float speed = 0;

    private ArrayList<Float> accValues = new ArrayList<>();
    private ArrayList<Float> sumValues = new ArrayList<>();
    private ArrayList<Float> speedValues = new ArrayList<>();

    private ArrayList<Integer> levels = new ArrayList<>();
    private float[] upArray;
    private float[] downArray;

    public void Prati(float z){
        if (counter < 10){
            z = 0;
            counter++;
        }
        speed = speed + 0.5f * z;
        sum += z;
        accValues.add(z);
        speedValues.add(speed);
        sumValues.add(sum);


        if (upDown == 4 && sum < 0.01 && sum > -0.01){
            upDown = 0;
        }


         if (onMove == false && upDown == 0){
            //trip started +
            if (sum > averMaxAmp ){
                onMove = true;
                upDown = 1;
                floorStartTime = System.currentTimeMillis();
            }
            if (sum < averMinAmp){
                onMove = true;
                upDown = 2;
                floorStartTime = System.currentTimeMillis();
            }
        }else if (onMove == true){
            if (sum > averMaxAmp && upDown == 2){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = System.currentTimeMillis() - floorStartTime;
                floorChange = true;
            }
            if (sum < averMinAmp && upDown == 1){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = System.currentTimeMillis() - floorStartTime;
                floorChange = true;
            }
        }


        if (floorChange == true && upDownPrevious == 1){
            floorChange = false;

            for (int i = 0; i < nbOfFloors; i++){
                if (timeBetweenFloors < (upArray[i] + timeLimitUp) && timeBetweenFloors > (upArray[i] - timeLimitUp)){
                    currentFloor = currentFloor + (i+1);
                    levels.add(currentFloor);
                    break;
                }
            }

        }else if (floorChange == true && upDownPrevious == 2){
            floorChange = false;

            for (int i = 0; i < nbOfFloors; i++){
                if (timeBetweenFloors < (downArray[i] + timeLimitDown) && timeBetweenFloors > (downArray[i] - timeLimitDown)){
                    currentFloor = currentFloor - (i+1);
                    levels.add(currentFloor);
                    break;
                }
            }
        }
    }

    public void setNbOfFloors(int nbOfFloors) { this.nbOfFloors = nbOfFloors; }

    public void setStartFloor(int startFloor) { currentFloor = startFloor; }

    public int getCurrentFloor() { return currentFloor; }

    public void resetAll(){
        accValues.clear();
        sumValues.clear();
        speedValues.clear();
        counter=0;
        sum = 0;
        speed = 0;
        setTimeUp(timeUp);
        setTimeDown(timeDown);
    }


    public void setZeroSec() {
        this.startTime = System.currentTimeMillis();
    }

    public int getUpDown() { return upDown; }

    public ArrayList<Float> getSpeedValues() { return speedValues; }
    public ArrayList<Float> getAccValues() {return accValues;}
    public ArrayList<Float> getSumValues () {return sumValues;}

    public void initializeArray(){
        upArray = new float[nbOfFloors];
        downArray = new float[nbOfFloors];
        for (int i = 0; i < nbOfFloors; i++){
            upArray[i] = timeUp * (i+1);
            downArray[i] = timeDown * (i+1);
            Log.i("i+ " + nbOfFloors + " " + i, "array+ " + upArray[i]);
        }
        levels.add(currentFloor);
    }

    public long getOverallTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void setMaxAmp(float maxAmp) {
        averMaxAmp = maxAmp*0.7f;
    }

    public void setMinAmp(float minAmp) {
        averMinAmp = minAmp*0.7f;
    }

    public void setTimeUp(long timeUp) {
        this.timeUp = timeUp;
        timeLimitUp = timeUp/2;
    }

    public void setTimeDown(long timeDown) {
        this.timeDown = timeDown;
        timeLimitDown = timeDown/2;
    }
}
