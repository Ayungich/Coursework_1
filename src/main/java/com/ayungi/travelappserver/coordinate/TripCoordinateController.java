package com.ayungi.travelappserver.coordinate;

import com.ayungi.travelappserver.dto.coordinate.CoordinateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
public class TripCoordinateController {

    private final TripCoordinateService coordinateService;

    public TripCoordinateController(TripCoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    // Получить координаты для заданного путешествия
    @GetMapping("/by-trip")
    public ResponseEntity<List<CoordinateDto>> getCoordinatesByTrip(@RequestParam("tripId") Long tripId) {
        List<CoordinateDto> dtos = coordinateService.getCoordinatesByTrip(tripId);
        return ResponseEntity.ok(dtos);
    }

    // Создать новую координату для путешествия
    @PostMapping("/by-trip")
    public ResponseEntity<CoordinateDto> createCoordinate(@RequestParam("tripId") Long tripId,
                                                          @RequestBody CoordinateDto dto) {
        CoordinateDto created = coordinateService.createCoordinate(tripId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Обновить координату по id
    @PutMapping("/{id}")
    public ResponseEntity<CoordinateDto> updateCoordinate(@PathVariable Long id,
                                                          @RequestBody CoordinateDto dto) {
        CoordinateDto updated = coordinateService.updateCoordinate(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Удалить координату по id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinate(@PathVariable Long id) {
        coordinateService.deleteCoordinate(id);
        return ResponseEntity.noContent().build();
    }
}
