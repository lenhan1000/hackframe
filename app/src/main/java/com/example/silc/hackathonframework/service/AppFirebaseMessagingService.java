package com.example.silc.hackathonframework.service;

import android.content.Context;
import android.util.Log;

import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.models.App;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AppFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "AppFirebaseMessagingService";
    private Context context;
    private String instanceIdRoute;

    public AppFirebaseMessagingService(Context c){
        super();
        context = c;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Data payload: " + remoteMessage.getData());

        }
    }

    @Override
    public void onNewToken(String token){
        Log.d(TAG, "Refreshed token: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token){
        Http2Request req = new Http2Request(context);

    }
}
