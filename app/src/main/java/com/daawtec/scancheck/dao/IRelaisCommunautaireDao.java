package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.v7.widget.RecyclerView;

import com.daawtec.scancheck.entites.RelaisCommunautaire;

import java.util.List;

@Dao
public interface IRelaisCommunautaireDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(RelaisCommunautaire...relaisCommunautaires);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<RelaisCommunautaire> relaisCommunautaires);

    @Update
    int update(RelaisCommunautaire...relaisCommunautaires);

    @Delete
    int delete(RelaisCommunautaire...relaisCommunautaires);

    @Query("SELECT * FROM RELAIS_COMMUNAUTAIRE")
    List<RelaisCommunautaire> all();

    @Query("SELECT * FROM RELAIS_COMMUNAUTAIRE WHERE CODE_AS=:asCode")
    List<RelaisCommunautaire> get(String asCode);
}
