package com.ayungi.travelappserver.tripitem;

import com.ayungi.travelappserver.dto.packing.PackingItemEditRequestDto;
import com.ayungi.travelappserver.dto.packing.PackingItemRequestDto;
import com.ayungi.travelappserver.dto.packing.PackingItemResponseDto;
import com.ayungi.travelappserver.dto.packing.PackingItemUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/packing-items")
public class PackingItemController {

    private final PackingItemService packingItemService;

    public PackingItemController(PackingItemService packingItemService) {
        this.packingItemService = packingItemService;
    }

    // Эндпоинт для получения списка вещей для конкретного путешествия
    @GetMapping("/by-trip")
    public ResponseEntity<List<PackingItemResponseDto>> getPackingItemsByTrip(@RequestParam("tripId") Long tripId) {
        List<PackingItemResponseDto> dtos = packingItemService.getPackingItemsByTripId(tripId)
                .stream()
                .map(packingItemService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Эндпоинт для создания новой вещи в конкретном путешествии
    @PostMapping("/by-trip")
    public ResponseEntity<PackingItemResponseDto> createPackingItem(@RequestBody PackingItemRequestDto dto) {
        PackingItem created = packingItemService.createPackingItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(packingItemService.toDto(created));
    }

    // Обновить (отредактировать) вещь
    @PutMapping("/{id}/edit")
    public ResponseEntity<PackingItemResponseDto> editPackingItem(@PathVariable Long id,
                                                                  @RequestBody PackingItemEditRequestDto dto) {
        PackingItem updated = packingItemService.updatePackingItem(id, dto);
        return ResponseEntity.ok(packingItemService.toDto(updated));
    }

    // Эндпоинт для обновления состояния вещи (флаг taken)
    @PutMapping("/{id}")
    public ResponseEntity<PackingItemResponseDto> updatePackingItemFlag(@PathVariable Long id,
                                                                    @RequestBody PackingItemUpdateRequestDto request) {
        PackingItem updated = packingItemService.updatePackingItemFlag(id, request.getTaken());
        return ResponseEntity.ok(packingItemService.toDto(updated));
    }

    // Удалить вещь
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackingItem(@PathVariable Long id) {
        packingItemService.deletePackingItem(id);
        return ResponseEntity.noContent().build();
    }
}
