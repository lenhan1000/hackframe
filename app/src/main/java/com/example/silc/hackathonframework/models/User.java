package com.example.silc.hackathonframework.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.os.Parcel;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;

@IgnoreExtraProperties
public class User implements Parcelable{
    private static final String TAG = "models.Users";

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in){
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String email;
    private String displayName;
    private String mobilePhone;
    private String carrier;
    private String address;
    private JSONObject country;
    private JSONObject state;
    private String city;
    private String zipCode;
    private String countryCode;

    public User(){
        email=null;
        displayName= null;
        mobilePhone= null;
        carrier= null;
        address= null;
        country= null;
        state= null;
        city= null;
        zipCode= null;
        countryCode= null;
    }
    public User(String displayName, String mobilePhone, String carrier, String zipCode,
                String address, JSONObject country, JSONObject state, String city,
                String countryCode, String email){
        this.displayName = displayName;
        this.mobilePhone = mobilePhone;
        this.carrier = carrier;
        this.address = address;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.countryCode = countryCode;
        this.email=email;
    }

    public String getEmail(){ return this.email; }

    public String getDisplayName(){ return this.displayName; }

    public String getmobilePhone(){ return this.mobilePhone; }

    public String getCarrier(){ return this.carrier; }

    public String getAddress(){ return this.address; }

    public JSONObject getCountry() { return this.country; }

    public JSONObject getState(){ return this.state; }

    public String getCity(){ return this.city; }

    public String getZipCode(){ return this.zipCode; }

    public String getCountryCode(){ return this.zipCode; }

    public void setEmail(String arg){ this.email = arg; }

    public void setDisplayName(String arg){  this.displayName = arg; }

    public void setmobilePhone(String arg){  this.mobilePhone = arg; }

    public void setCarrier(String arg){  this.carrier = arg; }

    public void setAddress(String arg){  this.address = arg; }

    public void setCountry(JSONObject arg){  this.country = arg; }

    public void setState(JSONObject arg){  this.state = arg; }
            
    public void setCity(String arg){  this.city = arg; }

    public void setZipCode(String arg){  this.zipCode = arg; }

    public void setCountryCode(String arg){  this.zipCode = arg; }

    //Parcelling
    public User(Parcel in) {
        this.displayName = in.readString();
        this.mobilePhone = in.readString();
        this.carrier = in.readString();
        this.address = in.readString();
        this.zipCode = in.readString();
        this.countryCode = in.readString();
        this.city = in.readString();
        try {
            this.country = new JSONObject(in.readString());
            this.state = new JSONObject(in.readString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents(){ return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.email);
        dest.writeString(this.displayName);
        dest.writeString(this.mobilePhone);
        dest.writeString(this.carrier);
        dest.writeString(this.address);
        dest.writeString(this.countryCode);
        dest.writeString(this.country.toString());
        dest.writeString(this.state.toString());
        dest.writeString(this.city.toString());
        dest.writeString(this.zipCode);
    }


    public String toString(){
        return "Student{" +
                "email='"+ email + '\''+
                "displayName='" + displayName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", carrier='" + carrier + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("displayName", displayName);
            jo.put("countryCode", countryCode);
            jo.put("mobilePhone", mobilePhone);
            jo.put("carrier", carrier);
            jo.put("address", address);
            jo.put("country", country);
            jo.put("state", state);
            jo.put("city", city);
            jo.put("zipCode", zipCode);
        }catch(JSONException e){
            Log.e(TAG, e.getMessage());
        }

        return jo;
    }

    public static void processLogin(String email, String token, Context context){
        Utils.putSharedPreferenes(context,
                context.getString(R.string.user_preference_email),
                email,
                context.getString(R.string.user_preference));
        Utils.putSharedPreferenes(context,
                context.getString(R.string.user_preference_token),
                token,
                context.getString(R.string.user_preference));

    }

    public static void getUserInfo(Context context){
        String token = getToken(context);
        if (token.isEmpty()) return;
        Http2Request req = new Http2Request(context);
        String infoUrl = context.getResources().getString(R.string.api_user_info);
        req.get(context.getString(R.string.api_base_url), infoUrl,
                token);
    }

    public static String getToken(Context context){
        return Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_token),
                "",
                context.getString(R.string.user_preference));
    }
}
