package com.example.silc.hackathonframework.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.dagger.component.AppComponent;
import com.example.silc.hackathonframework.databinding.ActivityPetActivitiesBinding;
import com.example.silc.hackathonframework.db.AppDatabase;
import com.example.silc.hackathonframework.helpers.ActivityValueFormatter;
import com.example.silc.hackathonframework.helpers.ActivityYAxisFormatter;
import com.example.silc.hackathonframework.helpers.DateAxisFormatter;
import com.example.silc.hackathonframework.models.Accel;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.AppLifeCycleListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PetActivities extends AppBarActivity {
    private static final String TAG = "activities.PetActivity";
    private ActivityPetActivitiesBinding binding;
    private List<Accel> data;
    private List<Entry> entries = new ArrayList<Entry>();
    private OnChartGestureListener chartGestureListener = new OnChartGestureListener() {
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            Log.e(TAG, String.format("%f, %f", scaleX, scaleY));
        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    };


    @Inject
    AppDatabase appDb;

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
        long counter = 1;
        Log.e(TAG, Integer.toString(data.size()));
        HashMap<Long, List<Double>> domainRangeHM= new HashMap<>();
        HashMap<Long, Long> domainHM = new HashMap<>();
        for(Accel d: data){
            DateTime dt = new DateTime(d.time);
            long time = dt.secondOfMinute().setCopy(0)
                    .millisOfSecond().setCopy(0)
                    .getMillis();
            if(!domainHM.containsKey(time)){
                domainHM.put(time, counter);
                List<Double> range = new ArrayList<>();
                range.add(d.getMagnitude());
                domainRangeHM.put(counter, range);
                counter++;
            }else{
                domainRangeHM.get(domainHM.get(time))
                        .add(d.getMagnitude());
            }
        }
        Log.e(TAG, domainRangeHM.toString());
        for(Map.Entry<Long, List<Double>> entry: domainRangeHM.entrySet()){
            double sum = 0;
            for(double e:entry.getValue()){
                sum += e;
            }
            double avg = sum/ entry.getValue().size();
            Log.e(TAG, entry.getKey().toString());
            entries.add(new Entry(entry.getKey(), (float) avg));
        }
//        for(Accel d: data){
//            DateTime dateTime = new DateTime(d.time);
//            Log.e(TAG, dateTime.minuteOfHour().getAsShortText());
//            entries.add(new Entry((float) dateTime.getMinuteOfHour(), (float) d.getMagnitude()));
//        }
        Collections.sort(entries, new EntryXComparator());
        LineDataSet dataSet = new LineDataSet(entries,"Activity");
        dataSet.setValueFormatter(new ActivityValueFormatter());
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
        binding.chart.setOnChartGestureListener(chartGestureListener);
        binding.chart.getAxisLeft().setValueFormatter(new ActivityYAxisFormatter());
        binding.chart.getXAxis().setValueFormatter(new DateAxisFormatter(domainHM));

    }
}
