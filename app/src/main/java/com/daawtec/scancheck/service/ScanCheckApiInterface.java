package com.daawtec.scancheck.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScanCheckApiInterface {

    @GET("/checkmacaron/{code_macaron}/")
    Call<String> checkMacaron(@Path("code_macaron") String codeMacaron);
}
