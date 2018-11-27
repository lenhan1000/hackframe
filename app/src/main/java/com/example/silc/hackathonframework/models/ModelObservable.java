package com.example.silc.hackathonframework.models;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Utils;

import java.util.Observable;

public abstract class ModelObservable extends BaseObservable {

    ModelObservable(){};

    public static String getToken(Context context){
        return Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_token),
                "",
                context.getString(R.string.user_preference));    }
}
