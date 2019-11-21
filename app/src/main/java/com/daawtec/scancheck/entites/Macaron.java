package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="MACARON", foreignKeys = {
        @ForeignKey(entity=Agent.class, childColumns = "CODE_AGENT", parentColumns = "CODE_AGENT")
})
public class Macaron {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_MACARON")
    @SerializedName("codeMacaron")
    public String codeMacaron;

    @ColumnInfo(name="CODE_AS")
    @SerializedName("codeAs")
    public String codeAs;

    @ColumnInfo(name="CODE_AGENT")
    @SerializedName("codeAgent")
    public String codeAgent;

    @ColumnInfo(name="DATE_ENREGISTREMENT")
    @SerializedName("dateEnregistrement")
    public String dateEnregistrement;

    @ColumnInfo(name="IS_AFFECTED")
    @SerializedName("isAffected")
    public boolean isAffected;


    public Macaron(@NonNull String codeMacaron, String codeAs, String codeAgent, String dateEnregistrement, boolean isAffected) {
        this.codeMacaron = codeMacaron;
        this.codeAs = codeAs;
        this.codeAgent = codeAgent;
        this.dateEnregistrement = dateEnregistrement;
        this.isAffected = isAffected;
    }

    public String getCodeMacaron() {
        return codeMacaron;
    }

    public void setCodeMacaron(String codeMacaron) {
        this.codeMacaron = codeMacaron;
    }

    @Override
    public String toString() {
        return "Macaron{" +
                "codeMacaron='" + codeMacaron + '\'' +
                ", codeAs='" + codeAs + '\'' +
                ", codeAgent='" + codeAgent + '\'' +
                ", dateEnregistrement='" + dateEnregistrement + '\'' +
                ", isAffected=" + isAffected +
                '}';
    }
}
