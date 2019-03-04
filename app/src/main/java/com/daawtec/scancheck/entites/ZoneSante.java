package com.daawtec.scancheck.entites;

import com.google.gson.annotations.SerializedName;

public class ZoneSante {

    @SerializedName("codeZS")
    public String codeZS;

    @SerializedName("nomZS")
    public String nomZS;

    @SerializedName("codeDPS")
    public String codeDPS;

    public ZoneSante(String codeZS, String nomZS, String codeDPS) {
        this.codeZS = codeZS;
        this.nomZS = nomZS;
        this.codeDPS = codeDPS;
    }

    public String getCodeZS() {
        return codeZS;
    }

    public void setCodeZS(String codeZS) {
        this.codeZS = codeZS;
    }

    public String getNomZS() {
        return nomZS;
    }

    public void setNomZS(String nomZS) {
        this.nomZS = nomZS;
    }

    public String getCodeDPS() {
        return codeDPS;
    }

    public void setCodeDPS(String codeDPS) {
        this.codeDPS = codeDPS;
    }
}
