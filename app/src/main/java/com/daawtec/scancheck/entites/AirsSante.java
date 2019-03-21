package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="AIRS_SANTE", foreignKeys = {
        @ForeignKey(entity = ZoneSante.class, parentColumns = "CODE_ZS", childColumns = "CODE_ZS")
})
public class AirsSante {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAs")
    public String codeAS;

    @ColumnInfo(name="NOM_AS")
    @SerializedName("nom")
    public String nomAS;

    @ColumnInfo(name="CODE_ZS")
    @SerializedName("codeZs")
    public String codeZS;

    public AirsSante(String codeAS, String nomAS, String codeZS) {
        this.codeAS = codeAS;
        this.nomAS = nomAS;
        this.codeZS = codeZS;
    }

    @Override
    public String toString() {
        return nomAS ;
    }

    public String getCodeAS() {
        return codeAS;
    }

    public void setCodeAS(String codeAS) {
        this.codeAS = codeAS;
    }

    public String getNomAS() {
        return nomAS;
    }

    public void setNomAS(String nomAS) {
        this.nomAS = nomAS;
    }

    public String getCodeZS() {
        return codeZS;
    }

    public void setCodeZS(String codeZS) {
        this.codeZS = codeZS;
    }
}
