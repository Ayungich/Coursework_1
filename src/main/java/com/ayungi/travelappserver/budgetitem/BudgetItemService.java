package com.ayungi.travelappserver.budgetitem;

import com.ayungi.travelappserver.budget.BudgetCategory;
import com.ayungi.travelappserver.budget.BudgetCategoryRepository;
import com.ayungi.travelappserver.dto.budget.BudgetItemDto;
import com.ayungi.travelappserver.dto.budget.BudgetItemRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetItemService {

    private final BudgetItemRepository itemRepository;
    private final BudgetCategoryRepository categoryRepository;

    public BudgetItemService(BudgetItemRepository itemRepository, BudgetCategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public BudgetItemDto createBudgetItem(BudgetItemRequestDto dto) {
        BudgetCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Budget category not found with id: " + dto.getCategoryId()));
        BudgetItem item = new BudgetItem();
        item.setName(dto.getName());
        item.setAmount(dto.getAmount());
        item.setBudgetCategory(category);
        BudgetItem saved = itemRepository.save(item);
        return toDto(saved);
    }

    public List<BudgetItemDto> getBudgetItemsByCategory(Long categoryId) {
        return itemRepository.findByBudgetCategoryId(categoryId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetItemDto updateBudgetItem(Long itemId, BudgetItemRequestDto dto) {
        BudgetItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Budget item not found with id: " + itemId));
        item.setName(dto.getName());
        item.setAmount(dto.getAmount());
        BudgetItem updated = itemRepository.save(item);
        return toDto(updated);
    }

    @Transactional
    public void deleteBudgetItem(Long itemId) {
        BudgetItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Budget item not found with id: " + itemId));
        itemRepository.delete(item);
    }

    private BudgetItemDto toDto(BudgetItem item) {
        BudgetItemDto dto = new BudgetItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setAmount(item.getAmount());
        return dto;
    }
}
