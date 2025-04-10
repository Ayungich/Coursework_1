package com.ayungi.travelappserver.dto.trip;

import com.ayungi.travelappserver.dto.budget.BudgetCategoryDto;
import com.ayungi.travelappserver.dto.coordinate.CoordinateDto;
import com.ayungi.travelappserver.dto.packing.PackingItemDto;
import java.util.List;

public class TripResponseDto {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String type;
    private List<BudgetCategoryDto> budgetCategories;
    private List<PackingItemDto> packingItems;
    private List<CoordinateDto> coordinates; // список координат

    public TripResponseDto() { }

    public TripResponseDto(Long id,
                           String name,
                           String startDate,
                           String endDate,
                           String type,
                           List<BudgetCategoryDto> budgetCategories,
                           List<PackingItemDto> packingItems,
                           List<CoordinateDto> coordinates) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.budgetCategories = budgetCategories;
        this.packingItems = packingItems;
        this.coordinates = coordinates;
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

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BudgetCategoryDto> getBudgetCategories() {
        return budgetCategories;
    }
    public void setBudgetCategories(List<BudgetCategoryDto> budgetCategories) {
        this.budgetCategories = budgetCategories;
    }

    public List<PackingItemDto> getPackingItems() {
        return packingItems;
    }
    public void setPackingItems(List<PackingItemDto> packingItems) {
        this.packingItems = packingItems;
    }

    public List<CoordinateDto> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<CoordinateDto> coordinates) {
        this.coordinates = coordinates;
    }
}
