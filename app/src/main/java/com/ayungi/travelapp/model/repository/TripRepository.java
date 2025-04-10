package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ayungi.travelapp.model.data.requests.TripRequestDto;
import com.ayungi.travelapp.model.data.responses.TripResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRepository {
    private static TripRepository instance;
    private final ApiService api;

    private TripRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized TripRepository getInstance() {
        if (instance == null) {
            instance = new TripRepository();
        }
        return instance;
    }

    public LiveData<Resource<TripResponseDto>> createTrip(TripRequestDto request) {
        MutableLiveData<Resource<TripResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.createTrip(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TripResponseDto> call, @NonNull Response<TripResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<TripResponseDto>>> getAllTrips() {
        MutableLiveData<Resource<List<TripResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getAllTrips().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TripResponseDto>> call, @NonNull Response<List<TripResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TripResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<TripResponseDto>> updateTrip(long id, TripRequestDto request) {
        MutableLiveData<Resource<TripResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.updateTrip(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TripResponseDto> call, @NonNull Response<TripResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteTrip(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteTrip(id).enqueue(new Callback<>() {
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
