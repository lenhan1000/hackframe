package com.example.silc.hackathonframework.dagger.module;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.silc.hackathonframework.models.App;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Settings;
import com.mbientlab.metawear.module.Temperature;

import javax.inject.Singleton;

import bolts.Continuation;
import bolts.Task;
import dagger.Module;
import dagger.Provides;

@Module
public class PendantModule {
    BtleService.LocalBinder binder;
    String MW_MAC_ADDRESS = "C5:93:49:0F:2C:6A";
    String TAG = "modules.PendantModules";

    public PendantModule(BtleService.LocalBinder b){
        binder = b;
    }

    @Provides
    @Singleton
    public BluetoothManager provideBluetoohManager(App app){
        return (BluetoothManager ) app.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Provides
    @Singleton
    public BluetoothDevice provideBluetoothDevice(BluetoothAdapter adapter){
        return adapter.getRemoteDevice(MW_MAC_ADDRESS);
    }

    @Provides
    @Singleton
    public BluetoothAdapter provideBluetoothAdapter(BluetoothManager manager){
        return manager.getAdapter();
    }

    @Provides
    @Singleton
    public MetaWearBoard provideMetaWearBoard(BluetoothDevice device, App app){
        MetaWearBoard board = binder.getMetaWearBoard(device);
        board.connectAsync().continueWith(new Continuation<Void, Object>() {
            public Void then(Task<Void> task) throws Exception{
                if (task.isFaulted()){
                    Log.e(TAG, "Failed to connect");
                } else {
                    Log.i(TAG, "Connected");
                }
                return null;
            }
        });
        return board;
    }

    @Provides
    @Singleton
    public Temperature provideTemperature(MetaWearBoard board){
        return board.getModule(Temperature.class);
    }

    @Provides
    @Singleton
    public Temperature.Sensor provideTemperatureSensor(Temperature temp){
        return temp.findSensors(Temperature.SensorType.PRESET_THERMISTOR)[0];
    }

    @Provides
    @Singleton
    public Accelerometer provideAccelerometer(MetaWearBoard board){
        return board.getModule(Accelerometer.class);
    }

    @Provides
    @Singleton
    public Settings provideSettings(MetaWearBoard board){
        return board.getModule(Settings.class);
    }
}
