package com.example.silc.hackathonframework.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.silc.hackathonframework.models.Accel;

import java.util.List;


@Dao
public interface AccelDao {

    @Query("SELECT * FROM accel")
    List<Accel> getAll();

    @Insert
    void insertAll(Accel... accels);

    @Delete
    void delete(Accel accel);

    @Query("DELETE FROM accel")
    void reset();
}
