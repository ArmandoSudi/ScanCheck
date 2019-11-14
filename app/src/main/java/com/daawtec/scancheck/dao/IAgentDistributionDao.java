package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.daawtec.scancheck.entites.AgentDistribution;

import java.util.List;

@Dao
public interface IAgentDistributionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AgentDistribution...agentDistributions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<AgentDistribution> agentDistributions);

    @Update
    int update(AgentDistribution...agentDistributions);

    @Delete
    int delete(AgentDistribution...agentDistributions);

    @Query("SELECT * FROM AGENT_DISTRIBUTION WHERE CODE_AGENT_DISTRIBUTION=:codeAgentDistribution")
    AgentDistribution get(String codeAgentDistribution);

    @Query("SELECT * FROM AGENT_DISTRIBUTION WHERE CODE_AUTHENTIFICATION=:codeAuth")
    AgentDistribution getAgentByCodeAuth(String codeAuth);
}
