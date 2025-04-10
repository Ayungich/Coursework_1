package com.ayungi.travelappserver.analytics;

import com.ayungi.travelappserver.budget.BudgetCategory;
import com.ayungi.travelappserver.budgetitem.BudgetItem;
import com.ayungi.travelappserver.dto.analytics.BudgetCategoryAnalyticsDTO;
import com.ayungi.travelappserver.dto.analytics.BudgetItemAnalyticsDTO;
import com.ayungi.travelappserver.dto.analytics.TripBudgetAnalyticsDTO;
import com.ayungi.travelappserver.trip.Trip;
import com.ayungi.travelappserver.trip.TripRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final TripRepository tripRepository;

    public AnalyticsService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * Метод для получения аналитики бюджета поездки.
     * @param tripId Идентификатор поездки.
     * @param totalBudget Общий бюджет, передаваемый от клиента.
     * @return TripBudgetAnalyticsDTO с информацией по категориям, сумме расходов и остатком бюджета.
     */
    public TripBudgetAnalyticsDTO getTripBudgetAnalytics(Long tripId, BigDecimal totalBudget) {
        // Получаем сущность Trip
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));

        // Получаем список категорий бюджета, привязанных к поездке
        List<BudgetCategory> categories = trip.getBudgetCategories();

        List<BudgetCategoryAnalyticsDTO> categoryAnalytics = new ArrayList<>();
        BigDecimal totalSpent = BigDecimal.ZERO;

        for (BudgetCategory category : categories) {
            // Вычисляем сумму расходов в категории по BudgetItem'ам
            BigDecimal spentAmount = category.getItems().stream()
                    .map(BudgetItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalSpent = totalSpent.add(spentAmount);

            // Формируем аналитические данные для каждой статьи расходов
            List<BudgetItemAnalyticsDTO> itemsAnalytics = category.getItems().stream()
                    .map(item -> new BudgetItemAnalyticsDTO(item.getName(), item.getAmount()))
                    .collect(Collectors.toList());

            BudgetCategoryAnalyticsDTO categoryDTO = new BudgetCategoryAnalyticsDTO(
                    category.getName(),
                    category.getPlannedAmount(),
                    spentAmount,
                    itemsAnalytics
            );

            categoryAnalytics.add(categoryDTO);
        }

        // Расчет остатка бюджета исходя из переданного общего бюджета от клиента
        BigDecimal remainingBudget = totalBudget.subtract(totalSpent);

        return new TripBudgetAnalyticsDTO(totalBudget, totalSpent, remainingBudget, categoryAnalytics);
    }
}
