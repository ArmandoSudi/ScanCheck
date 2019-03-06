package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.InventairePhysique;

@Dao
public interface IIventairePhysiqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(InventairePhysique...inventairePhysiques);

    @Update
    int update(InventairePhysique...inventairePhysiques);

    @Delete
    int delete(InventairePhysique...inventairePhysiques);
}
