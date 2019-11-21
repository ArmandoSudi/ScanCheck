package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "TYPE_MENAGE")
public class TypeMenage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="CODE_TYPE_MENAGE")
    public String CodeTypeMenage;

    @ColumnInfo(name="LIBELLE")
    public String libelle;

    @ColumnInfo(name="CATEGORIE")
    public String categorie;

    public TypeMenage() {
    }

    public TypeMenage(String codeTypeMenage, String libelle, String categorie) {
        this.CodeTypeMenage = codeTypeMenage;
        this.libelle = libelle;
        this.categorie = categorie;
    }
}
