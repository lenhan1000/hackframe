package com.example.silc.hackathonframework.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String displayName;
    public String mphone;
    public String carrier;
    public User(){}
    public User(String displayName, String mphone, String carrier){
        this.displayName = displayName;
        this.mphone = mphone;
        this.carrier = carrier;
    }

}
