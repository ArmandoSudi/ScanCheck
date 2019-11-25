package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "CLE_REPARTION_MENAGE", foreignKeys = {
        @ForeignKey(entity = Campagne.class, parentColumns = "CODE_CAMPAGNE", childColumns = "CODE_CAMPAGNE"),
        @ForeignKey(entity = TypeMenage.class, parentColumns = "CODE_TYPE_MENAGE", childColumns = "CODE_TYPE_MENAGE")
})
public class CleRepartitionMenage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_CLE_REPARTITION_MENAGE")
    public String CodeCleRepartitionMenage;

    @ColumnInfo(name="CODE_CAMPAGNE")
    public String CodeCampagne;

    @ColumnInfo(name="CODE_TYPE_MENAGE")
    public String CodeTypeMenage;

    @ColumnInfo(name="NOMBRE_MILD")
    public int NombreMild;

    @ColumnInfo(name="DATE_CREATION")
    public Date DateCreation;
}
