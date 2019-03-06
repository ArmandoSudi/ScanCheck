package com.daawtec.scancheck.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.daawtec.scancheck.entites.SiteDistribution;

@Dao
public interface ISiteDistributionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(SiteDistribution...siteDistributions);

    @Update
    int update(SiteDistribution...siteDistributions);

    @Delete
    int delete(SiteDistribution...siteDistributions);

}
