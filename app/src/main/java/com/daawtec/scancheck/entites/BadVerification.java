package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "BAD_VERIFICACTION")
public class BadVerification {

    @SerializedName("codeBadVerification")
    @ColumnInfo(name = "CODE_BAD_VERIFICATION")
    public String codeBadVerification;

    @ColumnInfo(name="RAISON_ECHEC_VERIFICATION")
    @SerializedName("raisonEchecVerification")
    public String raisonEchecVerification;

    @ColumnInfo(name="DONNEE_RECUPEREE")
    @SerializedName("donneeRecuperee")
    public String donneeRecuperee;

    @ColumnInfo(name="DATE")
    @SerializedName("date")
    public Date date;

    public BadVerification(String codeBadVerification, String raisonEchecVerification, String donneeRecuperee, Date date) {
        this.codeBadVerification = codeBadVerification;
        this.raisonEchecVerification = raisonEchecVerification;
        this.donneeRecuperee = donneeRecuperee;
        this.date = date;
    }

    public String getCodeBadVerification() {
        return codeBadVerification;
    }

    public void setCodeBadVerification(String codeBadVerification) {
        this.codeBadVerification = codeBadVerification;
    }

    public String getRaisonEchecVerification() {
        return raisonEchecVerification;
    }

    public void setRaisonEchecVerification(String raisonEchecVerification) {
        this.raisonEchecVerification = raisonEchecVerification;
    }

    public String getDonneeRecuperee() {
        return donneeRecuperee;
    }

    public void setDonneeRecuperee(String donneeRecuperee) {
        this.donneeRecuperee = donneeRecuperee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
