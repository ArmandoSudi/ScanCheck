package com.daawtec.scancheck.entites;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Menage {

    @SerializedName("codeMenage")
    public String codeMenage;

    @SerializedName("nomResponsable")
    public String nomResponsable;

    @SerializedName("sexeResponsable")
    public String sexeResponsable;

    @SerializedName("ageResponsable")
    public int ageResponsable;

    @SerializedName("tailleMenage")
    public String tailleMenage;

    @SerializedName("dateIdentification")
    public Date dateIdentification;

    @SerializedName("codeReco")
    public String codeReco;

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
