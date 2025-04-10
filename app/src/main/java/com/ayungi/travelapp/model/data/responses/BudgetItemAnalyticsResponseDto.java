package com.ayungi.travelapp.model.data.responses;

public class BudgetItemAnalyticsResponseDto {
    private String name;
    private double amount;

    public BudgetItemAnalyticsResponseDto() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
