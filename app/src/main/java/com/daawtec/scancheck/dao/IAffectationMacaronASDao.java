package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.AffectationMacaronAS;

import java.util.Date;
import java.util.List;

@Dao
public interface IAffectationMacaronASDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(AffectationMacaronAS...affectationMacaronAS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<AffectationMacaronAS> affectationMacaronAS);

    @Update
    int update(AffectationMacaronAS...affectationMacaronAS);

    @Delete
    int delete(AffectationMacaronAS...affectationMacaronAS);

    @Query("SELECT * FROM AFFECTATION_MACARON_AS WHERE CODE_MACARON=:codeMacaron")
    AffectationMacaronAS get(String codeMacaron);

    @Query("SELECT * FROM AFFECTATION_MACARON_AS")
    List<AffectationMacaronAS> all();

    @Query("SELECT SUM(NOMBRE_MILD) FROM AFFECTATION_MACARON_AS")
    int getNombreTotalMILD();

    @Query("SELECT SUM(NOMBRE_MILD) FROM AFFECTATION_MACARON_AS WHERE DATE_VERIFICATION IS NOT NULL")
    int getNombreMILDDistribues();

    @Query("SELECT COUNT(*) FROM AFFECTATION_MACARON_AS")
    int getNombreMenagesIdentifies();

    @Query("SELECT COUNT(CODE_AFFECTATION) FROM AFFECTATION_MACARON_AS WHERE DATE_VERIFICATION IS NOT NULL")
    int getNombreMenagesServis();

}
