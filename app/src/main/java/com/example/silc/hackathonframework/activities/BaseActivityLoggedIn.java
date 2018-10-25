package com.example.silc.hackathonframework.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

public abstract class BaseActivityLoggedIn extends BaseActivity implements Http2Request.Http2RequestListener{
    private static final String TAG = "activities.LoggedIn";
    private static final int PERMISSION_REQUEST_FINE_LOCATION_CODE = 1;
    protected FusedLocationProviderClient fusedLocationClient;

    DialogInterface.OnClickListener messageListener = new DialogInterface.OnClickListener() {
        final int BUTTON_NEGATIVE = -2;
        final int BUTTON_POSITIVE = -1;

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case BUTTON_NEGATIVE:
                    dialogInterface.dismiss();
                    break;
                case BUTTON_POSITIVE:
                    ActivityCompat.requestPermissions(BaseActivityLoggedIn.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION_CODE);
                    dialogInterface.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        isCheckPermission();
    }

    private boolean isCheckPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("Allow access to your location");
                return false;
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_FINE_LOCATION_CODE);
            return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", messageListener)
                .setNegativeButton("Cancel", messageListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] permission,
                                           @NonNull int[] grantResults){
        switch(i){
            case PERMISSION_REQUEST_FINE_LOCATION_CODE:{
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "PERMISSION GRANTED");
                    ((App) getApplicationContext()).locationUpdate();
                }else{
                    Log.e(TAG, "PERMISSION DENIED");
                }
            }
        }
    }

}
