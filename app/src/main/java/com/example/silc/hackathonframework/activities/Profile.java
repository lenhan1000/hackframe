package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;
import com.example.silc.hackathonframework.helpers.GeographyDialogWrapper;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.State;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity implements View.OnClickListener,
        Http2Request.Http2RequestListener, SingleChoiceDialogFragment.NoticeDialogListener{
    private static final String TAG = "activities.Profile";
    private BottomNavigationView navigation;
    private FloatingActionButton fab;
    private boolean boolFab;
    private int dialog_id;
    private User user;
    private Context context;

    private Toolbar myToolbar;
    private EditText mEmail;
    private EditText mDisplayName;
    private EditText mAddress;
    private TextView mCity;
    private TextView mState;
    private TextView mCountry;
    private EditText mZipCode;
    private GeographyDialogWrapper geoDialog;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_dashboard:

                    Pair<View, String> p1 = Pair.create((View) myToolbar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(Profile.this, p1, p2);
                    startActivity(new Intent(context, Dashboard.class), options.toBundle());

                    return true;
                case R.id.navigation_pets:
                    return true;
                case R.id.navigation_settings:
                    return true;
                case R.id.navigation_more:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        boolFab = false;
        user = new User();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_profile);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        fab = findViewById(R.id.editFloatingBtn);
        fab.setOnClickListener(this);

        //Info Views
        mDisplayName = findViewById(R.id.displayName);
        mEmail = findViewById(R.id.email);
        mAddress = findViewById(R.id.address);
        mCity = findViewById(R.id.city);
        mState = findViewById(R.id.state);
        mCountry = findViewById(R.id.country);
        mZipCode = findViewById(R.id.zipCode);

        //Set onClick
        mCountry.setOnClickListener(this);
        mState.setOnClickListener(this);
        mCity.setOnClickListener(this);

        //Wrappers
        geoDialog = new GeographyDialogWrapper(context);

        User.getUserInfo(context);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.editFloatingBtn:
                if (!boolFab) {
                    v.clearAnimation();
                    fab.setImageResource(R.drawable.ic_save_pastel_64dp);
                    mDisplayName.setFocusableInTouchMode(true);
                    mEmail.setFocusableInTouchMode(true);
                    mAddress.setFocusableInTouchMode(true);
                    mZipCode.setFocusableInTouchMode(true);
                    boolFab = true;
                    Toast.makeText(context, "Select to Edit",
                            Toast.LENGTH_SHORT).show();
                } else {
                    v.clearAnimation();
                    fab.setImageResource(R.drawable.ic_edit_pastel_64dp);
                    mDisplayName.setFocusable(false);
                    mEmail.setFocusable(false);
                    mAddress.setFocusable(false);
                    mZipCode.setFocusable(false);
                    boolFab = false;
                    saveUser();
                 }
                break;
            case R.id.country:
                if(boolFab) {
                    dialog_id = R.id.country;
                    geoDialog.popCountryDialog();
                }
                break;
            case R.id.state:
                if(boolFab) {
                    dialog_id = R.id.state;
                    geoDialog.popStateDialog();
                }
                break;
            case R.id.city:
                if(boolFab) {
                    geoDialog.popCityDialog();
                }
        }
    }

    @Override
    public void onRequestFinished(String id, JSONObject body){
        if (id.equals(getString(R.string.api_user_info))){
            try {
                if (body.getBoolean("success")) {
                    JSONObject info = body.getJSONObject("message");
                    loadInfo(info);
                }else{
                    Log.e(TAG, body.getString("message"));
                    Toast.makeText(context, body.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                Toast.makeText(context, "An error occurred",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(id.equals(getString(R.string.api_user_update))) {
            try {
                if (body.getBoolean("success")) {
                    Toast.makeText(context, "Saved Successful",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, body.getString("message"));
                    Toast.makeText(context, body.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                Toast.makeText(context, "An error occurred",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog){
        if (dialog_id == R.id.country){
            mCountry.setText(dialog);
            geoDialog.country_id = id + 1;
        }else if (dialog_id == R.id.state){
            mState.setText(dialog);
            geoDialog.state_id = State.stateId(geoDialog.states, dialog);
        }else if (dialog_id == R.id.city){
            mCity.setText(dialog);
        }
    }

    public void loadInfo(JSONObject u){
        final JSONObject info = u;
        final String email = Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmail.setText(email);
                try {
                    mDisplayName.setText(info.getString("displayName"));
                    mAddress.setText(info.getString("address"));
                    mCity.setText(info.getString("city"));
                    mState.setText(info.getString("state"));
                    mCountry.setText(info.getString("country"));
                    mZipCode.setText(info.getString("zipCode"));
                }catch(JSONException e) {
                    Log.e(TAG, "Json parsing err: " + e.getMessage());
                }
            }
        });
    }

    private void saveUser(){
        validate();
        user.setDisplayName(mDisplayName.getText().toString());
        user.setAddress(mAddress.getText().toString());
        user.setCity(mCity.getText().toString());
        user.setState(mState.getText().toString());
        user.setCountry(mCountry.getText().toString());
        user.setZipCode(mZipCode.getText().toString());
        user.setEmail(Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference)));
        Http2Request req = new Http2Request(context);
        req.put(getString(R.string.api_base_url), getString(R.string.api_user_update),
                User.getToken(context),user.toJSON().toString());
    }

    private void validate(){
        //TODO
    }

}
