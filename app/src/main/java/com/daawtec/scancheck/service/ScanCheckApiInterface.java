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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScanCheckApiInterface {

    @GET("/")
    Call<String> checkMacaron(@Path("code_macaron") String codeMacaron);

    @GET("dps/")
    Call<List<DivisionProvincialeSante>> getDPS();
    @GET("zs/")
    Call<List<ZoneSante>> getZoneSante();
    @GET("as/")
    Call<List<AirsSante>> getAirSantes();
    @GET("recos/")
    Call<List<RelaisCommunautaire>> getReco();
    @GET("macarons/")
    Call<List<Macaron>> getMacarons();
    @GET("sd/")
    Call<List<SiteDistribution>> getSiteDistribution();

    @POST("saveMenages/")
    Call<String> postMenage(@Body List<Menage> menages);
    @POST("saveMacarons/")
    Call<String> postMacarons(@Body List<Macaron> macarons);

    @POST("/")
    Call<String> postBadVerification(List<BadVerification> badVerifications);
    @POST("/")
    Call<String> postInventairesPhysiques(List<InventairePhysique> inventairePhysiques);



}
