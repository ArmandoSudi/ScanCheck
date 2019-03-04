package com.daawtec.scancheck.entites;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Macaron {

    @SerializedName("codeMacaron")
    public String codeMacaron;

    @SerializedName("codeSecret")
    public String codeSecret;

    @SerializedName("dateFabrication")
    public Date dateFabrication;

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
