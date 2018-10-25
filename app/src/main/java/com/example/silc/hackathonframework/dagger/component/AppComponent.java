package com.example.silc.hackathonframework.dagger.component;

import android.app.Activity;
import android.app.Application;

import com.example.silc.hackathonframework.dagger.module.AppModule;
import com.example.silc.hackathonframework.dagger.module.LocationModule;
import com.example.silc.hackathonframework.dagger.module.NetworkModule;
import com.example.silc.hackathonframework.dagger.module.PendantModule;
import com.example.silc.hackathonframework.models.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        LocationModule.class,
})
public interface AppComponent {
    void inject(Activity activity);
    void inject(App app);

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance
        Builder application(App app);
        Builder network(NetworkModule networkModule);
    }
}
