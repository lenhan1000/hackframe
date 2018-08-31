package com.example.silc.hackathonframework.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http2Request {
    public static final String TAG = "helpers.Http2Request";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String id;
    private JSONObject res;
    private Context context;
    private OkHttpClient client = new OkHttpClient();
    public Http2RequestListener notice;
    public Callback callback = new Callback(){
        @Override
        public void onFailure(Call call, IOException e) {
            call.cancel();
            res = null;
            Log.e(TAG, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                res = new JSONObject(response.body().string());
                notice.onRequestFinished(id, res);
            } catch (JSONException e) {
                res = null;
                Log.e(TAG, e.getMessage());
            }
        }
    };


    public interface Http2RequestListener{
        void onRequestFinished(String id, JSONObject res);
    }

    public Http2Request(Context context){
        this.context = context;
        this.notice = (Http2RequestListener) context;
    }

    public Call post(String base_url, String route, String json){
        id = route;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(base_url + route)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(this.callback);
        return call;
    }

    public Call post(String base_url, String route, String json,
                     Callback callback) {
        id = route;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(base_url + route)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call get(String base_url, String route, String token) {
        id = route;
        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(base_url + route)
                .build();
        Call call = client.newCall(request);
        call.enqueue(this.callback);
        return call;
    }

    public Call get(String base_url, String route, String token,
                    Callback callback) {
        id = route;
        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(base_url + route)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call put(String base_url, String route, String token, String json) {
        id = route;
        RequestBody body = RequestBody.create(JSON, json);
        Request req = new Request.Builder()
                .header("Authorization", token)
                .url(base_url + route)
                .put(body)
                .build();
        Call call = client.newCall(req);
        call.enqueue(callback);
        return call;
    }

    public Call put(String base_url, String route, String token, String json,
                    Callback callback){
        id = route;
        RequestBody body = RequestBody.create(JSON, json);
        Request req = new Request.Builder()
                .header("Authorization", token)
                .url(base_url + route)
                .put(body)
                .build();
        Call call = client.newCall(req);
        call.enqueue(callback);
        return call;
    }

    public static String registerUserJson(String email, String password){
        return "{\"email\" : \"" + email + "\" , \"password\" : \"" + password + "\"}";
    }

    public JSONObject getRes() { return res; }
}
