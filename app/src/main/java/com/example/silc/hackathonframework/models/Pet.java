package com.example.silc.hackathonframework.models;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import okhttp3.internal.http2.Http2;

@IgnoreExtraProperties
public class Pet extends ModelObservable{
    private static final String TAG = "models.Pet";
    private static final int AGE_LIMIT = 30;
    private String name;
    private String type;
    private String breed;
    private double age;
    private double weight;
    private List<String> personality;
    private boolean gender;
    private boolean spayed;
    private Date birth;
//    private ArrayList<Object> medicalHist;
//    private ArrayList<Object> meals;
//    private ArrayList<Object> lastLoc;
//    private Date lastSync;
//    private ArrayList<Object> lastActivity;
//    private ArrayList<Object> locHist;
//    private ArrayList<Object> activityHist;

    public Pet(){
        type = null;
        breed = null;
        birth = new Date();
        personality = new ArrayList<>();
    }

    public String getName(){ return name; }

    public String getType(){ return type; }

    public String getBreed(){ return breed; }

    public double getAge(){ return age; }

    public String getAgeString(){
        return String.format(Locale.US, "%d", (int) age);
    }

    public double getWeight(){ return weight; }

    public String getWeightString(){
        return String.format(Locale.US, "%,.2f lbs", weight);
    }

//    public ArrayList<String> getPersonality(){ return this.personality; }

    public boolean getGender(){ return gender; }

    public String getGenderString(){
        return (gender) ? "Male" : "Female";
    }

    public boolean getSpayed(){ return spayed; }

    public String getSpayedString(){
        return (spayed) ? "Yes" : "No";
    }

    public Date getBirth(){ return birth; }

    public String getBirthString(){
        DateTime date = new DateTime(birth.getTime());
        return date.toString("MM-dd-yyyy");
    }

    public static int getAgeLimit(){ return AGE_LIMIT; }


    public void setName(String in){ this.name = in; }

    public void setType(String in){ this.type = in; }

    public void setBreed(String in){
        this.breed = in;
        notifyChange();
    }

    public void setAge(double in){
        this.age = in;
        notifyChange();
    }

    public void setWeight(double in){
        this.weight = in;
        notifyChange();
    }

//    public void setPersonality(ArrayList<String> in){ this.personality = in; }

    public void setGender(boolean in){
        this.gender = in;
        notifyChange();
    }

    public void setSpayed(boolean in){
        this.spayed = in;
        notifyChange();
    }

    public void setBirth(Date in){
        this.birth = in;
        notifyChange();
    }

    public String toString(){
        //TODO
        return "";
    }

    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try{
            j.put("name", name);
            j.put("type", type);
            j.put("breed", breed);
            j.put("age", age);
            j.put("weight", weight);
            j.put("personality", new JSONArray(personality));
            j.put("gender", gender);
            j.put("spayed", spayed);
            j.put("birth", birth.getTime());
        }catch(JSONException e){
            Log.e(TAG, e.getMessage());
        }
        return j;
    }

    public static void getBreedList(Context context, String type){
        String token = getToken(context);
        if(token.isEmpty()) return;
        Http2Request req = new Http2Request(context);
        String infoUrl;
        if (type.equals("Dog")){
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }else if (type.equals("Cat")){
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }else{
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }
        req.get(req.baseUrl, infoUrl,
                token);
    }

    public void getBreedList(Context context){
        String token = getToken(context);
        if(token.isEmpty()) return;
        Http2Request req = new Http2Request(context);
        String infoUrl;
        if (type.equals("Dog")){
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }else if (type.equals("Cat")){
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }else{
            infoUrl = context.getResources().getString(R.string.api_pet_dog_breed);
        }
        req.get(req.baseUrl, infoUrl,
                token);
    }

    public static String getMyPets(Context context){
        String token = getToken(context);
        if(token.isEmpty()) return "";
        Http2Request req = new Http2Request(context);
        String route = context.getResources().getString(R.string.api_user_pets);
        req.get(req.baseUrl,
                route,
                token);
        return route;
    }

    public static String getPetInfo(Context context) throws JSONException{
        String token = getToken(context);
        if(token.isEmpty()) return "";
        String petId = getSelectedId(context);
        if(petId.isEmpty()) return "";
        String route = context.getResources().getString(R.string.api_pet_info);
        Http2Request req = new Http2Request(context);
        JSONObject json = new JSONObject();
        json.put("petId", petId);
        req.post(req.baseUrl,
                route,
                json.toString(),
                token);
        return route;

    }

    public static String getFirstPet(Context context){
        String token = getToken(context);
        if(token.isEmpty()) return "";
        Http2Request req = new Http2Request(context);
        String route = context.getResources().getString(R.string.api_user_first_pet);
        req.get(req.baseUrl,
                route,
                token);
        return route;
    }

    public static void setSelected(String name, String id, Context context){
        Utils.putSharedPreferenes(context,
                "name",
                name,
                "selected_pet");
        Utils.putSharedPreferenes(context,
                "petId",
                id,
                "selected_pet");

    }

    public static String getSelectedName(Context context){
        return Utils.getStringSharedPreferences(
                context,
                "name",
                "",
                "selected_pet"
        );
    }

    public static String getSelectedId(Context context){
        return Utils.getStringSharedPreferences(
                context,
                "petId",
                "",
                "selected_pet"
        );
    }

    public void setJSON(JSONObject petJSON) throws JSONException{
        this.name = petJSON.getString("name");
        this.type = petJSON.getString("type");
        this.breed = petJSON.getString("breed");
        this.age = petJSON.getDouble("age");
        this.weight = petJSON.getDouble("weight");
        this.gender = petJSON.getBoolean("gender");
        this.spayed = petJSON.getBoolean("spayed");
        this.birth = new Date(petJSON.getLong("birth"));
    }

}
