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
        @ForeignKey(entity = RelaisCommunautaire.class, parentColumns = "CODE_RECO", childColumns = "CODE_RECO"),
        @ForeignKey(entity = Menage.class, parentColumns = "CODE_MENAGE", childColumns = "CODE_MENAGE"),
        @ForeignKey(entity = Macaron.class, parentColumns = "CODE_MACARON", childColumns = "CODE_MACARON")
})
public class AffectationMacaronAS {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AFFECTATION")
    @SerializedName("codeAffectation")
    public String codeAffectation;

    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAS")
    public String codeAS;

    @ColumnInfo(name="CODE_RECO")
    @SerializedName("codeReco")
    public String codeReco;

    @ColumnInfo(name="CODE_MENAGE")
    @SerializedName("codeMenage")
    public String codeMenage;

    @ColumnInfo(name="DATE_AFFECTATION_AS")
    @SerializedName("dateAffectationAS")
    public Date dateAffectationAS;

    @ColumnInfo(name="DATE_AFFECTATION_RECO")
    @SerializedName("dateAffectationReco")
    public Date dateAffectationReco;

    @ColumnInfo(name="DATE_VERIFICATION")
    @SerializedName("dateVerification")
    public Date dateVerification;

    @ColumnInfo(name="DATE_AFFECTATION_MENAGE")
    @SerializedName("dateAffectationMenage")
    public Date dateAffectationMenage;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;


    public AffectationMacaronAS() {

    }

    @NonNull
    public String getCodeAffectation() {
        return codeAffectation;
    }

    public void setCodeAffectation(@NonNull String codeAffectation) {
        this.codeAffectation = codeAffectation;
    }

    public String getCodeMenage() {
        return codeMenage;
    }

    public void setCodeMenage(String codeMenage) {
        this.codeMenage = codeMenage;
    }

    public Date getDateAffectationAS() {
        return dateAffectationAS;
    }

    public void setDateAffectationAS(Date dateAffectationAS) {
        this.dateAffectationAS = dateAffectationAS;
    }

    public Date getDateAffectationReco() {
        return dateAffectationReco;
    }

    public void setDateAffectationReco(Date dateAffectationReco) {
        this.dateAffectationReco = dateAffectationReco;
    }

    public Date getDateAffectationMenage() {
        return dateAffectationMenage;
    }

    public void setDateAffectationMenage(Date dateAffectationMenage) {
        this.dateAffectationMenage = dateAffectationMenage;
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

    public String getCodeReco() {
        return codeReco;
    }

    public void setCodeReco(String codeReco) {
        this.codeReco = codeReco;
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
