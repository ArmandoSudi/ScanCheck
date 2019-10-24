package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName="MACARON", foreignKeys = {
        @ForeignKey(entity=AgentDenombrement.class, childColumns = "CODE_AGENT_DENOMBREMENT", parentColumns = "CODE_AGENT_DENOMBREMENT")
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

    @ColumnInfo(name="CODE_AGENT_DENOMBREMENT")
    @SerializedName("codeAgentDenombrement")
    public String codeAgentDenombrement;

    @ColumnInfo(name="DATE_ENREGISTREMENT")
    @SerializedName("dateEnregistrement")
    public Date dateEnregistrement;

    @ColumnInfo(name="IS_AFFECTED")
    @SerializedName("isAffected")
    public boolean isAffected;


    public Macaron(@NonNull String codeMacaron, String codeAs, String codeAgentDenombrement, Date dateEnregistrement, boolean isAffected) {
        this.codeMacaron = codeMacaron;
        this.codeAs = codeAs;
        this.codeAgentDenombrement = codeAgentDenombrement;
        this.dateEnregistrement = dateEnregistrement;
        this.isAffected = isAffected;
    }

    public String getCodeMacaron() {
        return codeMacaron;
    }

    public void setCodeMacaron(String codeMacaron) {
        this.codeMacaron = codeMacaron;
    }

}
