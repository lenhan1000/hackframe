package com.example.silc.hackathonframework.helpers;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;
import com.example.silc.hackathonframework.models.City;
import com.example.silc.hackathonframework.models.Country;
import com.example.silc.hackathonframework.models.State;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class GeographyDialogWrapper extends SingleChoiceDialogWrapper{
    public static final String TAG = "helpers.SCDWrapper";
    public int country_id;
    public int state_id;
    public ArrayList<State> states;

    public GeographyDialogWrapper(Context context){
        super(context);
        country_id = 0;
        state_id = 0;
    }

    public void popCountryDialog(){
        try {
            ArrayList<String> countries = Country.getStringArray(
                   Country.getCountryArray(activity.getBaseContext()));
            popDialog("Select a Country", countries);
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getStackTrace());
            return;
        } catch (IOException e){
            Log.e(TAG, "Read file error: " + e.getStackTrace());
            return;
        }
    }

    public void popStateDialog() {
        try {
            states = State.getStateArray(activity.getBaseContext(), country_id);
            ArrayList<String> list = State.getStringArray(states);
            popDialog("Select a State/Region", list);
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getStackTrace());
        } catch (IOException e) {
            Log.e(TAG, "Read file err: " + e.getStackTrace());
        }
    }

    public void popCityDialog(){
        try{
            ArrayList<String> cities = City.getStringArray(
                    City.getCityArray(activity.getBaseContext(), state_id));
            popDialog("Select a City", cities);
         } catch (JSONException e){
            Log.e(TAG, "Json parsing error: " + e.getStackTrace());
        } catch (IOException e) {
            Log.e(TAG, "Read file err: " + e.getStackTrace());
        }
    }
}
