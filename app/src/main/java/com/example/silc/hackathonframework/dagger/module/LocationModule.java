package com.example.silc.hackathonframework.dagger.module;

import android.location.Location;
import android.util.Log;

import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {
    String TAG;
    LocationCallback callback;

    public LocationModule(){
       TAG = "dagger.module.LocationModule";
       callback = new LocationCallback();
    }

    @Provides
    @Singleton
    public FusedLocationProviderClient provideFusedLocationProviderClient(App app){
        return LocationServices.getFusedLocationProviderClient(app);
    }

    @Provides
    @Singleton
    LocationRequest provideLocationRequest(){
        LocationRequest req = new LocationRequest();
        req.setInterval(10000);
        req.setFastestInterval(5000);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return req;
    }

    @Provides
    @Singleton
    LocationCallback providesLocationCallback(App app,
                                              FusedLocationProviderClient client){
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult loc){
                if(loc == null) {
                    Log.e(TAG, "Location is null");
                    return;
                }
                for (Location l : loc.getLocations()){
                    User.updateLocation(
                            l.getLatitude(),
                            l.getLongitude(),
                            app.getApplicationContext()
                    );
                    Log.e(TAG,
                            String.format("Lat: %f Long: %f Alt: %f Acc: %f",
                                    l.getLatitude(), l.getLongitude(),
                                    l.getAltitude(), l.getAccuracy()));
                    client.removeLocationUpdates(callback);
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.e(TAG, "onLocationAvailability: isLocationAvailable =  " + locationAvailability.isLocationAvailable());
            }
        };
        return callback;
    }


}
