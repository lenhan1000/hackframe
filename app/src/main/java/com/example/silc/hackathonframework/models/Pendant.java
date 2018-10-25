package com.example.silc.hackathonframework.models;

import android.content.Context;

public class Pendant extends Model{
    private String address; //MAC ADDRESS
    private Context appContext;

    public Pendant(String address){
        appContext = App.getContext();
        address = address;
    }

    public String getAddress(){
        return address;
    }


}
