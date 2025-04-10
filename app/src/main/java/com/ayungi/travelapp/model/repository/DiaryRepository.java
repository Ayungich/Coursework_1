package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.requests.DiaryRequestDto;
import com.ayungi.travelapp.model.data.responses.DiaryResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryRepository {

    private static DiaryRepository instance;
    private final ApiService api;

    private DiaryRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized DiaryRepository getInstance() {
        if (instance == null) {
            instance = new DiaryRepository();
        }
        return instance;
    }

    public LiveData<Resource<DiaryResponseDto>> createDiary(String title, String date, String content) {
        MutableLiveData<Resource<DiaryResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        DiaryRequestDto requestDto = new DiaryRequestDto(title, date, content);
        api.createDiary(requestDto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<DiaryResponseDto> call,
                                   @NonNull Response<DiaryResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiaryResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

    // Получение списка дневников
    public LiveData<Resource<List<DiaryResponseDto>>> getAllDiaries() {
        MutableLiveData<Resource<List<DiaryResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.getAllDiaries().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<DiaryResponseDto>> call,
                                   @NonNull Response<List<DiaryResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DiaryResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

    // Получение дневника по id
    public LiveData<Resource<DiaryResponseDto>> getDiaryById(long diaryId) {
        MutableLiveData<Resource<DiaryResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getDiaryById(diaryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<DiaryResponseDto> call, @NonNull Response<DiaryResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiaryResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    // Обновление дневника по id
    public LiveData<Resource<DiaryResponseDto>> updateDiary(long diaryId, DiaryRequestDto request) {
        MutableLiveData<Resource<DiaryResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.updateDiary(diaryId, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<DiaryResponseDto> call, @NonNull Response<DiaryResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiaryResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteDiary(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteDiary(id).enqueue(new Callback<>() {
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
