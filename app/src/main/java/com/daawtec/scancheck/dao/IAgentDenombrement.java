package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.TypeAgent;

import java.util.List;

@Dao
public interface IAgentDenombrement {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AgentDenombrement...agentDenombrements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<AgentDenombrement> agentDenombrements);

    @Update
    int update(AgentDenombrement...agentDenombrements);

    @Delete
    int delete(AgentDenombrement...agentDenombrements);

    @Query("SELECT * FROM AGENT_DENOMBREMENT WHERE CODE_AGENT_DENOMBREMENT=:codeDenombrement")
    AgentDenombrement get(String codeDenombrement);

    @Query("SELECT * FROM AGENT_DENOMBREMENT WHERE CODE_AUTHENTIFICATION=:codeAuth")
    AgentDenombrement getAgentByCodeAuth(String codeAuth);
}
