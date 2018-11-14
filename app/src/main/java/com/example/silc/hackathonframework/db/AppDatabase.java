package com.example.silc.hackathonframework.db;

import com.example.silc.hackathonframework.dao.AccelDao;
import com.example.silc.hackathonframework.models.Accel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Accel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccelDao accelDao();
}
