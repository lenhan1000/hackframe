package com.example.silc.hackathonframework.models;

import android.os.Parcelable;
import android.os.Parcel;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String displayName;
    private String mPhone;
    private String carrier;
    private String address;
    private String country;
    private String state;
    private String city;
    public String zipCode;
    public String countryCode;

    public User(){
        displayName= null;
        mPhone= null;
        carrier= null;
        address= null;
        country= null;
        state= null;
        city= null;
        zipCode= null;
        countryCode= null;
    }
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

    //Parcelling
    public User(Parcel in){
        this.displayName = in.readString();
        this.mPhone = in.readString();
        this.carrier = in.readString();
        this.address = in.readString();
        this.country = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.zipCode = in.readString();
        this.countryCode = in.readString();
    }

    @Override
    public int describeContents(){ return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.displayName);
        dest.writeString(this.mPhone);
        dest.writeString(this.carrier);
        dest.writeString(this.address);
        dest.writeString(this.countryCode);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.zipCode);
    }

    public String toString(){
        return "Student{" +
                "displayName='" + displayName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", carrier='" + carrier + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
