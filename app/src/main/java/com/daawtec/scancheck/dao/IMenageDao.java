package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.Menage;

import java.util.List;

@Dao
public interface IMenageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Menage...menages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Menage> menages);

    @Update
    int update(Menage...menages);

    @Delete
    int delete(Menage...menages);

    @Query("SELECT * FROM MENAGE")
    List<Menage> all();

    @Query("SELECT * FROM MENAGE WHERE CODE_AGENT_DENOMBREMENT=:codeAgent")
    List<Menage> getByCodeAgentDenombrement(String codeAgent);

    @Query("SELECT COUNT(*) FROM MENAGE")
    int size();

    @Query("SELECT * FROM MENAGE WHERE CODE_MACARON=:codeMacaron")
    Menage getByCodeMacaron(String codeMacaron);

    @Query("UPDATE MENAGE SET ETAT_SERVI=:state WHERE CODE_MACARON=:codeMacaron")
    int updateMenage(boolean state, String codeMacaron);

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE")
    int getCount();

    @Query("SELECT SUM(NOMBRE_MILD) AS NBR FROM MENAGE WHERE CODE_SD=:codeSd")
    int getNbreMildAttenduByCodeSd(String codeSd);

    @Query("SELECT SUM(NOMBRE_MILD_SERVI) AS NBR FROM MENAGE WHERE CODE_SD=:codeSd")
    int getNbreMildServiByCodeSd(String codeSd);

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE WHERE TAILLE_MENAGE=:tailleMenage AND DATE_IDENTIFICATION=:date")
    int getCountByTailleMenage(int tailleMenage, String date);

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE WHERE TAILLE_MENAGE > 9")
    int getCountZBigMenage();

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE WHERE ETAT_SERVI=:state")
    int getCountMenageServi(boolean state);

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE WHERE DATE_IDENTIFICATION=:day")
    int getNombreMenageByDay(String day);

    @Query("SELECT COUNT(*) AS NBR FROM MENAGE WHERE DATE_IDENTIFICATION=:date")
    int getCountByDate(String date);

    @Query("SELECT * FROM MENAGE WHERE DATE_IDENTIFICATION=:date")
    List<Menage> getListMenageByDate(String date);
}
