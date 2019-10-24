package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Macaron;

import java.util.List;

@Dao
public interface IMacaronDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Macaron...macarons);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Macaron> macarons);

    @Update
    int update(Macaron...macarons);

    @Delete
    int delete(Macaron...macarons);

    @Query("SELECT * FROM MACARON")
    List<Macaron> all();

    @Query("SELECT * FROM MACARON WHERE CODE_MACARON=:codeMacaron")
    Macaron get(String codeMacaron);

    @Query("SELECT * FROM MACARON WHERE CODE_MACARON=:codeMacaron AND IS_AFFECTED=:state")
    Macaron getByState(String codeMacaron, boolean state);

    @Query("UPDATE MACARON SET IS_AFFECTED=:state WHERE CODE_MACARON=:codeMacaron")
    int updateMacaron(boolean state, String codeMacaron);

    @Query("SELECT COUNT(*) AS NBR FROM MACARON")
    int getCount();
}
