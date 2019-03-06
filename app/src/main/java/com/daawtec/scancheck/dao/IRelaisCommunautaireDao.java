package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.RelaisCommunautaire;

@Dao
public interface IRelaisCommunautaireDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(RelaisCommunautaire...relaisCommunautaires);

    @Update
    int update(RelaisCommunautaire...relaisCommunautaires);

    @Delete
    int delete(RelaisCommunautaire...relaisCommunautaires);
}
