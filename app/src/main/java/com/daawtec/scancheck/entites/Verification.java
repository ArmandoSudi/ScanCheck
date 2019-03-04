package com.daawtec.scancheck.entites;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Verification {

    @SerializedName("codeVerification")
    public String codeVerification;

    @SerializedName("codeMacaron")
    public String codeMacaron;

    @SerializedName("nombreMild")
    public int nombreMild;

    @SerializedName("dateVerification")
    public Date dateVerification;

    public Verification(String codeVerification, String codeMacaron, int nombreMild, Date dateVerification) {
        this.codeVerification = codeVerification;
        this.codeMacaron = codeMacaron;
        this.nombreMild = nombreMild;
        this.dateVerification = dateVerification;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public String getCodeMacaron() {
        return codeMacaron;
    }

    public void setCodeMacaron(String codeMacaron) {
        this.codeMacaron = codeMacaron;
    }

    public int getNombreMild() {
        return nombreMild;
    }

    public void setNombreMild(int nombreMild) {
        this.nombreMild = nombreMild;
    }

    public Date getDateVerification() {
        return dateVerification;
    }

    public void setDateVerification(Date dateVerification) {
        this.dateVerification = dateVerification;
    }
}
