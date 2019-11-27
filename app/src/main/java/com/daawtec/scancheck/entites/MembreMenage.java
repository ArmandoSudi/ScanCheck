package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "MEMBRE_MENAGE", foreignKeys = {
        @ForeignKey(entity = Menage.class, parentColumns = "CODE_MENAGE", childColumns = "CODE_MENAGE")
})
public class MembreMenage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MEMBRE_MENAGE")
    public String CodeMembreMenage;

    @ColumnInfo(name="CODE_MENAGE")
    @SerializedName("CodeMenage")
    public String CodeMenage;

    @ColumnInfo(name="NOM")
    @SerializedName("Nom")
    public String Nom;

    @ColumnInfo(name="PRENOM")
    @SerializedName("Prenom")
    public String Prenom;

    @ColumnInfo(name="SEXE")
    @SerializedName("Sexe")
    public String sexe;

    public MembreMenage() {
    }

    public MembreMenage(@NonNull String codeMembreMenage, String codeMenage, String nom, String prenom, String sexe) {
        CodeMembreMenage = codeMembreMenage;
        CodeMenage = codeMenage;
        Nom = nom;
        Prenom = prenom;
        this.sexe = sexe;
    }
}
