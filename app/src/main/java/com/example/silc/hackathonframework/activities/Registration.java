package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;


import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;
import com.example.silc.hackathonframework.helpers.GeographyDialogWrapper;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.SingleChoiceDialogWrapper;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.*;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registration extends AppCompatActivity implements SingleChoiceDialogFragment.NoticeDialogListener,
        View.OnClickListener, Http2Request.Http2RequestListener{
    private static final String TAG = "activities.Registration";
    private static final boolean DEBUG = false;
    private final Context context = this;
    private int dialog_id;
    private User user;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mDisplayNameField;
    private EditText mMobileField;
    private EditText mCarrierField;
    private EditText mAddressField;
    private EditText mZipCodeField;
    private CountryCodePicker mCountryCodeField;
    private FrameLayout mContentFrame;

    private String registerUrl = "/users";
    private String loginUrl = "/users/login";
    private Http2Request req;

    private TextView mCountryList;
    private TextView mStateList;
    private TextView mCityList;

    //Wrapper
    private GeographyDialogWrapper geoWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Variables
        user = new User();

        //Requests

        req = new Http2Request(this);
        mContentFrame = findViewById(R.id.view);
        inflate_basic();

        geoWrapper = new GeographyDialogWrapper(this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //IMPLEMENT FUNCTIONS

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.signUpButton){
            createAccount(mEmailField.getText().toString(),
                    mPasswordField.getText().toString());
        }
        if (i == R.id.country){
            dialog_id = R.id.country;
            geoWrapper.popCountryDialog();

        }if (i == R.id.state) {
            dialog_id = R.id.state;
            if (geoWrapper.country_id > 0)
                geoWrapper.popStateDialog();
            else
                Toast.makeText(Registration.this, "Select Country First",
                        Toast.LENGTH_SHORT).show();
        }if (i == R.id.city) {
            dialog_id = R.id.city;
            if(geoWrapper.state_id > 0 || geoWrapper.country_id > 0)
                geoWrapper.popCityDialog();
            else
                Toast.makeText(Registration.this, "Select Country or State First",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog){
        if (dialog_id == R.id.country){
            mCountryList.setText(dialog);
            geoWrapper.country_id = id + 1;
        }else if (dialog_id == R.id.state){
            mStateList.setText(dialog);
            geoWrapper.state_id = State.stateId(geoWrapper.states, dialog);
        }else if (dialog_id == R.id.city){
            mCityList.setText(dialog);
        }
    }

    //INFLATE FUNCTIONS

    private void inflate_basic(){
        LayoutInflater.from(this).inflate(R.layout.activity_registration_basic,
                mContentFrame);

        //Views
        mDisplayNameField = mContentFrame.findViewById(R.id.dname);
        mCountryCodeField = mContentFrame.findViewById(R.id.countryCode);
        mMobileField = mContentFrame.findViewById(R.id.mobile);
        mCarrierField = mContentFrame.findViewById(R.id.carrier);

        //Buttons
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "nextButtonPressed");
                if(!validateFormBasic()){
                    return;
                }
                Log.d(TAG, "nextViewInit");
                //Construct a User object
                user.setDisplayName(mDisplayNameField.getText().toString());
                user.setCountryCode(mCountryCodeField.getSelectedCountryCode());
                user.setmobilePhone(mMobileField.getText().toString());
                user.setCarrier(mCarrierField.getText().toString());
                inflate_address();
            }
        });

    }

    private void inflate_address(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.activity_registration_address,
                mContentFrame);

        //Views
        mCountryList = mContentFrame.findViewById(R.id.country);
        mCityList = mContentFrame.findViewById(R.id.city);
        mStateList = mContentFrame.findViewById(R.id.state);
        mAddressField = mContentFrame.findViewById(R.id.address);
        mZipCodeField = mContentFrame.findViewById(R.id.zipcode);

        //Button
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(this);
        mCountryList.setOnClickListener(this);
        mStateList.setOnClickListener(this);
        mCityList.setOnClickListener(this);
        //Buttons
        mContentFrame.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "nextButtonPressed");
                if(!validateFormAddress()){
                    return;
                }
                Log.d(TAG, "nextViewInit");
                //Construct a User object
                user.setAddress(mAddressField.getText().toString());
                user.setCountry(mCountryList.getText().toString());
                user.setState(mStateList.getText().toString());
                user.setCity(mCityList.getText().toString());
                user.setZipCode(mZipCodeField.getText().toString());
                inflate_cred();
            }
        });
    }

    private void inflate_cred(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.activity_registration_cred,
                mContentFrame);

        //Views
        mEmailField = mContentFrame.findViewById(R.id.email);
        mPasswordField = mContentFrame.findViewById(R.id.password);

        //Buttons
        mContentFrame.findViewById(R.id.signUpButton).setOnClickListener(this);
    }

    //VALIDATE FUNCTIONS

    private boolean validateFormBasic() {
        boolean valid = true;

        String displayName = mDisplayNameField.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            mDisplayNameField.setError("Required.");
            valid=false;
        }

        String mobile = mMobileField.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            mMobileField.setError("Required.");
            valid=false;
        }

        String carrier = mCarrierField.getText().toString();
        if (TextUtils.isEmpty(carrier)) {
            mCarrierField.setError("Required.");
            valid=false;
        }

        return DEBUG||valid;
    }

    private boolean validateFormAddress(){
        boolean valid = true;
        String address = mAddressField.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddressField.setError("Required.");
            valid=false;
        }

        String country = mCountryList.getText().toString();
        if (TextUtils.isEmpty(country)) {
            mCountryList.setError("Required.");
            valid=false;
        }

        String city = mCityList.getText().toString();
        if (TextUtils.isEmpty(city)) {
            mCityList.setError("Required.");
            valid=false;
        }

        String state = mStateList.getText().toString();
        if (TextUtils.isEmpty(state)) {
            mStateList.setError("Required.");
            valid=false;
        }

        String zipCode = mZipCodeField.getText().toString();
        if (TextUtils.isEmpty(zipCode)) {
            mZipCodeField.setError("Required.");
            valid=false;
        }else if(!Utils.isValidZipCode(zipCode)){
            mZipCodeField.setError("Invalid Zip.");
            valid = false;
        }

        return DEBUG||valid;
    }

    private boolean validateFormCred() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else if (!Utils.isValidEmail(email)){
            mEmailField.setError("Invalid email.");
            valid = false;
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else if(!Utils.isValidPassword(password)){
            mPasswordField.setError("Weak password.");
            valid = false;
        }

        return DEBUG||valid;
    }

    //HELPER FUNCTIONS


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateFormCred()) return;

        user.setEmail(email);
        JSONObject regUser = user.toJSON();
        Utils.addtoJSON(regUser, "password", password);
        Http2Request regRequest = new Http2Request(this);
        regRequest.post(getString(R.string.api_base_url), registerUrl, regUser.toString());
    }

    public void login(final String email, String password) {
        String json = Http2Request.registerUserJson(email, password);
        req.post(getString(R.string.api_base_url), loginUrl, json);
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try {
            boolean success = res.getBoolean("success");
            if (!success) Log.d(TAG, res.getString("message"));
            else {
                if (id == registerUrl)
                    login(user.getEmail(), mPasswordField.getText().toString());
                else {
                    User.processLogin(user.getEmail(), res.getString("token"), Registration.this);
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }

        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

}