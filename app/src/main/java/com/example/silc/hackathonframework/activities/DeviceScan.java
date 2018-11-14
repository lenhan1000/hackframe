package com.example.silc.hackathonframework.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;

public class DeviceScan extends AppCompatActivity {
    private static final String TAG = "activities.DeviceScan";
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter mBLEAdapter;
    private boolean mScanning;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        final BluetoothManager bluetoothMananger =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBLEAdapter = bluetoothMananger.getAdapter();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }else {
            findDevices();
        }
    }

    private void findDevices(){

    }
}
