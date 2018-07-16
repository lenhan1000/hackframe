package com.example.silc.hackathonframework.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String displayName;
    public String mPhone;
    public String carrier;
    public String address;
    public String country;
    public String state;
    public String city;
    public String zipCode;

    public User(){}
    public User(String displayName, String mPhone, String carrier, String zipCode,
                String address, String country, String state, String city){
        this.displayName = displayName;
        this.mPhone = mPhone;
        this.carrier = carrier;
        this.address = address;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
    }

}
