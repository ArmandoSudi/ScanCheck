package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.ZoneSante;

public interface IZoneSanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(ZoneSante...zoneSantes);

    @Update
    int update(ZoneSante...zoneSantes);

    @Delete
    int delete(ZoneSante...zoneSantes);
}
