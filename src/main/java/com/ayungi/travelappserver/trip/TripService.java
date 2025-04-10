package com.ayungi.travelappserver.trip;

import com.ayungi.travelappserver.coordinate.TripCoordinate;
import com.ayungi.travelappserver.dto.coordinate.CoordinateDto;
import com.ayungi.travelappserver.dto.trip.TripRequestDto;
import com.ayungi.travelappserver.dto.trip.TripResponseDto;
import com.ayungi.travelappserver.dto.budget.BudgetCategoryDto;
import com.ayungi.travelappserver.dto.budget.BudgetItemDto;
import com.ayungi.travelappserver.dto.packing.PackingItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Transactional
    public TripResponseDto createTrip(TripRequestDto requestDto) {
        Trip trip = new Trip();
        trip.setName(requestDto.getName());
        trip.setStartDate(requestDto.getStartDate());
        trip.setEndDate(requestDto.getEndDate());
        trip.setType(requestDto.getType());
        // Маппинг списка координат, если он задан
        if (requestDto.getCoordinates() != null) {
            List<TripCoordinate> coords = requestDto.getCoordinates().stream()
                    .map(c -> {
                        TripCoordinate coord = new TripCoordinate();
                        coord.setLatitude(c.getLatitude());
                        coord.setLongitude(c.getLongitude());
                        coord.setTrip(trip);
                        return coord;
                    }).collect(Collectors.toList());
            trip.setCoordinates(coords);
        }
        Trip saved = tripRepository.save(trip);
        return toDto(saved);
    }

    public TripResponseDto getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        return toDto(trip);
    }

    public List<TripResponseDto> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripResponseDto updateTrip(Long id, TripRequestDto requestDto) {
        Trip updatedTrip = tripRepository.findById(id).map(trip -> {
            trip.setName(requestDto.getName());
            trip.setStartDate(requestDto.getStartDate());
            trip.setEndDate(requestDto.getEndDate());
            trip.setType(requestDto.getType());
            if (requestDto.getCoordinates() != null) {
                List<TripCoordinate> coords = requestDto.getCoordinates().stream()
                        .map(c -> {
                            TripCoordinate coordinate = new TripCoordinate();
                            coordinate.setLatitude(c.getLatitude());
                            coordinate.setLongitude(c.getLongitude());
                            coordinate.setTrip(trip);
                            return coordinate;
                        }).collect(Collectors.toList());
                trip.setCoordinates(coords);
            }
            return tripRepository.save(trip);
        }).orElseThrow(() -> new RuntimeException("Trip not found"));
        return toDto(updatedTrip);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    public TripResponseDto toDto(Trip trip) {
        TripResponseDto dto = new TripResponseDto();
        dto.setId(trip.getId());
        dto.setName(trip.getName());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setType(trip.getType());

        // Маппинг координат
        if (trip.getCoordinates() != null) {
            List<CoordinateDto> coordDtos = trip.getCoordinates().stream()
                    .map(c -> new CoordinateDto(c.getId(), c.getLatitude(), c.getLongitude()))
                    .collect(Collectors.toList());
            dto.setCoordinates(coordDtos);
        }

        // Маппинг бюджетных категорий
        if (trip.getBudgetCategories() != null) {
            List<BudgetCategoryDto> budgetCategoryDtos = trip.getBudgetCategories().stream().map(category -> {
                BudgetCategoryDto catDto = new BudgetCategoryDto();
                catDto.setId(category.getId());
                catDto.setName(category.getName());
                if (category.getItems() != null) {
                    List<BudgetItemDto> itemDtos = category.getItems().stream().map(item -> {
                        BudgetItemDto biDto = new BudgetItemDto();
                        biDto.setId(item.getId());
                        biDto.setName(item.getName());
                        biDto.setAmount(item.getAmount());
                        return biDto;
                    }).collect(Collectors.toList());
                    catDto.setItems(itemDtos);
                }
                return catDto;
            }).collect(Collectors.toList());
            dto.setBudgetCategories(budgetCategoryDtos);
        }

        // Маппинг вещей для упаковки
        if (trip.getPackingItems() != null) {
            List<PackingItemDto> packingDtos = trip.getPackingItems().stream().map(item -> {
                PackingItemDto pDto = new PackingItemDto();
                pDto.setId(item.getId());
                pDto.setName(item.getName());
                pDto.setTaken(item.getTaken());
                return pDto;
            }).collect(Collectors.toList());
            dto.setPackingItems(packingDtos);
        }

        return dto;
    }
}
