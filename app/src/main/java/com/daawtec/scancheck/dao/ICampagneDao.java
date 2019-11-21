package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Campagne;

import java.util.List;

@Dao
public interface ICampagneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Campagne...campagnes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Campagne> campagnes);

    @Update
    int update(Campagne...campagnes);

    @Delete
    int delete(Campagne...campagnes);
}
