package com.example.silc.hackathonframework.models;

import android.content.Context;

import com.example.silc.hackathonframework.helpers.Utils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class City {
    private static final String JSONfile = "cities.json";
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

    public static ArrayList<City> getCityArray(Context cnxt, int sid)
            throws JSONException, IOException{
        String str = Utils.loadJSONFromAsset(cnxt, JSONfile);
        ArrayList<JSONObject> list = Utils.getArrayListFromJSONArray(new JSONObject(str)
                .getJSONArray("cities"));
        ArrayList<JSONObject> result = new ArrayList<>();
        for (int i = 0;i<list.size(); i++)
            if (list.get(i).getInt("state_id") == sid)
                result.add(list.get(i));
        return jsonToCity(result);
    }

    public static ArrayList<String> getStringArray(ArrayList<City> list){
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
