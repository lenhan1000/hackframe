package com.example.silc.hackathonframework.dagger.component;

import android.app.Activity;

import com.example.silc.hackathonframework.dagger.module.AppModule;
import com.example.silc.hackathonframework.dagger.module.LocationModule;
import com.example.silc.hackathonframework.dagger.module.PendantModule;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.Pendant;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Temperature;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PendantModule.class})
public interface PendantComponent {
    void inject(Activity activity);

    Temperature.Sensor getTemperatureSensor();
    Accelerometer getAccelerometer();
    MetaWearBoard getMetaWearBoard();

    @Component.Builder
    interface Builder {
        PendantComponent build();
        @BindsInstance
        Builder application(App app);
        Builder pendant(PendantModule module);
    }
}
