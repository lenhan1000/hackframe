package com.example.silc.hackathonframework.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.mbientlab.metawear.data.Acceleration;

import java.util.Locale;
import java.lang.Math;


@Entity
public class Accel {

    @PrimaryKey
    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "x")
    public double x;


    @ColumnInfo(name = "y")
    public double y;

    @ColumnInfo(name = "z")
    public double z;

    public Accel(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Accel(Acceleration data){
        this.time = System.currentTimeMillis();
        this.x  = data.x();
        this.y = data.y();
        this.z = data.z();
    }

    public String toString(){
        return String.format(Locale.ENGLISH,"{\"x\": %f, \"y\": %f, \"z\": %f}", x, y, z);
    }

    public double getMagnitude(){
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2), .5);
    }

    public static double getMagnitude(double x, double y, double z){
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2), .5);
    }
}
