package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="AFFECTATION_MACARON_AS", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS"),
        @ForeignKey(entity = RelaisCommunautaire.class, parentColumns = "CODE_RECO", childColumns = "CODE_RECO")
})
public class AffectationMacaronAS {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAS")
    public String codeAS;

    @ColumnInfo(name="DATE_AFFECTATION_AS")
    @SerializedName("dateAffectationAS")
    public String dateAffectationAS;

    @ColumnInfo(name="CODE_RECO")
    @SerializedName("codeReco")
    public String codeReco;

    @ColumnInfo(name="DATE_AFFECTATION_RECO")
    @SerializedName("dateAffectationReco")
    public String dateAffectationReco;

    @ColumnInfo(name="DATE_VERIFICATION")
    @SerializedName("dateVerification")
    public Date dateVerification;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;


    public AffectationMacaronAS(@NonNull String codeMacaron, String codeAS, String dateAffectationAS, String codeReco, String dateAffectationReco, Date dateVerification, int nombreMild) {
        this.codeMacaron = codeMacaron;
        this.codeAS = codeAS;
        this.dateAffectationAS = dateAffectationAS;
        this.codeReco = codeReco;
        this.dateAffectationReco = dateAffectationReco;
        this.dateVerification = dateVerification;
        this.nombreMild = nombreMild;
    }

    public String getCodeMacaron() {
        return codeMacaron;
    }

    public void setCodeMacaron(String codeMacaron) {
        this.codeMacaron = codeMacaron;
    }

    public String getCodeAS() {
        return codeAS;
    }

    public void setCodeAS(String codeAS) {
        this.codeAS = codeAS;
    }

    public String getDateAffectationAS() {
        return dateAffectationAS;
    }

    public void setDateAffectationAS(String dateAffectationAS) {
        this.dateAffectationAS = dateAffectationAS;
    }

    public String getCodeReco() {
        return codeReco;
    }

    public void setCodeReco(String codeReco) {
        this.codeReco = codeReco;
    }

    public String getDateAffectationReco() {
        return dateAffectationReco;
    }

    public void setDateAffectationReco(String dateAffectationReco) {
        this.dateAffectationReco = dateAffectationReco;
    }

    public Date getDateVerification() {
        return dateVerification;
    }

    public void setDateVerification(Date dateVerification) {
        this.dateVerification = dateVerification;
    }

    public int getNombreMild() {
        return nombreMild;
    }

    public void setNombreMild(int nombreMild) {
        this.nombreMild = nombreMild;
    }
}
