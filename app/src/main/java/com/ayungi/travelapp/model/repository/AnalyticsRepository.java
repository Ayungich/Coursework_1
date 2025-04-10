package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsRepository {

    private static AnalyticsRepository instance;
    private final ApiService analyticsApi;

    private AnalyticsRepository() {
        analyticsApi = ApiClient.getApiService();
    }

    public static synchronized AnalyticsRepository getInstance() {
        if (instance == null) {
            instance = new AnalyticsRepository();
        }
        return instance;
    }

    public LiveData<Resource<TripBudgetAnalyticsResponseDto>> getTripAnalytics(long tripId, double totalBudget) {
        final MutableLiveData<Resource<TripBudgetAnalyticsResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        analyticsApi.getTripAnalytics(tripId, totalBudget).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TripBudgetAnalyticsResponseDto> call,
                                   @NonNull Response<TripBudgetAnalyticsResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripBudgetAnalyticsResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
