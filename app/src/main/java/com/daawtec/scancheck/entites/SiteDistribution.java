package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="SITE_DISTRIBUTION", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS")
})
public class SiteDistribution {

    @ColumnInfo(name="CODE_SD")
    @SerializedName("codeSD")
    public String codeSD;

    @ColumnInfo(name="NOM")
    @SerializedName("nom")
    public String nom;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAS")
    public String codeAS;

    @ColumnInfo(name="QUANTITE_LIVREE")
    @SerializedName("quantiteLivree")
    public int quantiteLivree;

    @ColumnInfo(name="QUANTITE_SORTIE")
    @SerializedName("quantiteSortie")
    public int quantiteSortie;

    public SiteDistribution(String codeSD, String nom, String codeAS, int quantiteLivree, int quantiteSortie) {
        this.codeSD = codeSD;
        this.nom = nom;
        this.codeAS = codeAS;
        this.quantiteLivree = quantiteLivree;
        this.quantiteSortie = quantiteSortie;
    }

    public String getCodeSD() {
        return codeSD;
    }

    public void setCodeSD(String codeSD) {
        this.codeSD = codeSD;
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

    public int getQuantiteLivree() {
        return quantiteLivree;
    }

    public void setQuantiteLivree(int quantiteLivree) {
        this.quantiteLivree = quantiteLivree;
    }

    public int getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(int quantiteSortie) {
        this.quantiteSortie = quantiteSortie;
    }
}
