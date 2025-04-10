package com.ayungi.travelapp.model.data.responses;

import java.util.List;

public class BudgetCategoryAnalyticsResponseDto {
    private String name;
    private double plannedAmount;
    private double spentAmount;
    private List<BudgetItemAnalyticsResponseDto> items;

    public BudgetCategoryAnalyticsResponseDto() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPlannedAmount() {
        return plannedAmount;
    }
    public void setPlannedAmount(double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
    public double getSpentAmount() {
        return spentAmount;
    }
    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }
    public List<BudgetItemAnalyticsResponseDto> getItems() {
        return items;
    }
    public void setItems(List<BudgetItemAnalyticsResponseDto> items) {
        this.items = items;
    }
}
