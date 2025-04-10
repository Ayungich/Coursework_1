package com.ayungi.travelappserver.dto.budget;

import java.math.BigDecimal;
import java.util.List;

public class BudgetCategoryDto {
    private Long id;
    private String name;
    private BigDecimal plannedAmount;
    private List<BudgetItemDto> items;

    public BudgetCategoryDto() { }

    public BudgetCategoryDto(Long id, String name, BigDecimal plannedAmount, List<BudgetItemDto> items) {
        this.id = id;
        this.name = name;
        this.plannedAmount = plannedAmount;
        this.items = items;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }
    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public List<BudgetItemDto> getItems() {
        return items;
    }
    public void setItems(List<BudgetItemDto> items) {
        this.items = items;
    }
}
