package com.ayungi.travelapp.model.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayungi.travelapp.model.data.responses.AvatarUploadResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarRepository {

    private static AvatarRepository instance;
    private final ApiService api;

    private AvatarRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized AvatarRepository getInstance() {
        if (instance == null) {
            instance = new AvatarRepository();
        }
        return instance;
    }

    public LiveData<Resource<AvatarUploadResponseDto>> uploadAvatar(long userId, File file) {
        MutableLiveData<Resource<AvatarUploadResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        // Создаем RequestBody для файла
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // Создаем MultipartBody.Part
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        api.uploadAvatar(userId, body).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AvatarUploadResponseDto> call,
                                   @NonNull Response<AvatarUploadResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AvatarUploadResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
                Log.e("UploadAvatarRepo", t.getMessage().toString());
            }
        });

        return liveData;
    }

    public LiveData<Resource<Bitmap>> getAvatar(Long avatarId) {
        MutableLiveData<Resource<Bitmap>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        api.getAvatar(avatarId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        liveData.setValue(Resource.success(bitmap));
                    } catch (Exception e) {
                        liveData.setValue(Resource.error("Ошибка декодирования изображения: " + e.getMessage(), null));
                    }
                } else {
                    int code = response.code();
                    liveData.setValue(Resource.error("Ошибка сервера: код=" + code, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Сетевая ошибка: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
