package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AFFECTATION", foreignKeys = {
        @ForeignKey(entity = Agent.class, childColumns = "CODE_AGENT", parentColumns = "CODE_AGENT"),
        @ForeignKey(entity = AirsSante.class, childColumns = "CODE_AS", parentColumns = "CODE_AS")
})

public class Affectation {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_AFFECTAITON")
    @SerializedName("CodeAffectation")
    public String codeAffectation;

    @ColumnInfo(name="CODE_AGENT")
    @SerializedName("CodeAgent")
    public String codeAgent;

    @ColumnInfo(name="CODE_AS")
    public String CodeAs;

    @ColumnInfo(name="CODE_TYPE_AGENT")
    @SerializedName("CodeTypeAgent")
    public String codeTypeAgent;

    @ColumnInfo(name="DATE_AFFECTATION")
    @SerializedName("DateAffectationSTR")
    public String dateAffectation;

    @ColumnInfo(name="POPULATION_MACROPLAN")
    @SerializedName("PopulationMacronPlan")
    public int populationMacroPlan;

    @ColumnInfo(name="CODE_CAMPAGNE")
    public String CodeCampagne;

    public Affectation() {
    }

    public Affectation(@NonNull String codeAffectation, String codeAgent, String codeTypeAgent, String codeAs, String dateAffectation) {
        this.codeAffectation = codeAffectation;
        this.codeAgent = codeAgent;
        this.codeTypeAgent = codeTypeAgent;
        this.dateAffectation = dateAffectation;
        this.CodeAs = codeAs;
    }

}
