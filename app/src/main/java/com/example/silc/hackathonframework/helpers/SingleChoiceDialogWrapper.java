package com.example.silc.hackathonframework.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SingleChoiceDialogWrapper {
    protected AppCompatActivity activity;

    SingleChoiceDialogWrapper (Context context){
        activity = (AppCompatActivity) context;
    }

    protected void popDialog(String title, ArrayList<String> list){
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", list);
        data.putString("title", title);
        dialog.setArguments(data);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        dialog.show(ft, null);
    }
}
