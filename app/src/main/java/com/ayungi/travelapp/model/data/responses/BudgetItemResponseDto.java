package com.ayungi.travelapp.model.data.responses;

import java.math.BigDecimal;

public class BudgetItemResponseDto {
    private Long id;
    private String name;
    private BigDecimal amount;

    public BudgetItemResponseDto() { }

    public BudgetItemResponseDto(Long id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
