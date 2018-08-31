package com.example.silc.hackathonframework.models;

import android.content.Context;

import com.example.silc.hackathonframework.helpers.Utils;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;

public class Country {
    int id;
    private static final String JSONfile = "countries.json";
    private String name;
    private String sortname;

    public Country(JSONObject c){
        try {
            this.id = Integer.parseInt(c.getString("id"));
            this.name = c.getString("name");
            this.sortname = c.getString("sortname");
        }catch (JSONException e){
        }
    }

    public static ArrayList<String> getStringArray(ArrayList<Country> list){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0;i<list.size(); i++)
            result.add(list.get(i).name);
        return result;
    }

    public static ArrayList<Country> getCountryArray(Context cnxt)
            throws JSONException, IOException{
        String str = Utils.loadJSONFromAsset(cnxt, JSONfile);
        ArrayList<JSONObject> list = Utils.getArrayListFromJSONArray(new JSONObject(str)
                .getJSONArray("countries"));
        ArrayList<Country> result = new ArrayList<Country>();
        for (int i = 0;i<list.size(); i++){
            result.add(new Country(list.get(i)));
        }
        return result;
    }
}
