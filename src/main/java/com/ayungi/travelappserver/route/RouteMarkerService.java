package com.ayungi.travelappserver.route;

import com.ayungi.travelappserver.dto.route.RouteMarkerDto;
import com.ayungi.travelappserver.trip.Trip;
import com.ayungi.travelappserver.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteMarkerService {

    private final RouteMarkerRepository markerRepository;
    private final TripRepository tripRepository;

    public RouteMarkerService(RouteMarkerRepository markerRepository, TripRepository tripRepository) {
        this.markerRepository = markerRepository;
        this.tripRepository = tripRepository;
    }

    public List<RouteMarkerDto> getMarkersByTripId(Long tripId) {
        List<RouteMarker> markers = markerRepository.findByTripId(tripId);
        return markers.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public RouteMarkerDto createMarker(Long tripId, RouteMarkerDto dto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        RouteMarker marker = new RouteMarker();
        marker.setTrip(trip);
        marker.setLatitude(dto.getLatitude());
        marker.setLongitude(dto.getLongitude());
        marker.setTitle(dto.getTitle());
        marker.setDescription(dto.getDescription());
        RouteMarker saved = markerRepository.save(marker);
        return toDto(saved);
    }

    @Transactional
    public RouteMarkerDto updateMarker(Long markerId, RouteMarkerDto dto) {
        RouteMarker marker = markerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));
        marker.setLatitude(dto.getLatitude());
        marker.setLongitude(dto.getLongitude());
        marker.setTitle(dto.getTitle());
        marker.setDescription(dto.getDescription());
        RouteMarker updated = markerRepository.save(marker);
        return toDto(updated);
    }

    @Transactional
    public void deleteMarker(Long markerId) {
        markerRepository.deleteById(markerId);
    }

    public RouteMarkerDto toDto(RouteMarker marker) {
        return new RouteMarkerDto(
                marker.getId(),
                marker.getTrip().getId(),
                marker.getLatitude(),
                marker.getLongitude(),
                marker.getTitle(),
                marker.getDescription()
        );
    }
}
