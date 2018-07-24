package com.example.silc.hackathonframework.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String displayName = null;
    private String mPhone= null;
    private String carrier= null;
    private String address= null;
    private String country= null;
    private String state= null;
    private String city= null;
    public String zipCode= null;
    public String countryCode= null;

    public User(){}
    public User(String displayName, String mPhone, String carrier, String zipCode,
                String address, String country, String state, String city,
                String countryCode){
        this.displayName = displayName;
        this.mPhone = mPhone;
        this.carrier = carrier;
        this.address = address;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.countryCode = countryCode;
    }

    public String getDisplayName(){ return this.displayName; }

    public String getmPhone(){ return this.mPhone; }

    public String getCarrier(){ return this.carrier; }

    public String getAddress(){ return this.address; }

    public String getCountry(){ return this.country; }

    public String getState(){ return this.state; }

    public String getCity(){ return this.city; }

    public String getZipCode(){ return this.zipCode; }

    public String getCountryCode(){ return this.zipCode; }

    public void setDisplayName(String arg){  this.displayName = arg; }

    public void setmPhone(String arg){  this.mPhone = arg; }

    public void setCarrier(String arg){  this.carrier = arg; }

    public void setAddress(String arg){  this.address = arg; }

    public void setCountry(String arg){  this.country = arg; }

    public void setState(String arg){  this.state = arg; }
            
    public void setCity(String arg){  this.city = arg; }

    public void setZipCode(String arg){  this.zipCode = arg; }

    public void setCountryCode(String arg){  this.zipCode = arg; }
}
