package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.BadVerification;

import java.util.List;

@Dao
public interface IBadVerificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(BadVerification...badVerifications);

    @Update
    int update(BadVerification...badVerifications);

    @Delete
    int delete(BadVerification...badVerifications);

    @Query("SELECT * FROM BAD_VERIFICACTION")
    List<BadVerification> all();


}
