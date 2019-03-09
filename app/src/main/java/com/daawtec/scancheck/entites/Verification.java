package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="VERIFICATION")
public class Verification {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_VERIFICATION")
    @SerializedName("codeVerification")
    public String codeVerification;

    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;

    @ColumnInfo(name="DATE_VERIFICATION")
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
