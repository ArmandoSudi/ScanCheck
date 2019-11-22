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

    public TypeAgent(String codeTypeAgent) {
        this.codeTypeAgent = codeTypeAgent;
    }
}
