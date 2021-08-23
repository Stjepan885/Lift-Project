package com.example.lift;

import android.widget.Toast;

import java.util.ArrayList;

public class Movement {

    private boolean floorChange = false;
    private boolean on = false;
    private boolean onMove = false;
    private boolean still = true;
    private int upDown = 0; // up = 1, down = 2, stationary = 0
    private int upDownPrevious = 0;

    private int nbOfFloors;
    private int startFloor;
    private int endFloor;
    private int currentFloor;

    private long timeBetweenFloors;
    private long overallTime;
    private long floorStartTime;
    private long zeroSec;

    private float maxAmp=0;
    private float minAmp=999;

    float counter = 0;
    float sum = 0;
    float speed = 0;

    private ArrayList<Float> accValues = new ArrayList<>();
    private ArrayList<Float> sumValues = new ArrayList<>();
    private ArrayList<Float> speedValues = new ArrayList<>();

    private ArrayList<Float> levels = new ArrayList<>();
    private float[] array;
    private float[] upArray;
    private float[] downArray;



    public void Prati(float x, float y, float z){
        if (counter < 10){
            z = 0;
        }
        counter++;
        speed = speed + 0.5f * z;
        sum += z;
        accValues.add(z);
        speedValues.add(speed);
        sumValues.add(sum);

        if (z<minAmp){
            minAmp = z;
        }
        if (z>maxAmp){
            maxAmp = z;
        }

        if (upDown == 4 && sum < 0.01 && sum > -0.01){
            upDown = 0;
        }


         if (onMove == false){
            //trip started +
            if (sum > 0.1f ){
                onMove = true;
                upDown = 1;
                floorStartTime = ((System.currentTimeMillis()/1000)%60);
            }
            if (sum < -0.1f){
                onMove = true;
                upDown = 2;
                floorStartTime = ((System.currentTimeMillis()/1000)%60);
            }
        }else if (onMove == true){
            if (sum > 0.1f && upDown == 2){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = ((System.currentTimeMillis()/1000)%60) - floorStartTime;
                floorChange = true;
            }
            if (sum < -0.1f && upDown == 1){
                onMove = false;
                upDownPrevious = upDown;
                upDown = 4;
                timeBetweenFloors = ((System.currentTimeMillis()/1000)%60) - floorStartTime;
                floorChange = true;
            }
        }


        if (floorChange == true && upDownPrevious == 1){
            floorChange = false;

            for (int i = 0; i < nbOfFloors-1; i++){
                if (upArray[i] == 0){
                    upArray[i] = timeBetweenFloors;
                    currentFloor += i+1;
                    if (currentFloor > nbOfFloors){
                        currentFloor = nbOfFloors;
                    }
                    break;
                }else if (timeBetweenFloors < (upArray[i] - 10)){
                    for (int j = nbOfFloors-1; j > i; j--){
                        upArray[j] = upArray[j-1];
                    }
                    upArray[i] = timeBetweenFloors;
                    currentFloor += i+1;
                    if (currentFloor > nbOfFloors){
                        currentFloor = nbOfFloors;
                    }
                    break;
                }else if (timeBetweenFloors < (upArray[i] + 1) && timeBetweenFloors > (upArray[i] - 1)){
                    break;
                }
            }



        }else if (floorChange == true && upDownPrevious == 2){
            floorChange = false;

            for (int i = 0; i < nbOfFloors-1; i++){
                if (downArray[i] == 0){
                    downArray[i] = timeBetweenFloors;
                    currentFloor -= i+1;
                    if (currentFloor < 0){
                        currentFloor = 0;
                    }
                    break;
                }else if (timeBetweenFloors < (downArray[i] - 10)){
                    for (int j = nbOfFloors-1; j > i; j--){
                        downArray[j] = downArray[j-1];
                    }
                    downArray[i] = timeBetweenFloors;
                    currentFloor -= i+1;
                    break;
                }else if (timeBetweenFloors < (downArray[i] + 1) && timeBetweenFloors > (downArray[i] - 1)){
                    if (currentFloor < 0){
                        currentFloor = 0;
                    }
                    break;
                }
            }

        }


    }


    public void setNbOfFloors(int nbOfFloors) { this.nbOfFloors = nbOfFloors; }

    public void setStartFloor(int startFloor) { this.startFloor = startFloor; currentFloor = startFloor; }

    public float getSpeed() { return speed; }

    public int getCurrentFloor() { return currentFloor; }

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
        upArray = new float[nbOfFloors-1];
        downArray = new float[nbOfFloors-1];
        for (int i = 0; i < nbOfFloors-1; i++){
            upArray[i] = 0;
            downArray[i] = 0;
            array[i] = 0;
        }
    }

    public float getMaxAmp() {
        return maxAmp;
    }
}
