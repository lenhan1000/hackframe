package com.example.silc.hackathonframework.models;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application implements Http2Request.Http2RequestListener{
    private static final String TAG = "models.App";
    private static Context context;
    private static String instanceIdRoute;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        instanceIdRoute = context.getResources().getString(R.string.api_user_instance_id);
        Log.d(TAG, "WHERE");
    }

    public static Context getContext(){
        return context;
    }

    public static void sendInstanceId(String id){
        String old_id = Utils.getStringSharedPreferences(
                context,
                "instanceId",
                "",
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
    public void onRequestFinished(String id, JSONObject msg){

    }
}
