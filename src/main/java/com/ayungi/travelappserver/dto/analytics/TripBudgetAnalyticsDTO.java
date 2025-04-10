package com.ayungi.travelappserver.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public class TripBudgetAnalyticsDTO {
    // Общий бюджет на всю поездку
    private BigDecimal totalBudget;
    // Суммарные затраты по всем категориям
    private BigDecimal totalSpent;
    // Остаток
    private BigDecimal remainingBudget;
    // Список аналитики по категориям
    private List<BudgetCategoryAnalyticsDTO> categories;

    public TripBudgetAnalyticsDTO() {}

    public TripBudgetAnalyticsDTO(BigDecimal totalBudget, BigDecimal totalSpent, BigDecimal remainingBudget, List<BudgetCategoryAnalyticsDTO> categories) {
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
        this.remainingBudget = remainingBudget;
        this.categories = categories;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }
    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }
    public void setRemainingBudget(BigDecimal remainingBudget) {
        this.remainingBudget = remainingBudget;
    }
    public List<BudgetCategoryAnalyticsDTO> getCategories() {
        return categories;
    }
    public void setCategories(List<BudgetCategoryAnalyticsDTO> categories) {
        this.categories = categories;
    }
}
