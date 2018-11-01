package com.example.silc.hackathonframework.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.silc.hackathonframework.R;

public class PetProfile extends AppCompatActivity {
    private static final String Tag = "activities.PetProfile";
    private ActivityPetProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_profile);
    }
}
