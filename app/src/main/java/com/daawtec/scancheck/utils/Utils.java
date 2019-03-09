package com.daawtec.scancheck.utils;

import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.ZoneSante;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static List<DivisionProvincialeSante> getDivisionSantes(){
        List<DivisionProvincialeSante> dps = new ArrayList<>();
        dps.add(new DivisionProvincialeSante("1001", "Division P. Sante Konoha"));
        dps.add(new DivisionProvincialeSante("1002", "Division P. Sante Mist"));
        dps.add(new DivisionProvincialeSante("1003", "Division P. Sante Gaara"));

        return dps;
    }

    public static List<ZoneSante> getZoneSantes(){
        List<ZoneSante> zs = new ArrayList<>();
        zs.add(new ZoneSante("1001", "Zone Sante Alpha", "1001"));
        zs.add(new ZoneSante("1002", "Zone Sante Beta", "1001"));
        zs.add(new ZoneSante("1003", "Zone Sante Gama", "1002"));
        zs.add(new ZoneSante("1004", "Zone Sante Romeo", "1002"));
        zs.add(new ZoneSante("1005", "Zone Sante Golf", "1003"));
        return zs;
    }

    public static List<AirsSante> getAireSantes(){
        List<AirsSante> as = new ArrayList<>();
        as.add(new AirsSante("1001", "Aire Sante MBUDI", "1001"));
        as.add(new AirsSante("1002", "Aire Sante MIDEDA", "1002"));
        as.add(new AirsSante("1003", "Aire Sante KIMBUALA", "1004"));
        as.add(new AirsSante("1004", "Aire Sante MIMOZA", "1005"));
        as.add(new AirsSante("1005", "Aire Sante BRIKIN", "1003"));
        return as;
    }

    public static List<RelaisCommunautaire> getReco(){
        List<RelaisCommunautaire> recos = new ArrayList<>();
        recos.add(new RelaisCommunautaire("1001", "Reco IBANDA", "1005"));
        recos.add(new RelaisCommunautaire("1002", "Reco NGUBA", "1001"));
        recos.add(new RelaisCommunautaire("1003", "Reco KADUTU", "1002"));
        recos.add(new RelaisCommunautaire("1004", "Reco BAGIRA", "1004"));
        recos.add(new RelaisCommunautaire("1005", "Reco NYOFU", "1005"));
        recos.add(new RelaisCommunautaire("1006", "Reco LABOTTE", "1003"));
        return recos;
    }

    public static String getTimeStamp(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}
