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
    @SerializedName("CodeAs")
    public String CodeAS;

    @ColumnInfo(name="NOM_AS")
    @SerializedName("Nom")
    public String Nom;

    @ColumnInfo(name="CODE_ZS")
    @SerializedName("CodeZs")
    public String CodeZS;

    public AirsSante() {
    }

    public AirsSante(String codeAS, String nom, String codeZS) {
        this.CodeAS = codeAS;
        this.Nom = nom;
        this.CodeZS = codeZS;
    }

    @Override
    public String toString() {
        return Nom;
    }

    public String getCodeAS() {
        return CodeAS;
    }

    public void setCodeAS(String codeAS) {
        this.CodeAS = codeAS;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getCodeZS() {
        return CodeZS;
    }

    public void setCodeZS(String codeZS) {
        this.CodeZS = codeZS;
    }
}
