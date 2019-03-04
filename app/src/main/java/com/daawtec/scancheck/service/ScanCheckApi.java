package com.daawtec.scancheck.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanCheckApi {

    static Gson gson = new GsonBuilder()
            .setDateFormat("dd/mm/yyyy")
            .create();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static final ScanCheckApiInterface SERVICE = RETROFIT.create(ScanCheckApiInterface.class);

    public static ScanCheckApiInterface getService() { return SERVICE;}

}
