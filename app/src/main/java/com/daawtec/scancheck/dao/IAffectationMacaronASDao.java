package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.AffectationMacaronAS;

import java.util.List;

@Dao
public interface IAffectationMacaronASDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AffectationMacaronAS...affectationMacaronAS);

    @Update
    int update(AffectationMacaronAS...affectationMacaronAS);

    @Delete
    int delete(AffectationMacaronAS...affectationMacaronAS);

    @Query("SELECT * FROM AFFECTATION_MACARON_AS")
    List<AffectationMacaronAS> all();

}
