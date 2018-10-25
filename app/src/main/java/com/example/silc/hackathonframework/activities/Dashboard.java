package com.example.silc.hackathonframework.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.silc.hackathonframework.databinding.ActivityDashboardBinding;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.models.App;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;

import org.json.JSONObject;

public class Dashboard extends AppBarActivity implements View.OnClickListener,
        Http2Request.Http2RequestListener{
    private static final String TAG = "activities.Dashboard";
    private final String MW_MAC_ADDRESS = "C5:93:49:0F:2C:6A";
    private ActivityDashboardBinding binding;
    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard board;

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
        ((App) getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_dashboard,
                mContentFrame,
                true);
        getSupportActionBar().setTitle("Champ");
        getSupportActionBar().setSubtitle("Last Synced " + Utils.getCurrentTime());
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
