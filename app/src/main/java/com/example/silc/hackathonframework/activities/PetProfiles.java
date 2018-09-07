package com.example.silc.hackathonframework.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONObject;

import java.util.ArrayList;

public class PetProfiles extends AppCompatActivity implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.PetProfiles";
    private static final boolean DEBUG = true;
    private Context context = this;
    private BottomNavigationView navigation;
    private FloatingActionButton fab;
    private Toolbar actionBar;
    private LinearLayout contentView;
    private ArrayList<LinearLayout> viewArray;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_dashboard: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(PetProfiles.this, p1, p2);
                    startActivity(new Intent(context, Dashboard.class), options.toBundle());
                    return true;
                }
                case R.id.navigation_profile: {
                    Pair<View, String> p1 = Pair.create((View) actionBar, "appbar");
                    Pair<View, String> p2 = Pair.create((View) navigation, "navigation");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(PetProfiles.this, p1, p2);
                    startActivity(new Intent(context, Profile.class), options.toBundle());
                    return true;
                }
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

    private View.OnClickListener mOnClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.FloatingBtn: {

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profiles);
        //Set up appbar, bottom nag, etc
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_pets);
        actionBar = findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        fab = findViewById(R.id.FloatingBtn);
        fab.setOnClickListener(mOnClickListener);
        contentView = findViewById(R.id.content);
        createPetProfileButton("LMAO");
        createPetProfileButton("4HEAD");
    }

    @Override
    public void onRequestFinished(String id, JSONObject body){

    }

    private void createPetProfileButton(String name){
        //Set up view
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 0);
        layout.setLayoutParams(params);
        layout.setPadding(10, 10, 10,10);
        layout.setBackgroundResource(R.drawable.button_dashboard_standard);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        //Set up picture
        ImageView pic = new ImageView(context);
        pic.setId(1);
        pic.setImageResource(R.drawable.ic_dog_pastel_64dp);
        pic.setForegroundGravity(Gravity.CENTER_VERTICAL);
        layout.addView(pic);

        //Set up text
        TextView text = new TextView(context);
        text.setId(2);
        text.setText(name);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        text.setPadding(10, 10, 10, 10);
        text.setTextSize(30);
        layout.addView(text);


        contentView.addView(layout);
        viewArray.add(layout);
    }

}
