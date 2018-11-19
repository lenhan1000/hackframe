package com.example.silc.hackathonframework.models;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.dagger.component.AppComponent;
import com.example.silc.hackathonframework.dagger.component.DaggerAppComponent;
import com.example.silc.hackathonframework.dagger.component.DaggerPendantComponent;
import com.example.silc.hackathonframework.dagger.component.PendantComponent;
import com.example.silc.hackathonframework.dagger.module.AppModule;
import com.example.silc.hackathonframework.dagger.module.LocationModule;
import com.example.silc.hackathonframework.dagger.module.NetworkModule;
import com.example.silc.hackathonframework.dagger.module.PendantModule;
import com.example.silc.hackathonframework.db.AppDatabase;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Settings;
import com.mbientlab.metawear.module.Temperature;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;


public class App extends Application implements
        Http2Request.Http2RequestListener{
    private static final String TAG = "models.App";
    private static Context context;
    private static String instanceIdRoute;
    static final int PERMISSION_REQUEST_WRITE = 1;

    private PendantComponent pendantComponent;
    private List<Accel> accelerationList;

    @Inject
    AppLifeCycleListener lifecycleListener;

    private MetaWearBoard board;

    private Temperature.Sensor tempSensor;

    private Accelerometer accelerometer;

    @Inject
    LocationRequest locationRequest;

    @Inject
    LocationCallback locationCallback;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    AppDatabase appDb;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        instanceIdRoute = context.getResources().getString(R.string.api_user_instance_id);
        getComponent().inject(this);
        accelerationList = new ArrayList<Accel>();
//        bindPendant();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleListener);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "ITS ME DIO");
//                ObjectMapper mapper = new ObjectMapper();
//                try {
//                    Log.e(TAG,mapper.writeValueAsString(accelerationList));
//                }catch(IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }, 300000);
    }


    public static Context getContext(){
        return context;
    }

    public App getApp() {return this;}

    public AppDatabase getAppDb() {
        return appDb;
    }

    public static void sendInstanceId(String id){
        String old_id = Utils.getStringSharedPreferences(
                context,
                "instanceId",
                id,
                context.getString(R.string.user_preference)
        );
        try {
            Http2Request req = new Http2Request(context);
            JSONObject js = new JSONObject();
            js.put("oldInstanceId", old_id);
            js.put("instanceId", id);
            js.put("brand", Build.BRAND);
            js.put("versionNumb", Build.VERSION.SDK_INT);
            js.put("version", Build.VERSION.RELEASE);
            js.put("bootloader", Build.BOOTLOADER);
            js.put("board", Build.BOARD);
            js.put("device", Build.DEVICE);
            js.put("display", Build.DISPLAY);
            js.put("fingerprint", Build.FINGERPRINT);
            js.put("hardware", Build.HARDWARE);
            js.put("manufacturer", Build.MANUFACTURER);
            js.put("model", Build.MODEL);
            js.put("product", Build.PRODUCT);
            req.post(
                    req.baseUrl,
                    instanceIdRoute,
                    js.toString(),
                    User.getToken(context)
            );
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
        Utils.putSharedPreferenes(
                context,
                "instanceId",
                id,
                context.getString(R.string.user_preference)
        );
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try {
            boolean success = res.getBoolean("success");
            if (!success) Log.d(TAG, res.getString("msg"));
            else {
                if (id == getResources().getString(R.string.api_user_location)){
                    Log.d(TAG, "Update location success");
                }
                else {

                }
            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public AppComponent getComponent(){
        return DaggerAppComponent.builder()
                .application(this)
                .network(new NetworkModule("adfasdfas"))
                .build();
    }

    public PendantComponent getPendantComponent() {
        return pendantComponent;
    }

    private void bindPendant(){
        bindService(new Intent(this, BtleService.class),
                mConn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BtleService.LocalBinder binder = (BtleService.LocalBinder) iBinder;
            pendantComponent = DaggerPendantComponent.builder()
                    .application(getApp())
                    .pendant(new PendantModule(binder))
                    .build();
            startPendant();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void startPendant(){
        board = pendantComponent.getMetaWearBoard();
    }

    public boolean isBoardConnected(){
        if (board == null){
            return false;
        }
        return board.isConnected();
    }

    public void getTemp(){
        tempSensor = pendantComponent.getTemperatureSensor();
        tempSensor.addRouteAsync(new RouteBuilder() {
            @Override
            public void configure(RouteComponent source) {
                source.stream(new Subscriber() {
                    @Override
                    public void apply(Data data, Object ... env) {
                        Log.i("MainActivity", "Temperature (C) = " + data.value(Float.class));
                    }
                });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                tempSensor.read();
                return null;
            }
        });
    }

    public void getBattery(){
        Settings settings = pendantComponent.getSettings();
        settings.battery().addRouteAsync(new RouteBuilder() {
            @Override
            public void configure(RouteComponent source) {
                source.stream(new Subscriber() {
                    @Override
                    public void apply(Data data, Object... env) {
                        Log.e(TAG, data.value(Settings.BatteryState.class).toString());
                    }
                });
            }
        }).continueWith(new Continuation<Route, Object>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                settings.battery().read();
                return null;
            }
        });
    }

    public void locationUpdate() throws SecurityException{
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        );
    }

    public void clearAppDatabase(){
        this.deleteDatabase("fetch-db");
    }

    public void setUpProfilePicture(){
        File dir = Environment.getExternalStorageDirectory();
        File image = new File(dir, "Pictures/profiles.png");
        if (!image.exists()) {
            FileOutputStream out;
            try{
                out = new FileOutputStream(image);
                Bitmap img = BitmapFactory.decodeResource(getResources(),
                        R.drawable.pic_sample_profile);
                img.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }catch (FileNotFoundException e){
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }catch (IOException e){
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
