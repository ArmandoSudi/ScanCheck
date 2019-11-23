package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "VALIDITE_ROLE", foreignKeys = {
        @ForeignKey(entity = Agent.class, parentColumns = "CODE_AGENT", childColumns = "CODE_AGENT")
})
public class ValiditeRole {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_VALIDITE_ROLE")
    public String CodeValiditeRole;

    @ColumnInfo(name="CODE_AGENT")
    public String codeAgent;

    @ColumnInfo(name="CODE_TYPE_AGENT")
    public String codeTypeAgent;

    @ColumnInfo(name="DATE_DEBUT")
    public Date dateDebut;

    @ColumnInfo(name="DATE_FIN")
    public Date dateFin;

    public ValiditeRole() {

    }

}
