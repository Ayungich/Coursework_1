package com.ayungi.travelappserver.budget;

import com.ayungi.travelappserver.dto.budget.BudgetCategoryDto;
import com.ayungi.travelappserver.dto.budget.BudgetCategoryRequestDto;
import com.ayungi.travelappserver.dto.budget.BudgetItemDto;
import com.ayungi.travelappserver.trip.Trip;
import com.ayungi.travelappserver.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetCategoryService {

    private final BudgetCategoryRepository categoryRepository;
    private final TripRepository tripRepository;

    public BudgetCategoryService(BudgetCategoryRepository categoryRepository, TripRepository tripRepository) {
        this.categoryRepository = categoryRepository;
        this.tripRepository = tripRepository;
    }

    @Transactional
    public BudgetCategoryDto createBudgetCategory(BudgetCategoryRequestDto dto) {
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + dto.getTripId()));
        BudgetCategory category = new BudgetCategory();
        category.setName(dto.getName());
        category.setTrip(trip);
        // Если в запросе указана планируемая сумма, сохраняем её
        if(dto.getPlannedAmount() != null) {
            category.setPlannedAmount(dto.getPlannedAmount());
        }
        BudgetCategory saved = categoryRepository.save(category);
        return toDto(saved);
    }

    public List<BudgetCategoryDto> getCategoriesByTrip(Long tripId) {
        return categoryRepository.findByTripId(tripId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetCategoryDto updateBudgetCategory(Long categoryId, BudgetCategoryRequestDto dto) {
        BudgetCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Budget category not found with id: " + categoryId));
        category.setName(dto.getName());
        // Обновляем планируемую сумму, если передано (может быть null)
        category.setPlannedAmount(dto.getPlannedAmount());
        BudgetCategory updated = categoryRepository.save(category);
        return toDto(updated);
    }

    @Transactional
    public void deleteBudgetCategory(Long categoryId) {
        BudgetCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Budget category not found with id: " + categoryId));
        categoryRepository.delete(category);
    }

    private BudgetCategoryDto toDto(BudgetCategory category) {
        BudgetCategoryDto dto = new BudgetCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setPlannedAmount(category.getPlannedAmount());
        if (category.getItems() != null) {
            List<BudgetItemDto> items = category.getItems().stream().map(item -> {
                BudgetItemDto itemDto = new BudgetItemDto();
                itemDto.setId(item.getId());
                itemDto.setName(item.getName());
                itemDto.setAmount(item.getAmount());
                return itemDto;
            }).collect(Collectors.toList());
            dto.setItems(items);
        }
        return dto;
    }
}
