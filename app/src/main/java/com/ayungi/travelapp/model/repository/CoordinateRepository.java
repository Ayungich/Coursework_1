package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.CoordinateRequestDto;
import com.ayungi.travelapp.model.data.responses.CoordinateResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoordinateRepository {
    private static CoordinateRepository instance;
    private final ApiService api;

    private CoordinateRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized CoordinateRepository getInstance() {
        if (instance == null) {
            instance = new CoordinateRepository();
        }
        return instance;
    }

    // Получаем все координаты для путешествия
    public LiveData<Resource<List<CoordinateResponseDto>>> getCoordinates(Long tripId) {
        MutableLiveData<Resource<List<CoordinateResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getCoordinatesByTrip(tripId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<CoordinateResponseDto>> call, @NonNull Response<List<CoordinateResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CoordinateResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Создать новую координату для путешествия
    public LiveData<Resource<CoordinateResponseDto>> createCoordinate(Long tripId, CoordinateRequestDto dto) {
        MutableLiveData<Resource<CoordinateResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.createCoordinate(tripId, dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CoordinateResponseDto> call, @NonNull Response<CoordinateResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoordinateResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Удалить координату по ID
    public LiveData<Resource<Void>> deleteCoordinate(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteCoordinate(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success(null));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }
}
