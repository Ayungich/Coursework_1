package com.ayungi.travelappserver.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public class BudgetCategoryAnalyticsDTO {
    private String name;
    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private List<BudgetItemAnalyticsDTO> items;

    public BudgetCategoryAnalyticsDTO() {}

    public BudgetCategoryAnalyticsDTO(String name, BigDecimal plannedAmount, BigDecimal spentAmount, List<BudgetItemAnalyticsDTO> items) {
        this.name = name;
        this.plannedAmount = plannedAmount;
        this.spentAmount = spentAmount;
        this.items = items;
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
    public BigDecimal getSpentAmount() {
        return spentAmount;
    }
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }
    public List<BudgetItemAnalyticsDTO> getItems() {
        return items;
    }
    public void setItems(List<BudgetItemAnalyticsDTO> items) {
        this.items = items;
    }
}
