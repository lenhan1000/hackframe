package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityPetProfilesBinding;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Pet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetProfiles extends AppBarActivity implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.PetProfiles";
    private static final boolean DEBUG = true;
    private String petsRoute;
    private FloatingActionButton fab;
    private LinearLayout contentView;
    private ArrayList<LinearLayout> viewArray;
    private ActivityPetProfilesBinding binding;
    private View.OnClickListener mOnClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.FloatingBtn: {
                    startActivity(new Intent(context, PetRegistration.class));
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_profiles,
                mContentFrame,
                true);
        viewArray = new ArrayList<LinearLayout>();
        //Set up appbar, bottom nag, etc
        fab = binding.FloatingBtn;
        fab.setOnClickListener(mOnClickListener);
        contentView = binding.petLayout;
        navigation.getMenu().findItem(R.id.navigation_pets).setChecked(true);
        petsRoute = Pet.getMyPets(context);
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_pets).setChecked(true);
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try {
            boolean success = res.getBoolean("success");
            if (!success) Log.d(TAG, res.getString("msg"));
            else {
                if (id == petsRoute){
                    ArrayList<JSONObject> pets = Utils.getArrayListFromJSONArray(
                            res.getJSONArray("msg")
                    );
                    for (int i = 0; i < pets.size(); i++){
                        runOnUiThread(new PetProfileRunnable(
                                pets.get(i).getString("name")));
                    }
                }
                else {

                }
            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }


    class PetProfileRunnable implements Runnable{
        String name;
        PetProfileRunnable(String name) {this.name = name;}
        public void run(){
            createPetProfileButton(name);
        }
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
        ImageButton pic = new ImageButton(context);
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
