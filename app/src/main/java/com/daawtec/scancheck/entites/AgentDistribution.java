package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AGENT_DISTRIBUTION", foreignKeys = {
        @ForeignKey(entity = SiteDistribution.class, parentColumns = "CODE_SD", childColumns = "CODE_SD")
})
public class AgentDistribution {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AGENT_DISTRIBUTION")
    @SerializedName("CodeAgentDistribution")
    public String codeAgentDistribution;

    @ColumnInfo(name="NOM")
    @SerializedName("Nom")
    public String nom;

    @ColumnInfo(name="ROLE")
    @SerializedName("Role")
    public String role;

    @ColumnInfo(name="CODE_SD")
    @SerializedName("CodeSD")
    public String codeSd;

    @ColumnInfo(name="CODE_AUTHENTIFICATION")
    @SerializedName("CodeAuthentification")
    public String codeAuthentification;

    public AgentDistribution(@NonNull String codeAgentDistribution, String nom, String role, String codeSd, String codeAuthentification) {
        this.codeAgentDistribution = codeAgentDistribution;
        this.nom = nom;
        this.role = role;
        this.codeSd = codeSd;
        this.codeAuthentification = codeAuthentification;
    }
}
