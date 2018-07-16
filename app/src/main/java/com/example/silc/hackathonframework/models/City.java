package com.example.silc.hackathonframework.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    int id;
    String name;
    int state_id;

    public City(JSONObject c){
        try {
            this.id = Integer.parseInt(c.getString("id"));
            this.name = c.getString("name");
            this.state_id = Integer.parseInt(c.getString("state_id"));
        }catch (JSONException e){
        }
    }

    public static ArrayList<City> stateToCities(ArrayList<City> list, int state_id){
        ArrayList<City> result = new ArrayList<City>();
        for (int i = 0;i<list.size(); i++)
            if (list.get(i).state_id == state_id)
                result.add(list.get(i));
        return result;
    }

    public static ArrayList<String> toArrayStrings(ArrayList<City> list){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0;i<list.size(); i++)
            result.add(list.get(i).name);
        return result;
    }

    public static ArrayList<City> jsonToCity(ArrayList<JSONObject> list){
        ArrayList<City> result = new ArrayList<City>();
        for (int i = 0;i<list.size(); i++){
            result.add(new City(list.get(i)));
        }
        return result;
    }
}
