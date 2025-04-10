package com.ayungi.travelappserver.trip;

import com.ayungi.travelappserver.budget.BudgetCategory;
import com.ayungi.travelappserver.coordinate.TripCoordinate;
import com.ayungi.travelappserver.tripitem.PackingItem;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Даты начала и конца путешествия
    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String type;

    // Связь с бюджетными категориями
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> budgetCategories;

    // Связь с вещами для упаковки
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackingItem> packingItems;

    // Новая связь с координатами (отдельная сущность)
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripCoordinate> coordinates;

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getType() {
        return type;
    }
    public void setType(String type) { this.type = type; }

    public List<BudgetCategory> getBudgetCategories() {
        return budgetCategories;
    }
    public void setBudgetCategories(List<BudgetCategory> budgetCategories) {
        this.budgetCategories = budgetCategories;
    }

    public List<PackingItem> getPackingItems() {
        return packingItems;
    }
    public void setPackingItems(List<PackingItem> packingItems) { this.packingItems = packingItems; }

    public List<TripCoordinate> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<TripCoordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
