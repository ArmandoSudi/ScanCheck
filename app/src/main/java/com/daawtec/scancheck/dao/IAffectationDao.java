package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.RelaisCommunautaire;

import java.util.List;

@Dao
public interface IAffectationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Affectation...affectations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Affectation> affectations);

    @Update
    int update(Affectation...affectations);

    @Delete
    int delete(Affectation...affectations);

    @Query("SELECT * FROM AFFECTATION WHERE CODE_AGENT=:codeAgent")
    Affectation getAffectationByAgent(String codeAgent);
}
