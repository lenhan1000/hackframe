package com.example.silc.hackathonframework.controllers.pendant;

import android.util.Log;

import com.example.silc.hackathonframework.db.AppDatabase;
import com.example.silc.hackathonframework.models.Accel;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.Pendant;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Temperature;

import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;

public class AccelerationComponentController {
    @Inject
    Accelerometer accelerometer;
    private AppDatabase appDb;
    private boolean running;

    public AccelerationComponentController(App app){
        try {
            while (!app.isBoardConnected()) {
                Thread.sleep(1000);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        app.getPendantComponent().inject(this);
        appDb = app.getAppDb();
        setUp();
        running = false;
    }

    private void setUp(){
        accelerometer.configure()
                .odr(1f)
                .range(6f)
                .commit();
        accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
            @Override
            public void configure(RouteComponent source) {
                source.stream(new Subscriber() {
                    @Override
                    public void apply(Data data, Object... env) {
                        Log.e("Pendant Accelerometer",
                                new Accel(data.value(Acceleration.class)).toString() + ',');
                        appDb.accelDao().insertAll(new Accel(data.value(Acceleration.class)));
                    }
                });
            }
        });
    }

    public void start(){
        appDb.accelDao().reset();
        accelerometer.acceleration().start();
        accelerometer.start();
        running = true;
    }

    public void stop(){
        accelerometer.acceleration().stop();
        accelerometer.stop();
        running = false;
    }

    public boolean isRunning(){
        return running;
    }

    public List<Accel> getAll(){
        return appDb.accelDao().getAll();
    }
}
