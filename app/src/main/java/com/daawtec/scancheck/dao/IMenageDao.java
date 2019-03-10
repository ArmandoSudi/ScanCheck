package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Menage;

import java.util.List;

@Dao
public interface IMenageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Menage...menages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Menage> menages);

    @Update
    int update(Menage...menages);

    @Delete
    int delete(Menage...menages);

    @Query("SELECT * FROM MENAGE")
    List<Menage> all();
}
