package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.LoginRequestDto;
import com.ayungi.travelapp.model.data.responses.LoginResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static LoginRepository instance;
    private final ApiService api;

    private LoginRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public LiveData<Resource<LoginResponseDto>> login(String email, String password) {
        MutableLiveData<Resource<LoginResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        api.login(requestDto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseDto> call,
                                   @NonNull Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    switch (code) {
                        case 401:
                            liveData.setValue(Resource.error("Неверный пароль", null));
                            break;
                        case 404:
                            liveData.setValue(Resource.error("Пользователь не найден", null));
                            break;
                        default:
                            liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
