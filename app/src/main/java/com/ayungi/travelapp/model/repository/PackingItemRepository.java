package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.PackingItemEditRequestDto;
import com.ayungi.travelapp.model.data.requests.PackingItemRequestDto;
import com.ayungi.travelapp.model.data.requests.PackingItemUpdateRequestDto;
import com.ayungi.travelapp.model.data.responses.PackingItemResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackingItemRepository {

    private static PackingItemRepository instance;
    private final ApiService api;

    private PackingItemRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized PackingItemRepository getInstance() {
        if (instance == null) {
            instance = new PackingItemRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<PackingItemResponseDto>>> getAllPackingItems(Long tripId) {
        MutableLiveData<Resource<List<PackingItemResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.getAllPackingItems(tripId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PackingItemResponseDto>> call, @NonNull Response<List<PackingItemResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PackingItemResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<PackingItemResponseDto>> createPackingItem(String name, Boolean taken, Long tripId) {
        MutableLiveData<Resource<PackingItemResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        PackingItemRequestDto request = new PackingItemRequestDto(name, taken, tripId);
        api.createPackingItem(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PackingItemResponseDto> call, @NonNull Response<PackingItemResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PackingItemResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<PackingItemResponseDto>> updatePackingItem(Long id, Boolean taken) {
        MutableLiveData<Resource<PackingItemResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        PackingItemUpdateRequestDto request = new PackingItemUpdateRequestDto(taken);
        api.updatePackingItem(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PackingItemResponseDto> call,
                                   @NonNull Response<PackingItemResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PackingItemResponseDto> call,
                                  @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<PackingItemResponseDto>> editPackingItem(Long id, PackingItemEditRequestDto request) {
        MutableLiveData<Resource<PackingItemResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.editPackingItem(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PackingItemResponseDto> call,
                                   @NonNull Response<PackingItemResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PackingItemResponseDto> call,
                                  @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deletePackingItem(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deletePackingItem(id).enqueue(new Callback<>() {
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
