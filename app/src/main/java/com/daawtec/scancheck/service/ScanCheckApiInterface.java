package com.daawtec.scancheck.service;

import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.ZoneSante;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScanCheckApiInterface {

    @GET("/checkmacaron/{code_macaron}/")
    Call<String> checkMacaron(@Path("code_macaron") String codeMacaron);

    @GET("/")
    Call<List<DivisionProvincialeSante>> getDPS();
    @GET("/")
    Call<List<ZoneSante>> getZoneSante();
    @GET("/")
    Call<List<AirsSante>> getAirSantes();
    @GET("/")
    Call<List<RelaisCommunautaire>> getReco();
    @GET("/")
    Call<List<Macaron>> getMacarons();
    @GET("/")
    Call<List<SiteDistribution>> getSiteDistribution();

    @POST("/")
    Call<String> postMenage(List<Menage> menages);
    @POST("/")
    Call<String> postMacarons(List<Macaron> macarons);
    @POST("/")
    Call<String> postBadVerification(List<BadVerification> badVerifications);
    @POST("/")
    Call<String> postInventairesPhysiques(List<InventairePhysique> inventairePhysiques);

}
