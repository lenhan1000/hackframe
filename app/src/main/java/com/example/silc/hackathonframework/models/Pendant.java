package com.example.silc.hackathonframework.models;

import android.content.Context;

import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Settings;
import com.mbientlab.metawear.module.Temperature;

import javax.inject.Inject;

public class Pendant extends Model{
    private String address; //MAC ADDRESS
    private Context appContext;

    @Inject
    Settings settings;

    @Inject
    Temperature.Sensor tempSensor;

    @Inject
    Accelerometer accelerometer;

    public Pendant(String address){
        appContext = App.getContext();
        address = address;
    }

    public String getAddress(){
        return address;
    }


}
