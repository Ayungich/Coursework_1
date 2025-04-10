package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.PackingItemResponseDto;
import com.ayungi.travelapp.model.repository.PackingItemRepository;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

public class PackingItemViewModel extends ViewModel {
    private final PackingItemRepository repository;

    public PackingItemViewModel() {
        repository = PackingItemRepository.getInstance();
    }

    public LiveData<Resource<List<PackingItemResponseDto>>> getAllPackingItems(Long tripId) {
        return repository.getAllPackingItems(tripId);
    }

    public LiveData<Resource<PackingItemResponseDto>> createPackingItem(String name, Boolean taken, Long tripId) {
        return repository.createPackingItem(name, taken, tripId);
    }

    public LiveData<Resource<PackingItemResponseDto>> updatePackingItem(Long id, Boolean taken) {
        return repository.updatePackingItem(id, taken);
    }

    public LiveData<Resource<PackingItemResponseDto>> editPackingItem(Long id, com.ayungi.travelapp.model.data.requests.PackingItemEditRequestDto request) {
        return repository.editPackingItem(id, request);
    }

    public LiveData<Resource<Void>> deletePackingItem(Long id) {
        return repository.deletePackingItem(id);
    }
}
