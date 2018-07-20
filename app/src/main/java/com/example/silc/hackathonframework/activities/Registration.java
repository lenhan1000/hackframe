package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    private static final String TAG = "Registration";
    private int dialog_id;
    private int country_id;
    private int state_id;
    private ArrayList<State> states;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mDisplayNameField;
    private EditText mMobileField;
    private EditText mCarrierField;
    private EditText mAddressField;
    private EditText mZipCodeField;
    private CountryCodePicker mCountryCode;

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


        //Views
        mCountryCode = findViewById(R.id.countryCode);
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mDisplayNameField = findViewById(R.id.dname);
        mMobileField = findViewById(R.id.mobile);
        mCarrierField = findViewById(R.id.carrier);
        mAddressField = findViewById(R.id.address);
        mCountryList = findViewById(R.id.country);
        mStateList = findViewById(R.id.state);
        mCityList = findViewById(R.id.city);
        mZipCodeField = findViewById(R.id.zipcode);

        //Buttons
        findViewById(R.id.signUpButton).setOnClickListener(this);
        mCountryList.setOnClickListener(this);
        mStateList.setOnClickListener(this);
        mCityList.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
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
                                    mCountryCode.getSelectedCountryCode().toString()
                                    );
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            mDatabase.child(userauth.getUid()).setValue(user);

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
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

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String displayName = mDisplayNameField.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            mDisplayNameField.setError("Required.");
            valid = false;
        } else {
            mDisplayNameField.setError(null);
        }

        String mobile = mMobileField.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            mMobileField.setError("Required.");
            valid = false;
        } else {
            mMobileField.setError(null);
        }

        String carrier = mCarrierField.getText().toString();
        if (TextUtils.isEmpty(carrier)) {
            mCarrierField.setError("Required.");
            valid = false;
        } else {
            mCarrierField.setError(null);
        }

        return valid;

//        String countryCode = mCountryCode.getSelectedCountryCode().toString();
//        if (TextUtils.isEmpty(password)) {
//            mCarrierField.setError("Required.");
//            valid = false;
//        } else {
//            mCarrierField.setError(null);
//        }
//
//        return valid;
    }

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

    private void pop_country_dialog(){
        ArrayList<Country> countries = null;
        String jsonStr = Utils.loadJSONFromAsset(this, "countries.json");
        try {
            countries = Country.jsonToCountry(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("countries")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
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
}
