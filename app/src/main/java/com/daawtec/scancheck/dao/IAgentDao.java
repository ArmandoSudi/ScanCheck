package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Agent;

import java.util.List;

@Dao
public interface IAgentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Agent...agents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Agent> agents);

    @Update
    int update(Agent...agents);

    @Delete
    int delete(Agent...agents);

    @Query("SELECT * FROM AGENT WHERE CODE_AUTHENTIFICATION=:codeAgent")
    Agent get(String codeAgent);

    @Query("SELECT * FROM AGENT WHERE CODE_AGENT=:codeAgent")
    Agent getByCodeAgent(String codeAgent);

    @Query("SELECT * FROM AGENT WHERE CODE_AUTHENTIFICATION=:codeAgentAuth")
    Agent getAgentByAuth(String codeAgentAuth);

    @Query("SELECT * FROM AGENT")
    List<Agent> all();
}
