package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="AFFECTATION_MACARON_AS", foreignKeys = {
        @ForeignKey(entity = AirsSante.class, parentColumns = "CODE_AS", childColumns = "CODE_AS"),
        @ForeignKey(entity = RelaisCommunautaire.class, parentColumns = "CODE_RECO", childColumns = "CODE_RECO"),
        @ForeignKey(entity = Macaron.class, parentColumns = "CODE_MACARON", childColumns = "CODE_MACARON")
})
public class AffectationMacaronAS {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AFFECTATION")
    @SerializedName("codeMacaronAs")
    public String codeAffectation;

    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAs")
    public String codeAS;

    @ColumnInfo(name="CODE_RECO")
    @SerializedName("codeReco")
    public String codeReco;

    @Nullable
    @ColumnInfo(name="DATE_AFFECTATION_AS")
    @SerializedName("dateAffectationAs")
    public String dateAffectationAS;

    @Nullable
    @ColumnInfo(name="DATE_AFFECTATION_RECO")
    @SerializedName("dateAffectationReco")
    public String dateAffectationReco;

    @ColumnInfo(name="DATE_VERIFICATION")
    @SerializedName("dateVerification")
    @Nullable
    public String dateVerification;

    @Nullable
    @ColumnInfo(name="DATE_AFFECTATION_MENAGE")
    @SerializedName("dateAffectationMenage")
    public String dateAffectationMenage;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;


    public AffectationMacaronAS() {

    }

    public AffectationMacaronAS(@NonNull String codeAffectation, String codeMacaron, String dateAffectationAS, String dateAffectationReco) {
        this.codeAffectation = codeAffectation;
        this.codeMacaron = codeMacaron;
        this.dateAffectationAS = dateAffectationAS;
        this.dateAffectationReco = dateAffectationReco;
    }

    @NonNull
    public String getCodeAffectation() {
        return codeAffectation;
    }

    public void setCodeAffectation(@NonNull String codeAffectation) {
        this.codeAffectation = codeAffectation;
    }

    public String getDateAffectationAS() {
        return dateAffectationAS;
    }

    public void setDateAffectationAS(String dateAffectationAS) {
        this.dateAffectationAS = dateAffectationAS;
    }

    public String getDateAffectationReco() {
        return dateAffectationReco;
    }

    public void setDateAffectationReco(String dateAffectationReco) {
        this.dateAffectationReco = dateAffectationReco;
    }

    public String getDateAffectationMenage() {
        return dateAffectationMenage;
    }

    public void setDateAffectationMenage(String dateAffectationMenage) {
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

    public String getDateVerification() {
        return dateVerification;
    }

    public void setDateVerification(String dateVerification) {
        this.dateVerification = dateVerification;
    }

    public int getNombreMild() {
        return nombreMild;
    }

    public void setNombreMild(int nombreMild) {
        this.nombreMild = nombreMild;
    }

    @Override
    public String toString() {
        return "AffectationMacaronAS{" +
                "codeAffectation='" + codeAffectation + '\'' +
                ", codeMacaron='" + codeMacaron + '\'' +
                ", codeAS='" + codeAS + '\'' +
                ", codeReco='" + codeReco + '\'' +
                ", dateAffectationAS='" + dateAffectationAS + '\'' +
                ", dateAffectationReco='" + dateAffectationReco + '\'' +
                ", dateVerification='" + dateVerification + '\'' +
                ", dateAffectationMenage='" + dateAffectationMenage + '\'' +
                ", nombreMild=" + nombreMild +
                '}';
    }
}
