package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName="AGENT_DENOMBREMENT", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS")
})
public class AgentDenombrement {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AGENT_DENOMBREMENT")
    public String codeAgentDenombrement;

    @ColumnInfo(name="NOM")
    public String nom;

    @ColumnInfo(name="CODE_AS")
    public String codeAs;
}
