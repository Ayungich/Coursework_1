package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ayungi.travelapp.model.data.requests.BudgetItemRequestDto;
import com.ayungi.travelapp.model.data.responses.BudgetItemResponseDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetItemRepository {
    private static BudgetItemRepository instance;
    private final ApiService api;

    private BudgetItemRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized BudgetItemRepository getInstance() {
        if (instance == null) {
            instance = new BudgetItemRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<BudgetItemResponseDto>>> getBudgetItems(Long categoryId) {
        MutableLiveData<Resource<List<BudgetItemResponseDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getBudgetItems(categoryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<BudgetItemResponseDto>> call, @NonNull Response<List<BudgetItemResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BudgetItemResponseDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<BudgetItemResponseDto>> createBudgetItem(BudgetItemRequestDto dto) {
        MutableLiveData<Resource<BudgetItemResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.createBudgetItem(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BudgetItemResponseDto> call, @NonNull Response<BudgetItemResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BudgetItemResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<BudgetItemResponseDto>> updateBudgetItem(Long id, BudgetItemRequestDto dto) {
        MutableLiveData<Resource<BudgetItemResponseDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.updateBudgetItem(id, dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BudgetItemResponseDto> call, @NonNull Response<BudgetItemResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BudgetItemResponseDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteBudgetItem(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteBudgetItem(id).enqueue(new Callback<>() {
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
