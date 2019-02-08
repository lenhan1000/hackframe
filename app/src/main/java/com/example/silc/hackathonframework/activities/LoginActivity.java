package com.example.silc.hackathonframework.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.text.Html;
import android.widget.Toast;


import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.ClientUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener, Http2Request.Http2RequestListener{
    private static final String TAG = "Login";
    private static final String loginUrl = "/users/login";
    private EditText mEmailField;
    private EditText mPasswordField;
    private String email;

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
        regLink.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                runOnUiThread(() ->  Toast.makeText(context, "Unsuccessful Login",
                                Toast.LENGTH_SHORT).show()
                );

                Log.d(TAG, res.getString("msg"));
            } else {
                ClientUser.processLogin(email, res.getString("token"), this);
                ((App) getApplication()).setDefaultPet();
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }
}
