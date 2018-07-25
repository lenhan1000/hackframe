package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registration extends AppCompatActivity implements SingleChoiceDialogFragment.NoticeDialogListener, View.OnClickListener{
    private static final String TAG = "activities.Registration";
    private static final boolean DEBUG = false;

    private int dialog_id;
    private int country_id;
    private int state_id;
    private ArrayList<State> states;
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

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView mCountryList;
    private TextView mStateList;
    private TextView mCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Variables
        country_id = 0;
        state_id = 0;
        user = new User();


        mContentFrame = findViewById(R.id.view);
        inflate_basic();

        mAuth = FirebaseAuth.getInstance();
    }

    //IMPLEMENT FUNCTIONS

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.signUpButton){
            createAccount(mEmailField.getText().toString(),mPasswordField.getText().toString());
        }
        if (i == R.id.country){
            dialog_id = R.id.country;
            pop_country_dialog();

        }if (i == R.id.state) {
            dialog_id = R.id.state;
            if (country_id > 0)
                pop_state_dialog(country_id);
            else
                Toast.makeText(Registration.this, "Select Country First",
                        Toast.LENGTH_SHORT).show();
        }if (i == R.id.city) {
            dialog_id = R.id.city;
            if(state_id > 0 || country_id > 0)
                pop_city_dialog(state_id);
            else
                Toast.makeText(Registration.this, "Select Country or State First",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog){
        if (dialog_id == R.id.country){
            mCountryList.setText(dialog);
            country_id = id + 1;
            Log.d(TAG, "Country ID: " + Integer.toString(country_id));
        }else if (dialog_id == R.id.state){
            mStateList.setText(dialog);
            state_id = State.stateId(states, dialog);
            Log.d(TAG, "State ID: " + Integer.toString(state_id));
        }else if (dialog_id == R.id.city){
            mCityList.setText(dialog);
        }
    }

    //DIALOG FUNCTIONS

    private void pop_country_dialog(){
        ArrayList<Country> countries;
        String jsonStr = Utils.loadJSONFromAsset(this, "countries.json");
        try {
            countries = Country.jsonToCountry(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("countries")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return;
        }
        ArrayList<String> countries_name = Country.toArrayStrings(countries);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", countries_name);
        data.putString("title", "Select a Country");
        dialog.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }

    private void pop_state_dialog(int cid){
        String jsonStr = Utils.loadJSONFromAsset(this, "states.json");
        try {
            states = State.jsonToState(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("states")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return;
        }
        states = State.countryToStates(states, cid);
        ArrayList<String> states_name = State.toArrayStrings(states);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", states_name);
        data.putString("title", "Select a State/Region");
        dialog.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }

    private void pop_city_dialog(int sid){
        ArrayList<City> cities;
        String jsonStr = Utils.loadJSONFromAsset(this, "cities.json");
        try {
            cities = City.jsonToCity(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("cities")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return;
        }
        cities = City.stateToCities(cities, sid);
        ArrayList<String> cities_name = City.toArrayStrings(cities);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", cities_name);
        data.putString("title", "Select a City");
        dialog.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }

    //INFLATE FUNCTIONS

    private void inflate_basic(){
        LayoutInflater.from(this).inflate(R.layout.frame_registration_basic,mContentFrame);

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
                user.setmPhone(mMobileField.getText().toString());
                user.setCarrier(mCarrierField.getText().toString());
                inflate_address();
            }
        });

    }

    private void inflate_address(){
        mContentFrame.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.frame_registration_address, mContentFrame);

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
        LayoutInflater.from(this).inflate(R.layout.frame_registration_cred, mContentFrame);

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
        if (!validateFormCred()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in ic_user_black_24dp's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userauth = mAuth.getCurrentUser();
                            User user = new User(mDisplayNameField.getText().toString(),
                                    mMobileField.getText().toString(),
                                    mCarrierField.getText().toString(),
                                    mZipCodeField.getText().toString(),
                                    mAddressField.getText().toString(),
                                    mCountryList.getText().toString(),
                                    mStateList.getText().toString(),
                                    mCityList.getText().toString(),
                                    mCountryCodeField.getSelectedCountryCode()
                            );
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            try {
                                mDatabase.child(userauth.getUid()).setValue(user);
                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(intent);
                            }catch (NullPointerException e){
                                Log.e(TAG, "Null user");

                            }

                        } else {
                            // If sign in fails, display a message to the ic_user_black_24dp.
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                Toast.makeText(Registration.this, "Weak Password",
                                        Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                Toast.makeText(Registration.this, "Invalid Email",
                                        Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthUserCollisionException e){
                                Toast.makeText(Registration.this, "Email already used",
                                        Toast.LENGTH_SHORT).show();
                            }catch(Exception e){
                                Log.e(TAG, e.getMessage());
                            }
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }

                    }
                });
        // [END create_user_with_email]
    }
//
//    private void update_user(HashMap kv){
//        Iterator it = kv.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry pair = (Map.Entry) it.next();
//            this.user.setStringKey(pair.getKey().toString(),pair.getValue().toString());
//            it.remove();
//        }
//    }

}