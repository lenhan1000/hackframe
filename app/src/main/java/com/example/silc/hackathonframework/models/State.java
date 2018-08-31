package com.example.silc.hackathonframework.models;

import android.content.Context;

import com.example.silc.hackathonframework.helpers.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class State {
    int id;
    private static final String JSONfile = "states.json";
    String name;
    int country_id;

    public State(JSONObject c){
        try {
            this.id = Integer.parseInt(c.getString("id"));
            this.name = c.getString("name");
            this.country_id = Integer.parseInt(c.getString("country_id"));
        }catch (JSONException e){
        }
    }

    public static ArrayList<State> getStateArray(Context cnxt, int cid)
            throws JSONException, IOException{
        String str = Utils.loadJSONFromAsset(cnxt, JSONfile);
        ArrayList<JSONObject> list = Utils.getArrayListFromJSONArray(new JSONObject(str)
                .getJSONArray("states"));
        ArrayList<JSONObject> result = new ArrayList<>();
        for (int i = 0;i<list.size(); i++)
            if (list.get(i).getInt("country_id") == cid)
                result.add(list.get(i));
        return jsonToState(result);
    }

    public static ArrayList<String> getStringArray(ArrayList<State> list){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0;i<list.size(); i++)
            result.add(list.get(i).name);
        return result;
    }

    public static int stateId(ArrayList<State> list, String state){
        for (int i = 0; i<list.size(); i++) {
            if (list.get(i).name == state)
                return list.get(i).id;
        }
        return 0;
    }

    public static ArrayList<State> jsonToState(ArrayList<JSONObject> list){
        ArrayList<State> result = new ArrayList<State>();
        for (int i = 0;i<list.size(); i++){
            result.add(new State(list.get(i)));
        }
        return result;
    }
}
