package com.example.lift;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private float kretanje[];
    private List<Float> filter;
    final int filterSensitivity = 3;
    private int filterCount = 2;
    private float filterSum = 0;

    public Movment() {
        this.kretanje = new float[filterSensitivity];
        this.filter = new ArrayList<>();
    }

    public float Prati(float x){

        if (filterCount > 0){
            kretanje[filterCount] = x;
            filterCount--;
        }else {
            filterSum = 0;
            for (int i = 0; i < filterSensitivity; i++) {
                if (kretanje[i] < 0.15)
                filterSum += kretanje[i];
            }
            filterSum /= filterSensitivity;
            filterCount = 2;
            filter.add(filterSum);
        }

        return filterSum;
    }

    public float Prati1(float x){



        return filterSum;
    }

    public int getLength(){

        return filter.size();
    }

    public float getFloat(int i){
        return this.filter.get(i);
    }


    public String getValue(int i) {

        float c = filter.get(i);

        int x = (int) c;

        String v = String.valueOf(x);

        return v;
    }


}
