package com.ayungi.travelappserver.route;

import com.ayungi.travelappserver.dto.route.RouteMarkerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/markers")
public class RouteMarkerManagementController {

    private final RouteMarkerService markerService;

    public RouteMarkerManagementController(RouteMarkerService markerService) {
        this.markerService = markerService;
    }

    // Обновить метку
    @PutMapping("/{id}")
    public ResponseEntity<RouteMarkerDto> updateMarker(@PathVariable Long id, @RequestBody RouteMarkerDto dto) {
        RouteMarkerDto updated = markerService.updateMarker(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Удалить метку
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarker(@PathVariable Long id) {
        markerService.deleteMarker(id);
        return ResponseEntity.noContent().build();
    }
}
