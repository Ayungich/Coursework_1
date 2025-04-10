package com.ayungi.travelappserver.budgetitem;

import com.ayungi.travelappserver.budget.BudgetCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budget_items")
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название статьи расхода, например "Суп"
    @Column(nullable = false)
    private String name;

    // Сумма расхода
    @Column(nullable = false)
    private BigDecimal amount;

    // Связь с BudgetCategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BudgetCategory budgetCategory;

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

    public BudgetCategory getBudgetCategory() {
        return budgetCategory;
    }
    public void setBudgetCategory(BudgetCategory budgetCategory) {
        this.budgetCategory = budgetCategory;
    }
}
