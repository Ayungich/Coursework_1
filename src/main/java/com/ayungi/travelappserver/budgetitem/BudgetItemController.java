package com.ayungi.travelappserver.budgetitem;

import com.ayungi.travelappserver.dto.budget.BudgetItemDto;
import com.ayungi.travelappserver.dto.budget.BudgetItemRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/budget-items")
public class BudgetItemController {

    private final BudgetItemService itemService;

    public BudgetItemController(BudgetItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<BudgetItemDto>> getItemsByCategory(@RequestParam("categoryId") Long categoryId) {
        List<BudgetItemDto> dtos = itemService.getBudgetItemsByCategory(categoryId);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/by-category")
    public ResponseEntity<BudgetItemDto> createItem(@RequestBody BudgetItemRequestDto dto) {
        BudgetItemDto created = itemService.createBudgetItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetItemDto> updateItem(@PathVariable Long id, @RequestBody BudgetItemRequestDto dto) {
        BudgetItemDto updated = itemService.updateBudgetItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteBudgetItem(id);
        return ResponseEntity.noContent().build();
    }
}
