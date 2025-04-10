package com.ayungi.travelappserver.tripitem;

import com.ayungi.travelappserver.dto.packing.PackingItemEditRequestDto;
import com.ayungi.travelappserver.dto.packing.PackingItemRequestDto;
import com.ayungi.travelappserver.dto.packing.PackingItemResponseDto;
import com.ayungi.travelappserver.trip.Trip;
import com.ayungi.travelappserver.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PackingItemService {

    private final PackingItemRepository packingItemRepository;
    private final TripRepository tripRepository; // Для поиска Trip

    public PackingItemService(PackingItemRepository packingItemRepository, TripRepository tripRepository) {
        this.packingItemRepository = packingItemRepository;
        this.tripRepository = tripRepository;
    }

    public List<PackingItem> getPackingItemsByTripId(Long tripId) {
        return packingItemRepository.findByTripId(tripId);
    }

    @Transactional
    public PackingItem createPackingItem(PackingItemRequestDto dto) {
        if (dto.getTripId() == null) {
            throw new RuntimeException("Trip ID is required");
        }
        PackingItem item = new PackingItem();
        item.setName(dto.getName());
        item.setTaken(dto.getTaken());
        // Находим Trip по tripId и устанавливаем связь
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + dto.getTripId()));
        item.setTrip(trip);
        return packingItemRepository.save(item);
    }

    @Transactional
    public PackingItem updatePackingItem(Long id, PackingItemEditRequestDto dto) {
        PackingItem item = packingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Packing item not found"));
        item.setName(dto.getName());
        item.setTaken(dto.getTaken());
        return packingItemRepository.save(item);
    }

    @Transactional
    public PackingItem updatePackingItemFlag(Long id, Boolean taken) {
        PackingItem item = packingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Packing item not found"));
        item.setTaken(taken);
        return packingItemRepository.save(item);
    }

    public void deletePackingItem(Long id) {
        packingItemRepository.deleteById(id);
    }

    public PackingItemResponseDto toDto(PackingItem item) {
        return new PackingItemResponseDto(item.getId(), item.getName(), item.getTaken());
    }
}
