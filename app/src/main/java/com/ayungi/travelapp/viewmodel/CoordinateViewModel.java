package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.requests.CoordinateRequestDto;
import com.ayungi.travelapp.model.data.responses.CoordinateResponseDto;
import com.ayungi.travelapp.model.repository.CoordinateRepository;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

public class CoordinateViewModel extends ViewModel {
    private final CoordinateRepository coordinateRepository;

    public CoordinateViewModel() {
        coordinateRepository = CoordinateRepository.getInstance();
    }

    // Получить все координаты для путешествия
    public LiveData<Resource<List<CoordinateResponseDto>>> getCoordinates(Long tripId) {
        return coordinateRepository.getCoordinates(tripId);
    }

    // Создать координату
    public LiveData<Resource<CoordinateResponseDto>> createCoordinate(Long tripId, double latitude, double longitude, String note) {
        CoordinateRequestDto dto = new CoordinateRequestDto(tripId, latitude, longitude, note);
        return coordinateRepository.createCoordinate(tripId, dto);
    }

    // Удалить координату
    public LiveData<Resource<Void>> deleteCoordinate(Long id) {
        return coordinateRepository.deleteCoordinate(id);
    }
}
