package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="ZONE_SANTE", foreignKeys = {
        @ForeignKey(entity = DivisionProvincialeSante.class, parentColumns = "CODE_DPS", childColumns = "CODE_DPS")
})
public class ZoneSante {

    @ColumnInfo(name="CODE_ZS")
    @SerializedName("codeZS")
    public String codeZS;

    @ColumnInfo(name="NOM_ZS")
    @SerializedName("nomZS")
    public String nomZS;

    @ColumnInfo(name="CODE_DPS")
    @SerializedName("codeDPS")
    public String codeDPS;

    public ZoneSante(String codeZS, String nomZS, String codeDPS) {
        this.codeZS = codeZS;
        this.nomZS = nomZS;
        this.codeDPS = codeDPS;
    }

    public String getCodeZS() {
        return codeZS;
    }

    public void setCodeZS(String codeZS) {
        this.codeZS = codeZS;
    }

    public String getNomZS() {
        return nomZS;
    }

    public void setNomZS(String nomZS) {
        this.nomZS = nomZS;
    }

    public String getCodeDPS() {
        return codeDPS;
    }

    public void setCodeDPS(String codeDPS) {
        this.codeDPS = codeDPS;
    }
}
