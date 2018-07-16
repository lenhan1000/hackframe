package com.example.silc.hackathonframework.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class State {
    int id;
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

    public static ArrayList<State> countryToStates(ArrayList<State> list, int country_id){
        ArrayList<State> result = new ArrayList<State>();
        for (int i = 0;i<list.size(); i++)
            if (list.get(i).country_id == country_id)
                result.add(list.get(i));
        return result;
    }

    public static ArrayList<String> toArrayStrings(ArrayList<State> list){
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
