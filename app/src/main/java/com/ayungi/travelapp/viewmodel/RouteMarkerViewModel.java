package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.RouteMarkerResponseDto;
import com.ayungi.travelapp.model.repository.RouteMarkerRepository;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

public class RouteMarkerViewModel extends ViewModel {

    private final RouteMarkerRepository repository;

    public RouteMarkerViewModel() {
        repository = RouteMarkerRepository.getInstance();
    }

    public LiveData<Resource<List<RouteMarkerResponseDto>>> getMarkersByTrip(Long tripId) {
        return repository.getMarkersByTrip(tripId);
    }

    public LiveData<Resource<RouteMarkerResponseDto>> createRouteMarker(Long tripId, RouteMarkerResponseDto request) {
        return repository.createRouteMarker(tripId, request);
    }

    public LiveData<Resource<RouteMarkerResponseDto>> updateRouteMarker(Long id, RouteMarkerResponseDto request) {
        return repository.updateRouteMarker(id, request);
    }

    public LiveData<Resource<Void>> deleteRouteMarker(Long id) {
        return repository.deleteRouteMarker(id);
    }
}
