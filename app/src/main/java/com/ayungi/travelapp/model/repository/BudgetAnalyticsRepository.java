package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetAnalyticsRepository {
    private static BudgetAnalyticsRepository instance;
    private final ApiService api;

    private BudgetAnalyticsRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized BudgetAnalyticsRepository getInstance() {
        if (instance == null) {
            instance = new BudgetAnalyticsRepository();
        }
        return instance;
    }

    public LiveData<Resource<TripBudgetAnalyticsDto>> getTripBudgetAnalytics(Long tripId) {
        MutableLiveData<Resource<TripBudgetAnalyticsDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getTripBudgetAnalytics(tripId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TripBudgetAnalyticsDto> call, @NonNull Response<TripBudgetAnalyticsDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripBudgetAnalyticsDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }
}
