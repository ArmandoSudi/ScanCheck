package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.ValiditeRole;

import java.util.List;

@Dao
public interface IValiditeRoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(ValiditeRole...validiteRoles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<ValiditeRole> validiteRoles);

    @Update
    int update(ValiditeRole...validiteRoles);

    @Delete
    int delete(ValiditeRole...validiteRoles);
}
