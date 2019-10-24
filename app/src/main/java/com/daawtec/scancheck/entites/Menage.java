package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="MENAGE", foreignKeys = {
        @ForeignKey(entity = SiteDistribution.class, parentColumns = "CODE_SD", childColumns = "CODE_SD"),
        @ForeignKey(entity = Macaron.class, parentColumns = "CODE_MACARON", childColumns = "CODE_MACARON")
})
public class Menage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MENAGE")
    @SerializedName("codeMenage")
    public String codeMenage;

    @ColumnInfo(name="NOM_RESPONSABLE")
    @SerializedName("nomResponsable")
    public String nomResponsable;

    @ColumnInfo(name="SEXE_RESPONSABLE")
    @SerializedName("sexeResponsable")
    public String sexeResponsable;

    @ColumnInfo(name="AGE_RESPONSABLE")
    @SerializedName("ageResponsable")
    public int ageResponsable;

    @ColumnInfo(name="TAILLE_MENAGE")
    @SerializedName("tailleMenage")
    public int tailleMenage;

    @ColumnInfo(name="DATE_IDENTIFICATION")
    @SerializedName("dateIdentification")
    public Date dateIdentification;

    @ColumnInfo(name="CODE_SD")
    @SerializedName("codeSD")
    public String codeSD;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;

    @ColumnInfo(name="LATITUDE")
    @SerializedName("latitude")
    public double latitude;

    @ColumnInfo(name="LONGITUDE")
    @SerializedName("longitude")
    public double longitude;

    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="ETAT_SERVI")
    @SerializedName("etatServi")
    public boolean etatServi;


    public Menage(@NonNull String codeMenage, String nomResponsable, String sexeResponsable, int ageResponsable, int tailleMenage, Date dateIdentification, String codeSD, int nombreMild, double latitude, double longitude, String codeMacaron, boolean etatServi) {
        this.codeMenage = codeMenage;
        this.nomResponsable = nomResponsable;
        this.sexeResponsable = sexeResponsable;
        this.ageResponsable = ageResponsable;
        this.tailleMenage = tailleMenage;
        this.dateIdentification = dateIdentification;
        this.codeSD = codeSD;
        this.nombreMild = nombreMild;
        this.latitude = latitude;
        this.longitude = longitude;
        this.codeMacaron = codeMacaron;
        this.etatServi = etatServi;
    }

    public String getCodeMenage() {
        return codeMenage;
    }

    public void setCodeMenage(String codeMenage) {
        this.codeMenage = codeMenage;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getSexeResponsable() {
        return sexeResponsable;
    }

    public void setSexeResponsable(String sexeResponsable) {
        this.sexeResponsable = sexeResponsable;
    }

    public int getAgeResponsable() {
        return ageResponsable;
    }

    public void setAgeResponsable(int ageResponsable) {
        this.ageResponsable = ageResponsable;
    }

    public int getTailleMenage() {
        return tailleMenage;
    }

    public void setTailleMenage(int tailleMenage) {
        this.tailleMenage = tailleMenage;
    }

    public Date getDateIdentification() {
        return dateIdentification;
    }

    public void setDateIdentification(Date dateIdentification) {
        this.dateIdentification = dateIdentification;
    }


}
