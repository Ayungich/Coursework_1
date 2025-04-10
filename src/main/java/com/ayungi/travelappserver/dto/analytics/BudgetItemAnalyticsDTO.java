package com.ayungi.travelappserver.dto.analytics;

import java.math.BigDecimal;

public class BudgetItemAnalyticsDTO {
    private String name;
    private BigDecimal amount;

    public BudgetItemAnalyticsDTO() {}

    public BudgetItemAnalyticsDTO(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
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
