package com.example.silc.hackathonframework.models;

import com.mbientlab.metawear.data.Acceleration;

import java.util.Locale;

public class Accel {
    public float x, y ,z;
    public Accel(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Accel(Acceleration data){
        this.x  = data.x();
        this.y = data.y();
        this.z = data.z();
    }

    public String toString(){
        return String.format(Locale.ENGLISH,"{\"x\": %f, \"y\": %f, \"z\": %f}", x, y, z);
    }
}
