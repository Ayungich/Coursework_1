package com.ayungi.travelapp.model.data.responses;

import java.util.List;

public class TripBudgetAnalyticsResponseDto {
    private double totalBudget;
    private double totalSpent;
    private double remainingBudget;
    private List<BudgetCategoryAnalyticsResponseDto> categories;

    public TripBudgetAnalyticsResponseDto() {
    }

    public double getTotalBudget() {
        return totalBudget;
    }
    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }
    public double getTotalSpent() {
        return totalSpent;
    }
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
    public double getRemainingBudget() {
        return remainingBudget;
    }
    public void setRemainingBudget(double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }
    public List<BudgetCategoryAnalyticsResponseDto> getCategories() {
        return categories;
    }
    public void setCategories(List<BudgetCategoryAnalyticsResponseDto> categories) {
        this.categories = categories;
    }
}
