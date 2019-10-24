package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.AgentDenombrement;

@Dao
public interface IAgentDenombrement {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AgentDenombrement...agentDenombrements);

    @Update
    int update(AgentDenombrement...agentDenombrements);

    @Delete
    int delete(AgentDenombrement...agentDenombrements);


}
