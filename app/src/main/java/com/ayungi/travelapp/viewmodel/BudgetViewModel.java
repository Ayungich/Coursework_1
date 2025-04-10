package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ayungi.travelapp.model.data.requests.BudgetCategoryRequestDto;
import com.ayungi.travelapp.model.data.responses.BudgetCategoryDto;
import com.ayungi.travelapp.model.repository.BudgetRepository;
import com.ayungi.travelapp.utils.Resource;
import java.math.BigDecimal;
import java.util.List;

public class BudgetViewModel extends ViewModel {
    private final BudgetRepository budgetRepository;

    public BudgetViewModel() {
        budgetRepository = BudgetRepository.getInstance();
    }

    public LiveData<Resource<List<BudgetCategoryDto>>> getBudgetCategories(Long tripId) {
        return budgetRepository.getBudgetCategories(tripId);
    }

    public LiveData<Resource<BudgetCategoryDto>> getBudgetCategoryById(Long id) {
        return budgetRepository.getBudgetCategoryById(id);
    }

    // Создание категории без plannedAmount
    public LiveData<Resource<BudgetCategoryDto>> createBudgetCategory(Long tripId, String name) {
        BudgetCategoryRequestDto dto = new BudgetCategoryRequestDto(tripId, name);
        return budgetRepository.createBudgetCategory(dto);
    }

    // Новая версия: создание с plannedAmount
    public LiveData<Resource<BudgetCategoryDto>> createBudgetCategory(Long tripId, String name, BigDecimal plannedAmount) {
        BudgetCategoryRequestDto dto = new BudgetCategoryRequestDto(tripId, name, plannedAmount);
        return budgetRepository.createBudgetCategory(dto);
    }

    // Обновление категории без plannedAmount
    public LiveData<Resource<BudgetCategoryDto>> updateBudgetCategory(Long id, String name) {
        BudgetCategoryRequestDto dto = new BudgetCategoryRequestDto(null, name);
        return budgetRepository.updateBudgetCategory(id, dto);
    }

    // Новая версия: обновление с plannedAmount
    public LiveData<Resource<BudgetCategoryDto>> updateBudgetCategory(Long id, String name, BigDecimal plannedAmount) {
        BudgetCategoryRequestDto dto = new BudgetCategoryRequestDto(null, name, plannedAmount);
        return budgetRepository.updateBudgetCategory(id, dto);
    }

    public LiveData<Resource<Void>> deleteBudgetCategory(Long id) {
        return budgetRepository.deleteBudgetCategory(id);
    }
}
