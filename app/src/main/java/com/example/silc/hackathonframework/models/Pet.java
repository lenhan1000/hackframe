package com.example.silc.hackathonframework.models;

import android.content.Context;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Pet extends Model{
    private static final String TAG = "models.Pet";

    private String name;
    private String type;
    private String breed;
    private double age;
    private double weight;
    private ArrayList<String> personality;
    private boolean gender;
    private boolean spayed;
    private Date birth;
    private ArrayList<Object> medicalHist;
    private ArrayList<Object> meals;
    private ArrayList<Object> lastLoc;
    private Date lastSync;
    private ArrayList<Object> lastActivity;
    private ArrayList<Object> locHist;
    private ArrayList<Object> activityHist;

    public Pet(){
        type = null;
        breed = null;
        personality = new ArrayList<String>();
    }

    public String getName(){ return this.name; }

    public String getType(){ return this.type; }

    public String getBreed(){ return this.breed; }

    public double getAge (){ return this.age; }

    public double getWeight(){ return this.weight; }

    public ArrayList<String> getPersonality(){ return this.personality; }

    public boolean getGender(){ return this.gender; }

    public boolean getSpayed(){ return this.spayed; }

    public Date getBirth(){ return this.birth; }


    public void setName(String in){ this.name = in; }

    public void setType(String in){ this.type = in; }

    public void setBreed(String in){ this.breed = in; }

    public void setAge(double in){ this.age = in; }

    public void setWeight(double in){ this.weight = in; }

    public void setPersonality(ArrayList<String> in){ this.personality = in; }

    public void setGender(boolean in){ this.gender = in; }

    public void setSpayed(boolean in){ this.spayed = in; }

    public void setBirth(Date in){ this.birth = in; }

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
            j.put("personality", personality);
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
}
