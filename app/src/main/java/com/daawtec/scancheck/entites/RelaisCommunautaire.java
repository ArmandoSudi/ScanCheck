package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="RELAIS_COMMUNAUTAIRE", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS")
})
public class RelaisCommunautaire {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_RECO")
    @SerializedName("codeReco")
    public String codeReco;

    @ColumnInfo(name="NOM")
    @SerializedName("nom")
    public String nom;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAS")
    public String codeAS;

    public RelaisCommunautaire(String codeReco, String nom, String codeAS) {
        this.codeReco = codeReco;
        this.nom = nom;
        this.codeAS = codeAS;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public String getCodeReco() {
        return codeReco;
    }

    public void setCodeReco(String codeReco) {
        this.codeReco = codeReco;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeAS() {
        return codeAS;
    }

    public void setCodeAS(String codeAS) {
        this.codeAS = codeAS;
    }
}
