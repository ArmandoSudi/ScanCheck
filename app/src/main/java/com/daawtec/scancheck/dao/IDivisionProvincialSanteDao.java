package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.DivisionProvincialeSante;

import java.util.List;

@Dao
public interface IDivisionProvincialSanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(DivisionProvincialeSante...divisionProvincialeSantes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<DivisionProvincialeSante> divisionProvincialeSantes);

    @Update
    int update(DivisionProvincialeSante...divisionProvincialeSantes);

    @Delete
    int delete(DivisionProvincialeSante...divisionProvincialeSantes);
}
