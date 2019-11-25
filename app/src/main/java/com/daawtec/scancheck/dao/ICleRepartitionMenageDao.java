package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.CleRepartitionMenage;

import java.util.List;

@Dao
public interface ICleRepartitionMenageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(CleRepartitionMenage...cleRepartitionMenages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<CleRepartitionMenage> cleRepartitionMenages);

    @Update
    int update(CleRepartitionMenage...cleRepartitionMenages);

    @Delete
    int delete(CleRepartitionMenage...cleRepartitionMenages);
}
