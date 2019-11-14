package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="AGENT_DENOMBREMENT", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS"),
})
public class AgentDenombrement {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AGENT_DENOMBREMENT")
    @SerializedName("CodeAgentDenombrement")
    public String codeAgentDenombrement;

    @ColumnInfo(name="NOM")
    @SerializedName("Nom")
    public String nom;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("CodeAs")
    public String codeAs;

    @ColumnInfo(name="CODE_AUTHENTIFICATION")
    @SerializedName("CodeAuthentification")
    public String codeAuthentification;

    public AgentDenombrement(@NonNull String codeAgentDenombrement, String nom, String codeAs, String codeAuthentification) {
        this.codeAgentDenombrement = codeAgentDenombrement;
        this.nom = nom;
        this.codeAs = codeAs;
        this.codeAuthentification = codeAuthentification;
    }
}
