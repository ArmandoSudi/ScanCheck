package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Macaron;

@Dao
public interface IMacaronDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Macaron...macarons);

    @Update
    int update(Macaron...macarons);

    @Delete
    int delete(Macaron...macarons);

    @Query("SELECT * FROM MACARON WHERE CODE_SECRET=:codeSecret")
    Macaron check(String codeSecret);
}
