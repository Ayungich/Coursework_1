package com.ayungi.travelapp.model.data.responses;

import java.math.BigDecimal;

public class BudgetResponseDto {
    private Long id;
    private String category;
    private BigDecimal amount;

    public BudgetResponseDto() {}

    public BudgetResponseDto(Long id, String category, BigDecimal amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
