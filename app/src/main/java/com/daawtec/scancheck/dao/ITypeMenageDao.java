package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.TypeMenage;

import java.util.List;

@Dao
public interface ITypeMenageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(TypeMenage...typeMenages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<TypeMenage> typeMenages);

    @Update
    int update(TypeMenage...typeMenages);

    @Delete
    int delete(TypeMenage...typeMenages);
}
