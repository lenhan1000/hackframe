package com.example.silc.hackathonframework.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.silc.hackathonframework.db.AppDatabase;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.models.App;
import com.example.silc.hackathonframework.models.AppLifeCycleListener;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AppModule {
    @Provides
    @Singleton
    public SharedPreferences providePreferences(
            App application) {
        return application.getSharedPreferences(
                "store", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public AppLifeCycleListener provideAppLifeCycleListener(App app){
        return new AppLifeCycleListener(app);
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(App app){
        return Room.databaseBuilder(app.getApplicationContext(),
                AppDatabase.class, "fetch-db")
                .allowMainThreadQueries()
                .build();
    }
}
