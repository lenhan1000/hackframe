package com.example.silc.hackathonframework.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.dagger.component.AppComponent;
import com.example.silc.hackathonframework.databinding.ActivityPetActivitiesBinding;
import com.example.silc.hackathonframework.db.AppDatabase;
import com.example.silc.hackathonframework.models.Accel;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.AppLifeCycleListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PetActivities extends AppBarActivity {
    private static final String TAG = "activities.PetActivity";
    private ActivityPetActivitiesBinding binding;
    private List<Accel> data;
    private List<Entry> entries = new ArrayList<Entry>();


    @Inject
    AppDatabase appDb;

    @Inject
    AppLifeCycleListener appLifeCycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_activities,
                mContentFrame,
                true);
        //Inject
        ((App) getApplication()).getComponent().inject(this);
        setData();

    }

    @Override
    public void onRequestFinished(String id, JSONObject res){

    }

    private void setData(){
        data = appDb.accelDao().getAll();
        float counter = 1;
        Log.e(TAG, Integer.toString(data.size()));
        for(Accel d: data){
            entries.add(new Entry(counter++, (float) d.getMagnitude()));
        }
        LineDataSet dataSet = new LineDataSet(entries,"Activity");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.disableDashedLine();
        dataSet.setColor(Color.BLUE);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(2f);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setFillAlpha(65);
        LineData lineData = new LineData(dataSet);
        binding.chart.setData(lineData);
        binding.chart.invalidate();
    }
}
