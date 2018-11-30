package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
            getWindow().setExitTransition(null);
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
                    return true;
                }
                case R.id.navigation_recording: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(AppBarActivity.this, p1, p2);
                    startActivity(new Intent(context, PendantControll.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_more: {
                    return true;
                }
            }
            return false;
        }
    };

    protected Toolbar.OnMenuItemClickListener menuItemClickListener =
            new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.appbar_reload:{
                            return true;
                        }
                        case R.id.more: {
                            appBarBinding.drawerLayout.openDrawer(GravityCompat.START);
                            return true;
                        }
                    }
                    return AppBarActivity.super.onOptionsItemSelected(menuItem);
                }
            };

    protected DrawerLayout.DrawerListener drawerListener =
            new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float v) {

                }

                @Override
                public void onDrawerOpened(@NonNull View view) {

                }

                @Override
                public void onDrawerClosed(@NonNull View view) {

                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            };

    protected NavigationView.OnNavigationItemSelectedListener navigationListener =
            (MenuItem menuItem) -> {
                menuItem.setChecked(true);
                appBarBinding.drawerLayout.closeDrawers();
                switch(menuItem.getItemId()){
                    case R.id.logout:{
                        Utils.clearSharedPreferences(this, getString(R.string.user_preference));
                        Utils.clearSharedPreferences(this, getString(R.string.pet_preference));
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                    case R.id.settings:{
                        mContentFrame.removeAllViews();
                        LayoutInflater.from(AppBarActivity.this).inflate(R.layout.activity_settings, mContentFrame);
                        findViewById(R.id.button2).setOnClickListener(AppBarActivity.this);
                    }
                }
                return true;
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dashboard_appbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);
        appBarBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_bar);
        actionBar = appBarBinding.actionBar;
        navigation = appBarBinding.navigation;
        mContentFrame = appBarBinding.content;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSupportActionBar(actionBar);
        actionBar.setOnMenuItemClickListener(menuItemClickListener);
        appBarBinding.drawerLayout.addDrawerListener(drawerListener);
        appBarBinding.navDrawer.setNavigationItemSelectedListener(navigationListener);
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

}
