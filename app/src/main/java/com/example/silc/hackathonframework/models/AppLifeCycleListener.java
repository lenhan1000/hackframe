package com.example.silc.hackathonframework.models;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class AppLifeCycleListener implements LifecycleObserver{
    App app;
    static final String TAG = "LifeCycleListener";

    public AppLifeCycleListener(App app){
        this.app = app;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMovetoForeground(){
        Log.d(TAG, "Move to foreground...");
        app.locationUpdate();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground(){
        Log.d(TAG,"Move to background...");
    }
}
