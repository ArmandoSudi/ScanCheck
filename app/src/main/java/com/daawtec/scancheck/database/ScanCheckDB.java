package com.daawtec.scancheck.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.Verification;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.utils.DateConverts;

@Database(entities = {
        AffectationMacaronAS.class,
        AirsSante.class,
        BadVerification.class,
        Macaron.class,
        Menage.class,
        Verification.class,
        ZoneSante.class},
        version = 1
)
@TypeConverters(DateConverts.class)
public abstract class ScanCheckDB extends RoomDatabase {

    private static final String DB_NAME = "scancheck.db";

    private static ScanCheckDB INSTANCE;

    public static ScanCheckDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScanCheckDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ScanCheckDB.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
