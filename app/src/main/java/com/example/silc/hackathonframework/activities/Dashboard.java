package com.example.silc.hackathonframework.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.silc.hackathonframework.databinding.ActivityDashboardBinding;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.Pet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.IOException;

public class Dashboard extends AppBarActivity implements View.OnClickListener,
        Http2Request.Http2RequestListener{
    private static final String TAG = "activities.Dashboard";
    private ActivityDashboardBinding binding;

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
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_dashboard,
                mContentFrame,
                true);
        ((App) getApplication()).getComponent().inject(this);
        getSupportActionBar().setTitle(Pet.getSelectedName(this));
        getSupportActionBar().setSubtitle("Last Synced " + Utils.getCurrentTime());
        binding.buttonActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PetActivities.class));
            }
        });
        binding.buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PetProfile.class));
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
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
        super.onClick(v);
    }

    @Override
    public void onRequestFinished(String id, JSONObject user){
        if (user != null) Log.d(TAG, user.toString());
    }

    private void forceInstanceIdUpdate(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.i(TAG, "UPDATING IID");
                App.sendInstanceId(instanceIdResult.getToken());
            }
        });
    }

}
