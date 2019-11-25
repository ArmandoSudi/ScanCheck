package com.daawtec.scancheck.service;

import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.Campagne;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.MembreMenage;
import com.daawtec.scancheck.entites.Menage;
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

    @GET("sd/")
    Call<List<SiteDistribution>> getSiteDistribution();

    @GET("macarons/")
    Call<List<Macaron>> getMacarons();

    @GET("affectations/")
    Call<List<Affectation>> getAffectations();

    @GET("agents/")
    Call<List<Agent>> getAgents();

    @GET("campagnes/")
    Call<List<Campagne>> getCampagnes();

    @POST("save-menages/")
    Call<String> postMenage(@Body List<Menage> menages);

    @POST("save-macarons/")
    Call<String> postMacarons(@Body List<Macaron> macarons);

    @POST("update-affectation/")
    Call<String> postAffectations(@Body List<Affectation> affectations);

    @POST("save-sites-distribution/")
    Call<String> postSds(@Body List<SiteDistribution> sd);

    @POST("save-membres-menages/")
    Call<String> postMembreMenage(@Body List<MembreMenage> membreMenages);

    //TODO SERVICE WEB A IMPLEMENTER
    //TODO posterAffectationAS, recupererAffectationAS,

    @POST("/")
    Call<String> postBadVerification(List<BadVerification> badVerifications);
    @POST("/")
    Call<String> postInventairesPhysiques(List<InventairePhysique> inventairePhysiques);


    @GET("campagne/{codeCampagne}")
    Call<Campagne> getCampagne(@Path("codeCampagne") String codeCampagne);

    @GET("agent/{codeAuthentification}")
    Call<Agent> getAgent(@Path("codeAuthentification") String codeAuthentification);

    @GET("affectation/{codeAgent}")
    Call<Affectation> getAffectation(@Path("codeAgent") String codeAgent);

    @GET("population-macro-plan/{codeAffectation}/{populationMacroPlan}")
    Call<String> updatePopulationMacroPlan(@Path("codeAffectation") String codeAffectation, @Path("populationMacroPlan") int populationMacroPlan);

}
