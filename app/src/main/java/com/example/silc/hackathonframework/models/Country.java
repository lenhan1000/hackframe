package com.example.silc.hackathonframework.models;

import org.json.*;
import java.util.ArrayList;

public class Country {
    String id;
    String name;
    String sortname;

    public Country(JSONObject c){
        try {
            this.id = c.getString("id");
            this.name = c.getString("name");
            this.sortname = c.getString("sortname");
        }catch (JSONException e){
        }
    }

    public static ArrayList<String> listCountry(ArrayList<Country> list){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0;i<list.size(); i++){
            result.add(list.get(i).name);
        }
        return result;
    }

    public static ArrayList<Country> jsonToCountry(ArrayList<JSONObject> list){
        ArrayList<Country> result = new ArrayList<Country>();
        for (int i = 0;i<list.size(); i++){
            result.add(new Country(list.get(i)));
        }
        return result;
    }
}
