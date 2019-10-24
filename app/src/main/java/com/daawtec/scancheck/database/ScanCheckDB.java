package com.daawtec.scancheck.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.daawtec.scancheck.dao.IAffectationMacaronASDao;
import com.daawtec.scancheck.dao.IAgentDenombrement;
import com.daawtec.scancheck.dao.IAirSanteDao;
import com.daawtec.scancheck.dao.IBadVerificationDao;
import com.daawtec.scancheck.dao.IDivisionProvincialSanteDao;
import com.daawtec.scancheck.dao.IIventairePhysiqueDao;
import com.daawtec.scancheck.dao.IMacaronDao;
import com.daawtec.scancheck.dao.IMenageDao;
import com.daawtec.scancheck.dao.IRelaisCommunautaireDao;
import com.daawtec.scancheck.dao.ISiteDistributionDao;
import com.daawtec.scancheck.dao.IZoneSanteDao;
import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.Verification;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.utils.Converter;
import com.daawtec.scancheck.utils.DateConverts;

@Database(entities = {
        AffectationMacaronAS.class,
        AirsSante.class,
        AgentDenombrement.class,
        BadVerification.class,
        DivisionProvincialeSante.class,
        InventairePhysique.class,
        Macaron.class,
        Menage.class,
        RelaisCommunautaire.class,
        SiteDistribution.class,
        Verification.class,
        ZoneSante.class},
        version = 8
)
@TypeConverters(DateConverts.class)
public abstract class ScanCheckDB extends RoomDatabase {

    private static final String DB_NAME = "scancheck.db";

    public abstract IAffectationMacaronASDao getIIAffectationMacaronASDao();
    public abstract IAirSanteDao getIAirSanteDao();
    public abstract IAgentDenombrement getIAgentDenombrementDao();
    public abstract IBadVerificationDao getIBadVerificationDao();
    public abstract IDivisionProvincialSanteDao getIDivisionProvincialSanteDao();
    public abstract IIventairePhysiqueDao getIIventairePhysiqueDao();
    public abstract IMacaronDao getIMacaronDao();
    public abstract IMenageDao getIMenageDao();
    public abstract IRelaisCommunautaireDao  getIRelaisCommunautaireDao();
    public abstract ISiteDistributionDao getISiteDistributionDao();
    public abstract IZoneSanteDao getIZoneSanteDao();

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
