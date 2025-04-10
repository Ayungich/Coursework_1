package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.UserCredentialsDto;
import com.ayungi.travelapp.model.data.responses.UserResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static UserRepository instance;
    private final ApiService api;

    private UserRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public LiveData<Resource<UserResponseDto>> getUserById(long userId) {
        MutableLiveData<Resource<UserResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.getUserById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call,
                                   @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Ошибка сервера: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<UserResponseDto>> patchUserCredentials(long userId, UserCredentialsDto dto) {
        MutableLiveData<Resource<UserResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.patchUserCredentials(userId, dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call,
                                   @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteUser(long userId) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteUser(userId).enqueue(new Callback<>() {
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
