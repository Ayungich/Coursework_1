package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.requests.RegistrationRequestDto;
import com.ayungi.travelapp.model.data.responses.RegistrationResponseDto;
import com.ayungi.travelapp.model.repository.RegistrationRepository;
import com.ayungi.travelapp.utils.Resource;

public class RegistrationViewModel extends ViewModel {

    private final RegistrationRepository registrationRepository;

    public RegistrationViewModel() {
        registrationRepository = RegistrationRepository.getInstance();
    }

    public LiveData<Resource<RegistrationResponseDto>> register(RegistrationRequestDto dto) {
        return registrationRepository.register(dto);
    }
}
