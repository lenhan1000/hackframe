package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityAppBarBinding;
import com.example.silc.hackathonframework.helpers.Utils;

public abstract class AppBarActivity extends BaseActivityLoggedIn implements View.OnClickListener{
    private static final String TAG = "activities.AppBar";
    protected Toolbar actionBar;
    protected FrameLayout mContentFrame;
    protected BottomNavigationView navigation;
    protected ActivityAppBarBinding appBarBinding;
    protected BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(AppBarActivity.this, p1, p2);
                    startActivity(new Intent(context, Profile.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_pets: {
                    Log.d(TAG,"HERE");
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(AppBarActivity.this, p1, p2);
                    startActivity(new Intent(context, PetList.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_dashboard:{
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(AppBarActivity.this, p1, p2);
                    startActivity(new Intent(context, Dashboard.class), options.toBundle());
                    finishAfterTransition();
                    return true;
                }
                case R.id.navigation_settings: {
                    mContentFrame.removeAllViews();
                    LayoutInflater.from(AppBarActivity.this).inflate(R.layout.activity_settings, mContentFrame);

                    findViewById(R.id.button2).setOnClickListener(AppBarActivity.this);
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        appBarBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_bar);
        actionBar = appBarBinding.actionBar;
        navigation = appBarBinding.navigation;
        mContentFrame = appBarBinding.content;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSupportActionBar(actionBar);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button2:
                Log.e(TAG,"HERE");
                Utils.clearSharedPreferences(this, getString(R.string.user_preference));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return;
        }
    }

}
