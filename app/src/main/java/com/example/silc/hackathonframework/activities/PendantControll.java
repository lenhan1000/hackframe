package com.example.silc.hackathonframework.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.controllers.pendant.AccelerationComponentController;
import com.example.silc.hackathonframework.databinding.ActivityPendantControllBinding;
import com.example.silc.hackathonframework.models.Accel;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.Pendant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mbientlab.metawear.impl.platform.IO;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class PendantControll extends AppBarActivity {
    private static final String TAG = "activities.PendantControll";
    private ActivityPendantControllBinding binding;
    private AccelerationComponentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pendant_controll,
                mContentFrame,
                true);
        navigation.getMenu().findItem(R.id.navigation_recording).setChecked(true);
        controller = new AccelerationComponentController((App) getApplication());
        checkAndCreateFolder();
        binding.start.setOnClickListener((View view) -> {
            if(!controller.isRunning()) {
                controller.start();
                Toast.makeText(context, "START",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.stop.setOnClickListener((View view) -> {
            if(controller.isRunning()) {
                controller.stop();
                popFileNameDialog();
            }
        });

    }

    @Override
    public void onRequestFinished(String id, JSONObject res){

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_recording).setChecked(true);
    }

    private void saveToJSON(String filename){
        List<Accel> data = controller.getAll();
        String dateTag = new DateTime(data.get(0).time).toString("_MM_dd_yyyy_HH_mm_ss");
        File jsonFile = new File(Environment.getExternalStorageDirectory(),
                "testing/JSONfiles/" + filename + dateTag + ".json");
        String element = new Gson().toJson(data);
        try(Writer writer = new FileWriter(jsonFile)){
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
        }catch(IOException e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        Log.e(TAG,element);
    }

    private void checkAndCreateFolder(){
        File dir = Environment.getExternalStorageDirectory();
        File jsonFolder = new File(dir, "testing/JSONfiles");
        if(!jsonFolder.exists()){
            jsonFolder.mkdirs();
        }
    }

    private void popFileNameDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Filename");
        dialog.setMessage("Enter filename");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        dialog.setView(input);
        dialog.setPositiveButton("Save",
            (DialogInterface dialogInterface, int i) -> {
                String filename = input.getText().toString();
                if(filename.isEmpty()){
                    runOnUiThread(() ->
                            Toast.makeText(context, "Invalid Name",
                                    Toast.LENGTH_SHORT)
                    );
                }else {
                    saveToJSON(input.getText().toString());
                    startActivity(new Intent(context, PendantControll.class));
                }
        });

        dialog.setNegativeButton("Discard",
            (DialogInterface dialogInterface, int i) ->
            dialogInterface.cancel());

        dialog.show();
    }
}
