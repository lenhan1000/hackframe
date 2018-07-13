package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;


import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.fragments.SingleChoiceDiaglogFragment;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Country;
import com.example.silc.hackathonframework.models.User;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Registration";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mDisplayNameField;
    private EditText mMobileField;
    private EditText mCarrierField;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView mCountryList;
    private ArrayList<Country> countries;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Views
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mDisplayNameField = findViewById(R.id.dname);
        mMobileField = findViewById(R.id.mobile);
        mCarrierField = findViewById(R.id.carrier);
        mCountryList = findViewById(R.id.country);

        //Buttons
        findViewById(R.id.signUpButton).setOnClickListener(this);
        mCountryList.setOnClickListener(this);
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userauth = mAuth.getCurrentUser();
                            User user = new User(mDisplayNameField.getText().toString(),
                                    mMobileField.getText().toString(),
                                    mCarrierField.getText().toString());
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            mDatabase.child(userauth.getUid()).setValue(user);

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
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

//        String displayName = mDisplayNameField.getText().toString();
//        if (TextUtils.isEmpty(displayName)) {
//            mDisplayNameField.setError("Required.");
//            valid = false;
//        } else {
//            mDisplayNameField.setError(null);
//        }
//
//        String mobile = mMobileField.getText().toString();
//        if (TextUtils.isEmpty(password)) {
//            mMobileField.setError("Required.");
//            valid = false;
//        } else {
//            mMobileField.setError(null);
//        }
//
//        String carrier = mCarrierField.getText().toString();
//        if (TextUtils.isEmpty(password)) {
//            mCarrierField.setError("Required.");
//            valid = false;
//        } else {
//            mCarrierField.setError(null);
//        }

        return valid;
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.signUpButton){
            createAccount(mEmailField.getText().toString(),mPasswordField.getText().toString());
        }
        if (i == R.id.country){
            String jsonStr = Utils.loadJSONFromAsset(this, "countries.json");
            try {
                countries = Country.jsonToCountry(Utils.getArrayListFromJSONArray(new JSONObject(jsonStr).getJSONArray("countries")));
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
            ArrayList<String> countries_name = Country.listCountry(countries);
            SingleChoiceDiaglogFragment dialog = new SingleChoiceDiaglogFragment();
            Bundle data = new Bundle();
            data.putStringArrayList("list", countries_name);
            data.putString("title", "Select a Country");
            dialog.setArguments(data);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialog.show(ft,null);

        }
    }



}
