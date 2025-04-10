package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.RegistrationRequestDto;
import com.ayungi.travelapp.model.data.responses.RegistrationResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationRepository {

    private static RegistrationRepository instance;
    private final ApiService api;

    private RegistrationRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized RegistrationRepository getInstance() {
        if (instance == null) {
            instance = new RegistrationRepository();
        }
        return instance;
    }

    public LiveData<Resource<RegistrationResponseDto>> register(RegistrationRequestDto dto) {
        MutableLiveData<Resource<RegistrationResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.register(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseDto> call,
                                   @NonNull Response<RegistrationResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 201 Created
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    if (code == 409) {
                        liveData.setValue(Resource.error("Пользователь уже существует", null));
                    } else {
                        liveData.setValue(Resource.error("Ошибка регистрации: код=" + code, null));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
