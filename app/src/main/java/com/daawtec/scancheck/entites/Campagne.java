package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "CAMPAGNE")
public class Campagne {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_CAMPAGNE")
    public String CodeCampagne;

    @ColumnInfo(name="DATE_DEBUT")
    @SerializedName("DateDebutSTR")
    public String DateDebut;

    @ColumnInfo(name="DATE_FIN")
    @SerializedName("DateFinSTR")
    public String DateFin;

    @ColumnInfo(name="CODE_TYPE_CAMPAGNE")
    public String CodeTypeCampagne;

    public Campagne() {
    }

    public Campagne(@NonNull String codeCampagne, String dateDebut, String dateFin, String codeTypeCampagne) {
        CodeCampagne = codeCampagne;
        DateDebut = dateDebut;
        DateFin = dateFin;
        CodeTypeCampagne = codeTypeCampagne;
    }
}
