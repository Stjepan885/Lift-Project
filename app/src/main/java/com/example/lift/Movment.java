package com.example.lift;

import java.util.ArrayList;
import java.util.List;

public class Movment {

    private List<Float> kretanje;


    public Movment() {
        this.kretanje = new ArrayList<Float>();
    }

    public void Prati(float x){

        int y = (int) x;

        kretanje.add(new Float(x));
    }

    public int getLength(){

        return kretanje.size();
    }

    public float getFloat(int i){
        return this.kretanje.get(i);
    }


    public String getValue(int i) {

        float c = kretanje.get(i);

        int x = (int) c;

        String v = String.valueOf(x);

        return v;
    }
}
