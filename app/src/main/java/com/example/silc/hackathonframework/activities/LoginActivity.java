package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.text.Html;


import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Http2Request.Http2RequestListener{
    private static final String TAG = "EmailPassword";
    private static final String loginUrl = "/users/login";

    private EditText mEmailField;
    private EditText mPasswordField;
    private String email;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views
        mEmailField = findViewById(R.id.username);
        mPasswordField = findViewById(R.id.password);

        //Buttons
        findViewById(R.id.logInButton).setOnClickListener(this);

        //Registration Direct
        final TextView regLink = findViewById(R.id.signUpLink);
        regLink.setText(Html.fromHtml("<u>Sign Up</u>"));
        regLink.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onStart() {
        super.onStart();

        // Check if ic_user_black_24dp is signed in (non-null) and update UI accordingly.
        if (Utils.containsSharedPreferences(this,
                getString(R.string.user_preference_token),
                getString(R.string.user_preference))){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        this.email = email;
        String json = Http2Request.registerUserJson(email, password);
        Http2Request req = new Http2Request(this);
        req.post(getString(R.string.api_base_url), loginUrl, json);
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

        return valid;
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.logInButton){
            signIn(mEmailField.getText().toString(),mPasswordField.getText().toString());
        }
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try {
            boolean success = res.getBoolean("success");
            if (!success) {
                Log.d(TAG, res.getString("message"));
            } else {
                User.processLogin(email, res.getString("token"), this);

                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }
}
