package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ayungi.travelapp.model.data.requests.BudgetItemRequestDto;
import com.ayungi.travelapp.model.data.responses.BudgetItemResponseDto;
import com.ayungi.travelapp.model.repository.BudgetItemRepository;
import com.ayungi.travelapp.utils.Resource;
import java.util.List;

public class BudgetItemViewModel extends ViewModel {
    private final BudgetItemRepository budgetItemRepository;

    public BudgetItemViewModel() {
        budgetItemRepository = BudgetItemRepository.getInstance();
    }

    public LiveData<Resource<List<BudgetItemResponseDto>>> getBudgetItems(Long categoryId) {
        return budgetItemRepository.getBudgetItems(categoryId);
    }

    public LiveData<Resource<BudgetItemResponseDto>> createBudgetItem(Long categoryId, String name, String amount) {
        BudgetItemRequestDto dto = new BudgetItemRequestDto(categoryId, name, new java.math.BigDecimal(amount));
        return budgetItemRepository.createBudgetItem(dto);
    }

    public LiveData<Resource<BudgetItemResponseDto>> updateBudgetItem(Long id, String name, String amount) {
        // При обновлении categoryId не требуется (будет сохранен прежний), поэтому передаем null.
        BudgetItemRequestDto dto = new BudgetItemRequestDto(null, name, new java.math.BigDecimal(amount));
        return budgetItemRepository.updateBudgetItem(id, dto);
    }

    public LiveData<Resource<Void>> deleteBudgetItem(Long id) {
        return budgetItemRepository.deleteBudgetItem(id);
    }
}
