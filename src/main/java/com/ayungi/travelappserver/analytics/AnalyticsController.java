package com.ayungi.travelappserver.analytics;

import com.ayungi.travelappserver.dto.analytics.TripBudgetAnalyticsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Получение аналитики по бюджету поездки.
     * Клиент передаёт общий бюджет поездки в параметре totalBudget.
     *
     * Пример запроса:
     * GET /api/analytics/trip/1?totalBudget=50000
     *
     * @param tripId      Идентификатор поездки.
     * @param totalBudget Общий бюджет поездки, передаваемый клиентом.
     * @return JSON с аналитикой бюджета поездки.
     */
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<TripBudgetAnalyticsDTO> getTripAnalytics(
            @PathVariable Long tripId,
            @RequestParam("totalBudget") BigDecimal totalBudget) {
        TripBudgetAnalyticsDTO analyticsDTO = analyticsService.getTripBudgetAnalytics(tripId, totalBudget);
        return ResponseEntity.ok(analyticsDTO);
    }
}
