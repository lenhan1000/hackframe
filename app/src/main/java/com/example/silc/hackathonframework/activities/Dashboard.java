package com.example.silc.hackathonframework.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import android.widget.TextView;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, Http2Request.Http2RequestListener{
    private static final String TAG = "activities.Dashboard";
    private ConstraintLayout mContentFrame;
    private Context context = this;
    private BottomNavigationView navigation;
    private Toolbar actionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(Dashboard.this, p1, p2);
                    startActivity(new Intent(context, Profile.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_pets: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(Dashboard.this, p1, p2);
                    startActivity(new Intent(context, PetProfiles.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_settings: {
                    mContentFrame.removeAllViews();
                    LayoutInflater.from(Dashboard.this).inflate(R.layout.activity_settings, mContentFrame);

                    Dashboard.this.findViewById(R.id.button2).setOnClickListener(Dashboard.this);
                    return true;
                }
                case R.id.navigation_more: {
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dashboard_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.appbar_reload:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        actionBar = findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Champ");
        getSupportActionBar().setSubtitle("Last Synced " + Utils.getCurrentTime());
        mContentFrame = findViewById(R.id.content);
        printINFO();
    }

    @Override
    protected void onDestroy(){
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

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button2:
                Utils.clearSharedPreferences(this, getString(R.string.user_preference));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return;
        }
    }

    @Override
    public void onRequestFinished(String id, JSONObject user){
        if (user != null) Log.d(TAG, user.toString());
    }

    private void printINFO(){
        Log.d(TAG,  "WHAT" + Build.VERSION.BASE_OS);
        Log.d(TAG, "SDK INT" + Integer.toString(Build.VERSION.SDK_INT));
        Log.d(TAG, Build.BOARD);
        Log.d(TAG, Build.BRAND);
        Log.d(TAG, Build.BOOTLOADER);
        Log.d(TAG, Build.DEVICE);
        Log.d(TAG, Build.DISPLAY);
        Log.d(TAG, Build.FINGERPRINT);
        Log.d(TAG, Build.HARDWARE);
        Log.d(TAG, Build.HOST);
        Log.d(TAG, Build.ID);
        Log.d(TAG, Build.MANUFACTURER);
        Log.d(TAG, Build.MODEL);
        Log.d(TAG, Build.PRODUCT);
        Log.d(TAG, Build.TAGS);
        Log.d(TAG, Build.TYPE);
        Log.d(TAG, Build.USER);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                App.sendInstanceId(instanceIdResult.getToken());
            }
        });

    }
}
