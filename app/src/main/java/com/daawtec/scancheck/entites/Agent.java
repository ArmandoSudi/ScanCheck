package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "AGENT")
public class Agent {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AGENT")
    public String CodeAgent;

    @ColumnInfo(name="NOM_AGENT")
    public String NomAgent;

    @ColumnInfo(name="CODE_AUTHENTIFICATION")
    public String CodeAuthentification;

    public Agent() {
    }

    public Agent(@NonNull String codeAgent, String nomAgent, String codeAuthentification) {
        CodeAgent = codeAgent;
        NomAgent = nomAgent;
        CodeAuthentification = codeAuthentification;
    }
}
