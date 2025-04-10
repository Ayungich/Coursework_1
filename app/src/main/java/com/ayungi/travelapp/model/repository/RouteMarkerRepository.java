package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.responses.RouteMarkerResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteMarkerRepository {

    private static RouteMarkerRepository instance;
    private final ApiService api;

    private RouteMarkerRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized RouteMarkerRepository getInstance() {
        if (instance == null) {
            instance = new RouteMarkerRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<RouteMarkerResponseDto>>> getMarkersByTrip(Long tripId) {
        MutableLiveData<Resource<List<RouteMarkerResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getMarkersByTrip(tripId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<RouteMarkerResponseDto>> call, @NonNull Response<List<RouteMarkerResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RouteMarkerResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<RouteMarkerResponseDto>> createRouteMarker(Long tripId, RouteMarkerResponseDto request) {
        MutableLiveData<Resource<RouteMarkerResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.createRouteMarker(tripId, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RouteMarkerResponseDto> call, @NonNull Response<RouteMarkerResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteMarkerResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<RouteMarkerResponseDto>> updateRouteMarker(Long id, RouteMarkerResponseDto request) {
        MutableLiveData<Resource<RouteMarkerResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.updateRouteMarker(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RouteMarkerResponseDto> call, @NonNull Response<RouteMarkerResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteMarkerResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteRouteMarker(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteRouteMarker(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success(null));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }
}
