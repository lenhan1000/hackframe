package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.silc.hackathonframework.R;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.signOutButton){
            signOut();
        }
    }
}
