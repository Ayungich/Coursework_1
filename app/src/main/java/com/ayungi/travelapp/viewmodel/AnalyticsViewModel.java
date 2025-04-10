package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsResponseDto;
import com.ayungi.travelapp.model.repository.AnalyticsRepository;
import com.ayungi.travelapp.utils.Resource;

public class AnalyticsViewModel extends ViewModel {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsViewModel() {
        analyticsRepository = AnalyticsRepository.getInstance();
    }

    /**
     * Получение аналитики по бюджету поездки.
     *
     * @param tripId      идентификатор поездки
     * @param totalBudget общий бюджет, введённый пользователем (например, 50000)
     * @return LiveData с объектом Resource, содержащим аналитические данные
     */
    public LiveData<Resource<TripBudgetAnalyticsResponseDto>> getTripAnalytics(long tripId, double totalBudget) {
        return analyticsRepository.getTripAnalytics(tripId, totalBudget);
    }
}
