package com.ayungi.travelappserver.route;

import com.ayungi.travelappserver.dto.route.RouteMarkerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/markers")
public class RouteMarkerController {

    private final RouteMarkerService markerService;

    public RouteMarkerController(RouteMarkerService markerService) {
        this.markerService = markerService;
    }

    // Получить список меток для конкретного путешествия
    @GetMapping
    public ResponseEntity<List<RouteMarkerDto>> getMarkers(@PathVariable Long tripId) {
        List<RouteMarkerDto> markers = markerService.getMarkersByTripId(tripId);
        return ResponseEntity.ok(markers);
    }

    // Создать новую метку для конкретного путешествия
    @PostMapping
    public ResponseEntity<RouteMarkerDto> createMarker(@PathVariable Long tripId, @RequestBody RouteMarkerDto dto) {
        RouteMarkerDto created = markerService.createMarker(tripId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
