package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="MENAGE", foreignKeys = {
        @ForeignKey(entity = RelaisCommunautaire.class, parentColumns = "CODE_RECO", childColumns = "CODE_RECO"),
        @ForeignKey(entity = SiteDistribution.class, parentColumns = "CODE_SD", childColumns = "CODE_SD")
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
    public String tailleMenage;

    @ColumnInfo(name="DATE_IDENTIFICATION")
    @SerializedName("dateIdentification")
    public Date dateIdentification;

    @ColumnInfo(name="CODE_RECO")
    @SerializedName("codeReco")
    public String codeReco;

    @ColumnInfo(name="CODE_SD")
    @SerializedName("codeSD")
    public String codeSD;

    @ColumnInfo(name="DATE_AFFECTATION_SD")
    @SerializedName("dateAffectationSD")
    public Date dateAffectationSD;

    public Menage(String codeMenage, String nomResponsable, String sexeResponsable, int ageResponsable, String tailleMenage, Date dateIdentification, String codeReco) {
        this.codeMenage = codeMenage;
        this.nomResponsable = nomResponsable;
        this.sexeResponsable = sexeResponsable;
        this.ageResponsable = ageResponsable;
        this.tailleMenage = tailleMenage;
        this.dateIdentification = dateIdentification;
        this.codeReco = codeReco;
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

    public String getTailleMenage() {
        return tailleMenage;
    }

    public void setTailleMenage(String tailleMenage) {
        this.tailleMenage = tailleMenage;
    }

    public Date getDateIdentification() {
        return dateIdentification;
    }

    public void setDateIdentification(Date dateIdentification) {
        this.dateIdentification = dateIdentification;
    }

    public String getCodeReco() {
        return codeReco;
    }

    public void setCodeReco(String codeReco) {
        this.codeReco = codeReco;
    }
}
