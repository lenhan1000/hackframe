package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import android.widget.TextView;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONObject;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, Http2Request.Http2RequestListener{
    private static final String TAG = "activities.Dashboard";
    private BottomNavigationView bottom;
    private ConstraintLayout mContentFrame;
    private Context context = this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    User.getUserInfo(context);
                    return true;
                case R.id.navigation_pets:
                    return true;
                case R.id.navigation_settings:
                    mContentFrame.removeAllViews();
                    LayoutInflater.from(Dashboard.this).inflate(R.layout.activity_settings, mContentFrame);

                    Dashboard.this.findViewById(R.id.button2).setOnClickListener(Dashboard.this);
                    return true;
                case R.id.navigation_more:
                    return true;
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Champ");
        getSupportActionBar().setSubtitle("Last Synced " + Utils.getCurrentTime());
        mContentFrame = findViewById(R.id.content);
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
}
