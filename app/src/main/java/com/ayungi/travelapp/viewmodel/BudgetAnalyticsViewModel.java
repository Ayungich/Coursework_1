package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsDto;
import com.ayungi.travelapp.model.repository.BudgetAnalyticsRepository;
import com.ayungi.travelapp.utils.Resource;

public class BudgetAnalyticsViewModel extends ViewModel {
    private final BudgetAnalyticsRepository analyticsRepository;

    public BudgetAnalyticsViewModel() {
        analyticsRepository = BudgetAnalyticsRepository.getInstance();
    }

    public LiveData<Resource<TripBudgetAnalyticsDto>> getTripBudgetAnalytics(Long tripId) {
        return analyticsRepository.getTripBudgetAnalytics(tripId);
    }
}
