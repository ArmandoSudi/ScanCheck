package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="DIVISION_PROVINCIALE_SANTE")
public class DivisionProvincialeSante {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_DPS")
    @SerializedName("codeDPS")
    public String codeDPS;

    @ColumnInfo(name="NOM")
    @SerializedName("nom")
    public String nom;

    public DivisionProvincialeSante(String codeDPS, String nom) {
        this.codeDPS = codeDPS;
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public String getCodeDPS() {
        return codeDPS;
    }

    public void setCodeDPS(String codeDPS) {
        this.codeDPS = codeDPS;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
