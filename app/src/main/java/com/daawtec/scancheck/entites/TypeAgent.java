package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "TYPE_AGENT")
public class TypeAgent {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_TYPE_AGENT")
    public String codeTypeAgent;

    @ColumnInfo(name="LIBELLE")
    public String libelle;

    public TypeAgent(String codeTypeAgent, String libelle) {
        this.codeTypeAgent = codeTypeAgent;
        this.libelle = libelle;
    }
}
