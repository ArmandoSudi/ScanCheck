package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName="MENAGE", foreignKeys = {
        @ForeignKey(entity = SiteDistribution.class, parentColumns = "CODE_SD", childColumns = "CODE_SD", onDelete = CASCADE),
        @ForeignKey(entity = Macaron.class, parentColumns = "CODE_MACARON", childColumns = "CODE_MACARON", onDelete = CASCADE),
        @ForeignKey(entity = Agent.class, parentColumns = "CODE_AGENT", childColumns = "CODE_AGENT_DISTRIBUTION"),
        @ForeignKey(entity = Agent.class, parentColumns = "CODE_AGENT", childColumns = "CODE_AGENT_DENOMBREMENT"),
        @ForeignKey(entity = TypeMenage.class, parentColumns = "CODE_TYPE_MENAGE", childColumns = "CODE_TYPE_MENAGE")
})
public class Menage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MENAGE")
    @SerializedName("codeMenage")
    public String codeMenage;

    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_AGENT_DENOMBREMENT")
    @SerializedName("CodeAgentDenombrement")
    public String codeAgentDenombrement;

    @ColumnInfo(name="CODE_AGENT_DISTRIBUTION")
    @SerializedName("CodeAgentDistribution")
    public String codeAgentDistribution;

    @ColumnInfo(name="CODE_TYPE_MENAGE")
    @SerializedName("CodeTypeMenage")
    public String codeTypeMenage;

    @ColumnInfo(name="NOM_RESPONSABLE")
    @SerializedName("nomResponsable")
    public String nomResponsable;

    @ColumnInfo(name="SEXE_RESPONSABLE")
    @SerializedName("sexeResponsable")
    public String sexeResponsable;

    @ColumnInfo(name="VILLAGE")
    @SerializedName("Village")
    public String village;

    @ColumnInfo(name="TAILLE_MENAGE")
    @SerializedName("tailleMenage")
    public int tailleMenage;

    @ColumnInfo(name="DATE_IDENTIFICATION")
    @SerializedName("dateIdentification")
    public String dateIdentification;

    @ColumnInfo(name="CODE_SD")
    @SerializedName("codeSD")
    public String codeSD;

    @ColumnInfo(name="NOMBRE_MILD")
    @SerializedName("nombreMild")
    public int nombreMild;

    @ColumnInfo(name="LATITUDE")
    @SerializedName("latitude")
    public double latitude;

    @ColumnInfo(name="LONGITUDE")
    @SerializedName("longitude")
    public double longitude;

    @ColumnInfo(name="RECO_NOM")
    @SerializedName("RecoNom")
    public String recoNom;

    @ColumnInfo(name="RECO_PRENOM")
    @SerializedName("RecoPrenom")
    public String recoPrenom;

    @ColumnInfo(name="COMMENTAIRE")
    @SerializedName("Commentaire")
    public String commentaire;

    @ColumnInfo(name="NOMBRE_MILD_SERVI")
    @SerializedName("NombreMildServi")
    public int nombreMildServi;

    @ColumnInfo(name="NOMBRE_COUCHETTE")
    @SerializedName("NombreCouchette")
    public int nombreCouchette;

    @ColumnInfo(name="ETAT_SERVI")
    @SerializedName("etatServi")
    public boolean etatServi;


    public Menage(@NonNull String codeMenage, String nomResponsable, String sexeResponsable, String village, int tailleMenage, String dateIdentification, String codeSD, int nombreMild, double latitude, double longitude, String codeMacaron, boolean etatServi) {
        this.codeMenage = codeMenage;
        this.nomResponsable = nomResponsable;
        this.sexeResponsable = sexeResponsable;
        this.village = village;
        this.tailleMenage = tailleMenage;
        this.dateIdentification = dateIdentification;
        this.codeSD = codeSD;
        this.nombreMild = nombreMild;
        this.latitude = latitude;
        this.longitude = longitude;
        this.codeMacaron = codeMacaron;
        this.etatServi = etatServi;
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

    public String getVillage() {
        return village;
    }

    public void setAgeResponsable(String village) {
        this.village = village;
    }

    public int getTailleMenage() {
        return tailleMenage;
    }

    public void setTailleMenage(int tailleMenage) {
        this.tailleMenage = tailleMenage;
    }

    public String getDateIdentification() {
        return dateIdentification;
    }

    @Override
    public String toString() {
        return "Menage{" +
                "codeMenage='" + codeMenage + '\'' +
                ", nomResponsable='" + nomResponsable + '\'' +
                ", sexeResponsable='" + sexeResponsable + '\'' +
                ", village='" + village + '\'' +
                ", tailleMenage=" + tailleMenage +
                ", dateIdentification='" + dateIdentification + '\'' +
                ", codeSD='" + codeSD + '\'' +
                ", nombreMild=" + nombreMild +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", codeMacaron='" + codeMacaron + '\'' +
                ", codeAgentDenombrement='" + codeAgentDenombrement + '\'' +
                ", codeAgentDistribution='" + codeAgentDistribution + '\'' +
                ", recoNom='" + recoNom + '\'' +
                ", recoPrenom='" + recoPrenom + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", nombreMildServi=" + nombreMildServi +
                ", nombreCouchette=" + nombreCouchette +
                ", etatServi=" + etatServi +
                '}';
    }

    public void setDateIdentification(String dateIdentification) {
        this.dateIdentification = dateIdentification;


    }

}
