package com.vuvrecharge.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGetResponse {
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiServices.YOUTUBE_VIDEO)
            .build();
    public ApiServices apiService = retrofit.create(ApiServices.class);
}
