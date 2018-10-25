package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.example.silc.hackathonframework.databinding.ActivityProfileBinding;
import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;
import com.example.silc.hackathonframework.helpers.GeographyDialogWrapper;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.State;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppBarActivity implements View.OnClickListener,
        Http2Request.Http2RequestListener, SingleChoiceDialogFragment.NoticeDialogListener{
    private static final String TAG = "activities.Profile";
    private static final boolean DEBUG = false;
    private FloatingActionButton fab;
    private boolean boolFab;
    private int dialog_id;
    private User user;
    private Context context;

    private TextInputEditText mEmail;
    private TextInputEditText mDisplayName;
    private TextInputEditText mAddress;
    private TextView mCity;
    private TextView mState;
    private TextView mCountry;
    private TextInputEditText mZipCode;
    private GeographyDialogWrapper geoDialog;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_profile,
                mContentFrame,
                true);
        context = this;
        boolFab = false;
        user = new User();
        fab = binding.editFloatingBtn;
        fab.setOnClickListener(this);
        navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);

        //Info Views
        mDisplayName = binding.displayName;
        mEmail = binding.email;
        mAddress = binding.address;
        mCity = binding.city;
        mState = binding.state;
        mCountry = binding.country;
        mZipCode = binding.zipCode;


        //Set onClick
        mCountry.setOnClickListener(this);
        mState.setOnClickListener(this);
        mCity.setOnClickListener(this);

        //Wrappers
        geoDialog = new GeographyDialogWrapper(context);

        User.getUserInfo(context);
    }

    protected void onResume(){
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
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
                    try {
                        saveUser();
                    }catch(JSONException e){
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        Toast.makeText(context, "An error occured",
                                Toast.LENGTH_SHORT).show();
                    }
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
                    dialog_id = R.id.city;
                    geoDialog.popCityDialog();
                }
        }
    }

    @Override
    public void onRequestFinished(String id, JSONObject body){
        if (id.equals(getString(R.string.api_user_info))){
            try {
                final String msg = body.getString("msg");
                if (body.getBoolean("success")) {
                    JSONObject info = body.getJSONObject("msg");
                    loadInfo(info);
                }else{
                    Log.e(TAG, body.getString("msg"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }catch (JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "An error occured",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(id.equals(getString(R.string.api_user_update))) {
            try {
                final String msg = body.getString("msg");
                if (body.getBoolean("success")) {
                    saveUserUiUpdate();
                } else {
                    Log.e(TAG, body.getString("msg"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, msg,
                                    Toast.LENGTH_SHORT).show();                        }
                    });
                }
            }catch (JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "An error occurred",
                                Toast.LENGTH_SHORT).show();                    }
                });
            }
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog){
        try {
            if (dialog_id == R.id.country) {
                if(!user.getCountry().getString("name").equals(dialog)){
                    mCountry.setText(dialog);
                    geoDialog.country_id = id + 1;
                    mState.setText("Select");
                    mCity.setText("Select");
                }
            } else if (dialog_id == R.id.state) {
                if(!user.getCountry().getString("name").equals(dialog)) {
                    mState.setText(dialog);
                    geoDialog.state_id = State.stateId(geoDialog.states, dialog);
                    mCity.setText("Select");
                }
            } else if (dialog_id == R.id.city) {
                mCity.setText(dialog);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void loadInfo(JSONObject u) throws JSONException{
        final JSONObject info = u;
        final String email = Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference));
        user.setEmail(email);
        //Set up user object with info
        user.setDisplayName(info.getString("displayName"));
        user.setAddress(info.getString("address"));
        user.setCity(info.getString("city"));
        user.setZipCode(info.getString("zipCode"));
        user.setCountry(info.getString("country"));
        user.setState(info.getString("state"));
        geoDialog.state_id = user.getState().getInt("id");
        geoDialog.country_id = user.getCountry().getInt("id");
        //Run thread to change info on UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmail.setText(email);
                try {
                    mDisplayName.setText(user.getDisplayName());
                    mAddress.setText(user.getAddress());
                    mCity.setText(user.getCity());
                    mState.setText(user.getState().getString("name"));
                    mCountry.setText(user.getCountry().getString("name"));
                    mZipCode.setText(user.getZipCode());
                }catch(JSONException e) {
                    Log.e(TAG, "Json parsing err: " + e.getMessage());
                }
            }
        });
    }

    private void saveUser() throws JSONException{
        if (!validate()){ return; }
        user.setDisplayName(mDisplayName.getText().toString());
        user.setAddress(mAddress.getText().toString());
        user.setCity(mCity.getText().toString());
        user.setState(geoDialog.state_id,
                mState.getText().toString());
        user.setCountry(geoDialog.country_id,
                mCountry.getText().toString());
        user.setZipCode(mZipCode.getText().toString());
        user.setEmail(Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference)));
        JSONObject body = new JSONObject();
        body.put("info", user.toJSON());
        Http2Request req = new Http2Request(context);
        req.put(getString(R.string.api_base_url), getString(R.string.api_user_update),
                User.getToken(context), body.toString());
    }

    private void saveUserUiUpdate(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.editFloatingBtn).clearAnimation();
                fab.setImageResource(R.drawable.ic_edit_pastel_64dp);
                mDisplayName.setFocusable(false);
                mEmail.setFocusable(false);
                mAddress.setFocusable(false);
                mZipCode.setFocusable(false);
                boolFab = false;
                Toast.makeText(context, "Saved Successful",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(){
        boolean valid = true;

        String displayName = mDisplayName.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            mDisplayName.setError("Required.");
            valid=false;
        }

//        String mobile = mMobile.getText().toString();
//        if (TextUtils.isEmpty(mobile)) {
//            mMobile.setError("Required.");
//            valid=false;
//        }
//
//        String carrier = mCarrier.getText().toString();
//        if (TextUtils.isEmpty(carrier)) {
//            mCarrier.setError("Required.");
//            valid=false;
//        }
        String address = mAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddress.setError("Required.");
            valid=false;
        }

        String country = mCountry.getText().toString();
        if (TextUtils.isEmpty(country)) {
            mCountry.setError("Required.");
            valid=false;
        }

        String city = mCity.getText().toString();
        if (city.equals("Select")) {
            mCity.setError("Required.");
            valid=false;
        }

        String state = mState.getText().toString();
        if (state.equals("Select")) {
            mState.setError("Required.");
            valid=false;
        }

        String zipCode = mZipCode.getText().toString();
        if (TextUtils.isEmpty(zipCode)) {
            mZipCode.setError("Required.");
            valid=false;
        }else if(!Utils.isValidZipCode(zipCode)){
            mZipCode.setError("Invalid Zip.");
            valid = false;
        }

        return DEBUG||valid;
    }

}
