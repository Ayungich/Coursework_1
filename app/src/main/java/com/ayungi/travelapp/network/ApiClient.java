package com.ayungi.travelapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://194.87.110.27:8080/";
//    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofitInstance;

    private ApiClient() {}

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}

