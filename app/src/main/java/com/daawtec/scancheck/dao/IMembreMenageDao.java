package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.MembreMenage;

import java.util.List;

@Dao
public interface IMembreMenageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(MembreMenage...membreMenages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<MembreMenage> membreMenages);

    @Update
    int update(MembreMenage...membreMenages);

    @Delete
    int delete(MembreMenage...membreMenages);
}
