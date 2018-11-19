package com.example.silc.hackathonframework.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityPetProfileBinding;
import com.example.silc.hackathonframework.models.Pet;

import org.json.JSONObject;

public class PetProfile extends AppBarActivity {
    private static final String TAG = "activities.PetProfile";
    private ActivityPetProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_profile,
                mContentFrame,
                true);

        binding.setPet(new Pet());
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){

    }
}
