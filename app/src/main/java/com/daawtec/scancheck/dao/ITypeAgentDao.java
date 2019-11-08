package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.TypeAgent;

import java.util.List;

@Dao
public interface ITypeAgentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(TypeAgent...typeAgents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<TypeAgent> typeAgents);

    @Update
    int update(TypeAgent...typeAgents);

    @Delete
    int delete(TypeAgent...typeAgents);
}
