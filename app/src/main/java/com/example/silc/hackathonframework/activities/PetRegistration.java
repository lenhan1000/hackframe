package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.aigestudio.wheelpicker.view.WheelStraightPicker;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Pet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetRegistration extends AppCompatActivity implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.PetRegistration";
    private static final boolean DEBUG = true;
    private final Context context = this;
    private static final Integer AGE_LIMIT = 30;
    private Pet pet;
    private String catBreedRoute;
    private String dogBreedRoute;

    private FrameLayout mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_registration);

        catBreedRoute = context.getResources().getString(R.string.api_pet_cat_breed);
        dogBreedRoute = context.getResources().getString(R.string.api_pet_dog_breed);

        pet = new Pet();
        mContentFrame = findViewById(R.id.content);
        inflate_name();
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try{
            boolean success = res.getBoolean("success");
            if (!success) Log.d(TAG, res.getString("msg"));
            else{
                if(id == catBreedRoute || id == dogBreedRoute){
                    final String[] info = Utils.getStringArrayFromJSONArray(
                            res.getJSONArray("msg"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inflate_breed(info);
                        }
                    });
                }
            }
        }catch(JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

    private void inflate_name(){
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_name,
                mContentFrame);
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputView = mContentFrame.findViewById(R.id.infoView);
                String input = inputView.getText().toString();
                if (TextUtils.isEmpty(input) && !DEBUG){
                    inputView.setError("Required.");
                    return;
                }
                pet.setName(input);
                inflate_type();
            }
        });
    }

    private void inflate_type(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_type,
                mContentFrame);
        mContentFrame.findViewById(R.id.buttonCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setType("Cat");
                Pet.getBreedList(context,"Cat");
            }
        });
        mContentFrame.findViewById(R.id.buttonDog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setType("Dog");
                Pet.getBreedList(context, "Dog");
            }
        });
    }

    private void inflate_breed(String[] list){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_breed,
                mContentFrame);
        final WheelStraightPicker wheelView = mContentFrame.findViewById(R.id.infoView);

        wheelView.setData(Arrays.asList(list));
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float v, float v1) {

            }

            @Override
            public void onWheelSelected(int i, String s) {
                pet.setBreed(s);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate_gender();
            }
        });
    }

    private void inflate_gender(){

        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_gender,
                mContentFrame);
        mContentFrame.findViewById(R.id.buttonMale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setGender(true);
                inflate_age();
            }
        });
        mContentFrame.findViewById(R.id.buttonFemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setGender(false);
                inflate_age();
            }
        });
    }

    private void inflate_age(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_age,
                mContentFrame);
        final WheelCurvedPicker wheelView = mContentFrame.findViewById(R.id.infoView);
        String[] list = new String[AGE_LIMIT];
        for (int i = 0; i < AGE_LIMIT; i++){
            list[i] = Integer.toString(i);
        }
        wheelView.setData(Arrays.asList(list));
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float v, float v1) {

            }

            @Override
            public void onWheelSelected(int i, String s) {
                pet.setAge((double) i);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });


    }
}
