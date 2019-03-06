package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="INVENTAIRE_PHYSIQUE")
public class InventairePhysique {

    @ColumnInfo(name="CODE_INVENTAIRE")
    @SerializedName("codeInventaire")
    public String codeInventaire;

    @ColumnInfo(name="DATE")
    @SerializedName("date")
    public Date date;

    @ColumnInfo(name="QUANTITE_THEORIQUE")
    @SerializedName("quantiteTheorique")
    public int quantiteTheorique;

    @ColumnInfo(name="QUANTITE_PHYSIQUE")
    @SerializedName("quantitePhysique")
    public int quantitePhysique;

    @ColumnInfo(name="ECART")
    @SerializedName("ecart")
    public int ecart;

    @ColumnInfo(name="NOMBRE_MACARON")
    @SerializedName("nombreMacaron")
    public int nombreMacaron;

    public InventairePhysique(String codeInventaire, Date date, int quantiteTheorique, int quantitePhysique, int ecart, int nombreMacaron) {
        this.codeInventaire = codeInventaire;
        this.date = date;
        this.quantiteTheorique = quantiteTheorique;
        this.quantitePhysique = quantitePhysique;
        this.ecart = ecart;
        this.nombreMacaron = nombreMacaron;
    }

    public String getCodeInventaire() {
        return codeInventaire;
    }

    public void setCodeInventaire(String codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantiteTheorique() {
        return quantiteTheorique;
    }

    public void setQuantiteTheorique(int quantiteTheorique) {
        this.quantiteTheorique = quantiteTheorique;
    }

    public int getQuantitePhysique() {
        return quantitePhysique;
    }

    public void setQuantitePhysique(int quantitePhysique) {
        this.quantitePhysique = quantitePhysique;
    }

    public int getEcart() {
        return ecart;
    }

    public void setEcart(int ecart) {
        this.ecart = ecart;
    }

    public int getNombreMacaron() {
        return nombreMacaron;
    }

    public void setNombreMacaron(int nombreMacaron) {
        this.nombreMacaron = nombreMacaron;
    }
}
