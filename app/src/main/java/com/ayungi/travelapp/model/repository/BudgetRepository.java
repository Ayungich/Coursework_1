package com.ayungi.travelapp.model.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ayungi.travelapp.model.data.requests.BudgetCategoryRequestDto;
import com.ayungi.travelapp.model.data.responses.BudgetCategoryDto;
import com.ayungi.travelapp.network.ApiClient;
import com.ayungi.travelapp.network.ApiService;
import com.ayungi.travelapp.utils.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetRepository {
    private static BudgetRepository instance;
    private final ApiService api;

    private BudgetRepository() {
        api = ApiClient.getApiService();
    }

    public static synchronized BudgetRepository getInstance() {
        if (instance == null) {
            instance = new BudgetRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<BudgetCategoryDto>>> getBudgetCategories(Long tripId) {
        MutableLiveData<Resource<List<BudgetCategoryDto>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getBudgetCategories(tripId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<BudgetCategoryDto>> call, @NonNull Response<List<BudgetCategoryDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BudgetCategoryDto>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<BudgetCategoryDto>> createBudgetCategory(BudgetCategoryRequestDto dto) {
        MutableLiveData<Resource<BudgetCategoryDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.createBudgetCategory(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BudgetCategoryDto> call, @NonNull Response<BudgetCategoryDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BudgetCategoryDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<BudgetCategoryDto>> getBudgetCategoryById(Long id) {
        MutableLiveData<Resource<BudgetCategoryDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.getBudgetCategoryById(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BudgetCategoryDto> call, @NonNull Response<BudgetCategoryDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BudgetCategoryDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<BudgetCategoryDto>> updateBudgetCategory(Long id, BudgetCategoryRequestDto dto) {
        MutableLiveData<Resource<BudgetCategoryDto>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.updateBudgetCategory(id, dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BudgetCategoryDto> call, @NonNull Response<BudgetCategoryDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Server error: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BudgetCategoryDto> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteBudgetCategory(Long id) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        api.deleteBudgetCategory(id).enqueue(new Callback<>() {
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
