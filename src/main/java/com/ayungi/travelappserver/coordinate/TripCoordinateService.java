package com.ayungi.travelappserver.coordinate;

import com.ayungi.travelappserver.dto.coordinate.CoordinateDto;
import com.ayungi.travelappserver.trip.Trip;
import com.ayungi.travelappserver.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripCoordinateService {

    private final TripCoordinateRepository coordinateRepository;
    private final TripRepository tripRepository;

    public TripCoordinateService(TripCoordinateRepository coordinateRepository, TripRepository tripRepository) {
        this.coordinateRepository = coordinateRepository;
        this.tripRepository = tripRepository;
    }

    @Transactional
    public CoordinateDto createCoordinate(Long tripId, CoordinateDto dto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));
        TripCoordinate coordinate = new TripCoordinate();
        coordinate.setLatitude(dto.getLatitude());
        coordinate.setLongitude(dto.getLongitude());
        coordinate.setTrip(trip);
        coordinate.setNote(dto.getNote()); // Устанавливаем заметку
        TripCoordinate saved = coordinateRepository.save(coordinate);
        return toDto(saved);
    }

    public List<CoordinateDto> getCoordinatesByTrip(Long tripId) {
        return coordinateRepository.findByTripId(tripId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CoordinateDto updateCoordinate(Long coordinateId, CoordinateDto dto) {
        TripCoordinate coordinate = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("Coordinate not found with id: " + coordinateId));
        coordinate.setLatitude(dto.getLatitude());
        coordinate.setLongitude(dto.getLongitude());
        coordinate.setNote(dto.getNote()); // Обновляем заметку
        TripCoordinate updated = coordinateRepository.save(coordinate);
        return toDto(updated);
    }

    @Transactional
    public void deleteCoordinate(Long coordinateId) {
        TripCoordinate coordinate = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("Coordinate not found with id: " + coordinateId));
        coordinateRepository.delete(coordinate);
    }

    private CoordinateDto toDto(TripCoordinate coordinate) {
        return new CoordinateDto(coordinate.getId(), coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getNote());
    }
}
