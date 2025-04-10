package com.ayungi.travelappserver.budget;

import com.ayungi.travelappserver.dto.budget.BudgetCategoryDto;
import com.ayungi.travelappserver.dto.budget.BudgetCategoryRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/budget-categories")
public class BudgetCategoryController {

    private final BudgetCategoryService categoryService;

    public BudgetCategoryController(BudgetCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/by-trip")
    public ResponseEntity<List<BudgetCategoryDto>> getCategoriesByTrip(@RequestParam("tripId") Long tripId) {
        List<BudgetCategoryDto> dtos = categoryService.getCategoriesByTrip(tripId);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/by-trip")
    public ResponseEntity<BudgetCategoryDto> createCategory(@RequestBody BudgetCategoryRequestDto dto) {
        BudgetCategoryDto created = categoryService.createBudgetCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetCategoryDto> updateCategory(@PathVariable Long id, @RequestBody BudgetCategoryRequestDto dto) {
        BudgetCategoryDto updated = categoryService.updateBudgetCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteBudgetCategory(id);
        return ResponseEntity.noContent().build();
    }
}
