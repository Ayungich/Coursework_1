package com.ayungi.travelappserver.budget;

import com.ayungi.travelappserver.budgetitem.BudgetItem;
import com.ayungi.travelappserver.trip.Trip;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "budget_categories")
public class BudgetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal plannedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "budgetCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetItem> items;

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

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }
    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<BudgetItem> getItems() {
        return items;
    }
    public void setItems(List<BudgetItem> items) {
        this.items = items;
    }
}
