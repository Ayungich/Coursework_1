package com.ayungi.travelapp.model.data.responses;

import java.math.BigDecimal;

public class BudgetCategoryAnalyticsDto {
    private Long categoryId;
    private String name;
    // Планируемая сумма (может быть null)
    private BigDecimal plannedAmount;
    // Суммарные расходы по категориям
    private BigDecimal expenses;
    // Разница: plannedAmount - expenses
    private BigDecimal variance;

    public BudgetCategoryAnalyticsDto() { }

    public BudgetCategoryAnalyticsDto(Long categoryId, String name, BigDecimal plannedAmount,
                                      BigDecimal expenses, BigDecimal variance) {
        this.categoryId = categoryId;
        this.name = name;
        this.plannedAmount = plannedAmount;
        this.expenses = expenses;
        this.variance = variance;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
    public BigDecimal getExpenses() {
        return expenses;
    }
    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }
    public BigDecimal getVariance() {
        return variance;
    }
    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }
}
