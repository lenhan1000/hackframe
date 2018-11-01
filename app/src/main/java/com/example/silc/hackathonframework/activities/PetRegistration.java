package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.aigestudio.wheelpicker.view.WheelStraightPicker;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Pet;
import com.example.silc.hackathonframework.models.User;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class PetRegistration extends BaseActivityLoggedIn implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.PetRegistration";
    private static final boolean DEBUG = true;
    private static final Integer AGE_LIMIT = 30;
    private Pet pet;
    private String catBreedRoute;
    private String dogBreedRoute;
    private String createRoute;

    private FrameLayout mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_registration);

        catBreedRoute = getString(R.string.api_pet_cat_breed);
        dogBreedRoute = getString(R.string.api_pet_dog_breed);
        createRoute = getString(R.string.api_pet_create);

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
                if(id == createRoute){
                    startActivity(new Intent(getApplicationContext(), PetList.class));
                    finish();
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
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelSelected(int i, String s) {
                pet.setBreed(s);
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
                inflate_spay();
            }
        });
        mContentFrame.findViewById(R.id.buttonFemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setGender(false);
                inflate_spay();
            }
        });
    }

    private void inflate_spay(){
        mContentFrame.removeAllViews();;
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_spayed,
                mContentFrame);
        mContentFrame.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setSpayed(true);
                inflate_age();
            }
        });
        mContentFrame.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setSpayed(false);
                inflate_age();
            }
        });
    }

    private void inflate_age(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_age,
                mContentFrame);
        WheelCurvedPicker wheelView = mContentFrame.findViewById(R.id.infoView);
        String[] list = new String[AGE_LIMIT];
        for (int i = 0; i < AGE_LIMIT; i++){
            list[i] = Integer.toString(i);
        }
        wheelView.setData(Arrays.asList(list));
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelSelected(int i, String s) {
                pet.setAge((double) i);
            }
        });
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate_weight();
            }
        });

    }

    private void inflate_weight(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_weight,
                mContentFrame);
        final HorizontalWheelView wheelView = mContentFrame.findViewById(R.id.infoView);
        final TextView weight = mContentFrame.findViewById(R.id.weight);
        wheelView.setOnlyPositiveValues(true);
        weight.setText("0.00 lbs");
        wheelView.setListener(new HorizontalWheelView.Listener(){
            @Override
            public void onRotationChanged(double radians){
                String w = String.format("%.2f lbs", wheelView.getDegreesAngle());
                weight.setText(w);
            }
        });
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setWeight(wheelView.getDegreesAngle());
                inflate_birth();
            }
        });
    }

    private void inflate_birth(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.activity_pet_registration_birth,
                mContentFrame);
        final DatePicker dateView = mContentFrame.findViewById(R.id.infoView);
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new GregorianCalendar(dateView.getYear(),
                        dateView.getMonth(),
                        dateView.getDayOfMonth())
                        .getTime();
                pet.setBirth(date);
                register();
            }
        });
    }

    private void register(){
        JSONObject petjs = pet.toJSON();
        Http2Request req = new Http2Request(context);
        req.post(getString(R.string.api_base_url),
                getString(R.string.api_pet_create),
                petjs.toString(),
                User.getToken(context));
    }
}
