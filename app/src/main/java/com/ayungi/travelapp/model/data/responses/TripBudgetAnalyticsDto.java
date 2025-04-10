package com.ayungi.travelapp.model.data.responses;

import java.math.BigDecimal;
import java.util.List;

public class TripBudgetAnalyticsDto {
    private Long tripId;
    private BigDecimal totalPlanned;
    private BigDecimal totalExpenses;
    private BigDecimal variance;
    private List<BudgetCategoryAnalyticsDto> categoryAnalytics;

    public TripBudgetAnalyticsDto() { }

    public TripBudgetAnalyticsDto(Long tripId, BigDecimal totalPlanned, BigDecimal totalExpenses,
                                  BigDecimal variance, List<BudgetCategoryAnalyticsDto> categoryAnalytics) {
        this.tripId = tripId;
        this.totalPlanned = totalPlanned;
        this.totalExpenses = totalExpenses;
        this.variance = variance;
        this.categoryAnalytics = categoryAnalytics;
    }

    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    public BigDecimal getTotalPlanned() {
        return totalPlanned;
    }
    public void setTotalPlanned(BigDecimal totalPlanned) {
        this.totalPlanned = totalPlanned;
    }
    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }
    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
    public BigDecimal getVariance() {
        return variance;
    }
    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }
    public List<BudgetCategoryAnalyticsDto> getCategoryAnalytics() {
        return categoryAnalytics;
    }
    public void setCategoryAnalytics(List<BudgetCategoryAnalyticsDto> categoryAnalytics) {
        this.categoryAnalytics = categoryAnalytics;
    }
}
