package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="MACARON")
public class Macaron {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_SECRET")
    @SerializedName("codeSecret")
    public String codeSecret;

    @ColumnInfo(name="DATE_FABRICATION")
    @SerializedName("dateFabrication")
    public Date dateFabrication;

    @ColumnInfo(name="DATE_MISE_EN_SERVICE")
    @SerializedName("dateMiseEnService")
    public Date dateMiseEnService;

    public Macaron(String codeMacaron, String codeSecret, Date dateFabrication, Date dateMiseEnService) {
        this.codeMacaron = codeMacaron;
        this.codeSecret = codeSecret;
        this.dateFabrication = dateFabrication;
        this.dateMiseEnService = dateMiseEnService;
    }

    public String getCodeMacaron() {
        return codeMacaron;
    }

    public void setCodeMacaron(String codeMacaron) {
        this.codeMacaron = codeMacaron;
    }

    public String getCodeSecret() {
        return codeSecret;
    }

    public void setCodeSecret(String codeSecret) {
        this.codeSecret = codeSecret;
    }

    public Date getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public Date getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(Date dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }
}
