package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ayungi.travelapp.model.data.requests.TripRequestDto;
import com.ayungi.travelapp.model.data.responses.TripResponseDto;
import com.ayungi.travelapp.model.repository.TripRepository;
import com.ayungi.travelapp.utils.Resource;
import java.util.List;

public class TripViewModel extends ViewModel {
    private final TripRepository tripRepository;

    public TripViewModel() {
        tripRepository = TripRepository.getInstance();
    }

    public LiveData<Resource<TripResponseDto>> createTrip(String name, String startDate, String endDate, String type) {
        TripRequestDto request = new TripRequestDto(name, startDate, endDate, type);
        return tripRepository.createTrip(request);
    }

    public LiveData<Resource<List<TripResponseDto>>> getAllTrips() {
        return tripRepository.getAllTrips();
    }

    public LiveData<Resource<TripResponseDto>> updateTrip(long id, TripRequestDto request) {
        return tripRepository.updateTrip(id, request);
    }

    public LiveData<Resource<Void>> deleteTrip(Long id) {
        return tripRepository.deleteTrip(id);
    }
}
