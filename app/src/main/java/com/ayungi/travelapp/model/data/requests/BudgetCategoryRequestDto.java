package com.ayungi.travelapp.model.data.requests;

import java.math.BigDecimal;

public class BudgetCategoryRequestDto {
    private Long tripId;
    private String name;
    private BigDecimal plannedAmount;

    public BudgetCategoryRequestDto() { }

    public BudgetCategoryRequestDto(Long tripId, String name) {
        this.tripId = tripId;
        this.name = name;
    }

    public BudgetCategoryRequestDto(Long tripId, String name, BigDecimal plannedAmount) {
        this.tripId = tripId;
        this.name = name;
        this.plannedAmount = plannedAmount;
    }

    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
}
