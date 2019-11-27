package com.daawtec.scancheck.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.daawtec.scancheck.dao.IAffectationDao;
import com.daawtec.scancheck.dao.IAffectationMacaronASDao;
import com.daawtec.scancheck.dao.IAgentDao;
import com.daawtec.scancheck.dao.IAirSanteDao;
import com.daawtec.scancheck.dao.IBadVerificationDao;
import com.daawtec.scancheck.dao.ICampagneDao;
import com.daawtec.scancheck.dao.IDivisionProvincialSanteDao;
import com.daawtec.scancheck.dao.IIventairePhysiqueDao;
import com.daawtec.scancheck.dao.IMacaronDao;
import com.daawtec.scancheck.dao.IMembreMenageDao;
import com.daawtec.scancheck.dao.IMenageDao;
import com.daawtec.scancheck.dao.IRelaisCommunautaireDao;
import com.daawtec.scancheck.dao.ISiteDistributionDao;
import com.daawtec.scancheck.dao.ITypeMenageDao;
import com.daawtec.scancheck.dao.IValiditeRoleDao;
import com.daawtec.scancheck.dao.IZoneSanteDao;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.Campagne;
import com.daawtec.scancheck.entites.CleRepartitionMenage;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.MembreMenage;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.TypeMenage;
import com.daawtec.scancheck.entites.ValiditeRole;
import com.daawtec.scancheck.entites.Verification;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.utils.DateConverts;

@Database(entities = {
        AffectationMacaronAS.class,
        AirsSante.class,
        BadVerification.class,
        DivisionProvincialeSante.class,
        InventairePhysique.class,
        Macaron.class,
        Menage.class,
        RelaisCommunautaire.class,
        SiteDistribution.class,
        Verification.class,
        ZoneSante.class,
        ValiditeRole.class,
        Agent.class,
        Affectation.class,
        TypeMenage.class,
        MembreMenage.class,
        Campagne.class,},
        version = 27
)
@TypeConverters(DateConverts.class)
public abstract class ScanCheckDB extends RoomDatabase {

    private static final String DB_NAME = "scancheck.db";

    public abstract IAffectationMacaronASDao getIIAffectationMacaronASDao();
    public abstract IAirSanteDao getIAirSanteDao();
    public abstract IBadVerificationDao getIBadVerificationDao();
    public abstract IDivisionProvincialSanteDao getIDivisionProvincialSanteDao();
    public abstract IIventairePhysiqueDao getIIventairePhysiqueDao();
    public abstract IMacaronDao getIMacaronDao();
    public abstract IMenageDao getIMenageDao();
    public abstract IRelaisCommunautaireDao  getIRelaisCommunautaireDao();
    public abstract ISiteDistributionDao getISiteDistributionDao();
    public abstract IZoneSanteDao getIZoneSanteDao();
    public abstract IValiditeRoleDao getIValiditeRoleDao();
    public abstract IAffectationDao getIAffectation();
    public abstract IAgentDao getIAgentDao();
    public abstract ITypeMenageDao getITypeMenageDao();
    public abstract IMembreMenageDao getIMembreMenageDao();
    public abstract ICampagneDao getICampagneDao();


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
