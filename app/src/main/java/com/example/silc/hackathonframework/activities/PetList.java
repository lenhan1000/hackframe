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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityPetListBinding;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Pet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetList extends AppBarActivity implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.PetList";
    private static final boolean DEBUG = true;
    private String petsRoute;
    private FloatingActionButton fab;
    private LinearLayout contentView;
    private ArrayList<LinearLayout> viewArray;
    private LinearLayout selected;
    private ActivityPetListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_list,
                mContentFrame,
                true);
        viewArray = new ArrayList<LinearLayout>();
        //Set up appbar, bottom nag, etc
        fab = binding.FloatingBtn;
        fab.setOnClickListener((View v) -> {
            startActivity(new Intent(context, PetRegistration.class));
            finish();
        });
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
                                pets.get(i).getString("name"),
                                pets.get(i).getString("id"))
                        );
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
        String id;
        PetProfileRunnable(String name, String id) {
            this.name = name;
            this.id = id;
        }
        public void run(){
            createPetProfileButton(name, id);
        }
    }

    private void createPetProfileButton(String name, String id){
        String selectedId = Pet.getSelectedId(context);

        //Set up view
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 0);
        layout.setLayoutParams(params);
        layout.setPadding(10, 10, 10,10);
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

        layout.setTag(R.string.pet_preference_name, name);
        layout.setTag(R.string.pet_preference_id, id);
        if (selectedId.equals(id)){
           select(layout);
           selected = layout;
        }else {
           deselect(layout);
        }        contentView.addView(layout);
        layout.setOnClickListener((View view) -> {
            if (view != selected) {
                select(view);
                deselect(selected);
                selected = (LinearLayout) view;
            }
        });
        viewArray.add(layout);
    }

    private void select(View view){
        Pet.setSelected(view.getTag(R.string.pet_preference_name).toString(),
                view.getTag(R.string.pet_preference_id).toString(),
                this
                );
        view.setBackgroundColor(getColor(R.color.colorAccent));
    }

    private void deselect(View view){
        view.setBackgroundResource(R.drawable.button_dashboard_standard);
    }


}
