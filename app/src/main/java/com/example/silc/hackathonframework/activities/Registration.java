package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;


import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.fragments.RegistrationAddress;
import com.example.silc.hackathonframework.fragments.RegistrationBasic;
import com.example.silc.hackathonframework.fragments.RegistrationCred;
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

import java.io.Serializable;
import java.util.ArrayList;

public class Registration extends AppCompatActivity implements
        RegistrationBasic.OnFragmentInteractionListener, RegistrationAddress.OnFragmentInteractionListener{
    private static final String TAG = "Registration";
    private User user;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mDisplayNameField;
    private EditText mMobileField;
    private EditText mCarrierField;
    private CountryCodePicker mCountryCode;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        user = new User();

        RegistrationBasic fragmentBasic = RegistrationBasic.newInstance(this.user);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, fragmentBasic)
                .commit();
        //Variables

//        country_id = 0;
//        state_id = 0;
//
//
//        //Views
//        mCountryCode = findViewById(R.id.countryCode);
//        mEmailField = findViewById(R.id.email);
//        mPasswordField = findViewById(R.id.password);
//        mDisplayNameField = findViewById(R.id.dname);
//        mMobileField = findViewById(R.id.mobile);
//        mCarrierField = findViewById(R.id.carrier);
//        mAddressField = findViewById(R.id.address);
//        mCountryList = findViewById(R.id.country);
//        mStateList = findViewById(R.id.state);
//        mCityList = findViewById(R.id.city);
//        mZipCodeField = findViewById(R.id.zipcode);
//
//        //Buttons
//        findViewById(R.id.signUpButton).setOnClickListener(this);
//        mCountryList.setOnClickListener(this);
//        mStateList.setOnClickListener(this);
//        mCityList.setOnClickListener(this);
//
//        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in ic_user_black_24dp's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser userauth = mAuth.getCurrentUser();
//                            User user = new User(mDisplayNameField.getText().toString(),
//                                    mMobileField.getText().toString(),
//                                    mCarrierField.getText().toString(),
//                                    mZipCodeField.getText().toString(),
//                                    mAddressField.getText().toString(),
//                                    mCountryList.getText().toString(),
//                                    mStateList.getText().toString(),
//                                    mCityList.getText().toString(),
//                                    mCountryCode.getSelectedCountryCode().toString()
//                                    );
//                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
//                            mDatabase.child(userauth.getUid()).setValue(user);
//
//                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the ic_user_black_24dp.
//                            try {
//                                throw task.getException();
//                            }catch (FirebaseAuthWeakPasswordException e){
//                                Toast.makeText(Registration.this, "Weak Password",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(FirebaseAuthInvalidCredentialsException e){
//                                Toast.makeText(Registration.this, "Invalid Email",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(FirebaseAuthUserCollisionException e){
//                                Toast.makeText(Registration.this, "Email already used",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(Exception e){
//                                Log.e(TAG, e.getMessage());
//                            }
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//
//                        }
//
//                    }
//                });
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
    public void onFragmentInteraction(String id, User user){
        this.user = user;
        if (id == "RegistrationBasic"){
            RegistrationAddress fragmentBasic = RegistrationAddress.newInstance(this.user);
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragment))
                    .add(R.id.fragment, fragmentBasic)
                    .commit();
        } else if(id == "RegistrationAddress"){
            RegistrationAddress fragmentBasic = RegistrationAddress.newInstance(this.user);
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragment))
                    .add(R.id.fragment, fragmentBasic)
                    .commit();
        }
    }


}
