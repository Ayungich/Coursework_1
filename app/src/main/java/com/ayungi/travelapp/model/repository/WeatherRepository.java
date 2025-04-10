package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.responses.WeatherResponseDto;
import com.ayungi.travelapp.network.WeatherApiClient;
import com.ayungi.travelapp.network.WeatherApiService;
import com.ayungi.travelapp.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository, который ходит в OpenWeather и возвращает LiveData<Resource<WeatherResponse>>.
 */
public class WeatherRepository {

    private static WeatherRepository instance;
    private final WeatherApiService apiService;
    private static final String API_KEY = "840eb0fa647f6494ea18d8a0760ab18f";

    private WeatherRepository() {
        apiService = WeatherApiClient.getWeatherApiService();
    }

    public static synchronized WeatherRepository getInstance() {
        if (instance == null) {
            instance = new WeatherRepository();
        }
        return instance;
    }

    public LiveData<Resource<WeatherResponseDto>> getWeatherByLocation(double lat, double lon) {
        MutableLiveData<Resource<WeatherResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        apiService.getCurrentWeather(lat, lon, "metric", API_KEY).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponseDto> call,
                                   @NonNull Response<WeatherResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
