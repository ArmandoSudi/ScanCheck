package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.AirsSante;

import java.util.List;

@Dao
public interface IAirSanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AirsSante...airsSantes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<AirsSante> airsSantes);

    @Update
    int update(AirsSante...airsSantes);

    @Delete
    int delete(AirsSante...airsSantes);
}
