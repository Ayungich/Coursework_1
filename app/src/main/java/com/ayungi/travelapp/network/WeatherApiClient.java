package com.ayungi.travelapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Retrofit retrofitInstance;

    private WeatherApiClient() {}

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static WeatherApiService getWeatherApiService() {
        return getInstance().create(WeatherApiService.class);
    }
}

