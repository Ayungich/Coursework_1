package com.ayungi.travelappserver.trip;

import com.ayungi.travelappserver.dto.trip.TripRequestDto;
import com.ayungi.travelappserver.dto.trip.TripResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getAllTrips() {
        List<TripResponseDto> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDto> getTripById(@PathVariable Long id) {
        TripResponseDto dto = tripService.getTripById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TripResponseDto> createTrip(@RequestBody TripRequestDto requestDto) {
        TripResponseDto created = tripService.createTrip(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDto> updateTrip(@PathVariable Long id, @RequestBody TripRequestDto requestDto) {
        TripResponseDto updated = tripService.updateTrip(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
