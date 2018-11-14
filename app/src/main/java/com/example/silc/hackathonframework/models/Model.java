package com.example.silc.hackathonframework.models;

import android.content.Context;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.helpers.Utils;

public abstract class Model {
    Model(){};

    public static String getToken(Context context){
        return Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_token),
                "",
                context.getString(R.string.user_preference));    }
}
