package com.ayungi.travelapp.model.data.requests;

import java.math.BigDecimal;

public class BudgetItemRequestDto {
    private Long categoryId;
    private String name;
    private BigDecimal amount;

    public BudgetItemRequestDto() { }

    public BudgetItemRequestDto(Long categoryId, String name, BigDecimal amount) {
        this.categoryId = categoryId;
        this.name = name;
        this.amount = amount;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
