package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ayungi.travelapp.model.repository.AuthRepository;
import com.ayungi.travelapp.utils.Resource;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = AuthRepository.getInstance();
    }

    public LiveData<Resource<Void>> confirmEmail(String token) {
        return authRepository.confirmEmail(token);
    }

    public LiveData<Resource<Void>> requestPasswordReset(String email) {
        return authRepository.requestPasswordReset(email);
    }

    public LiveData<Resource<Void>> resetPassword(String token, String newPassword) {
        return authRepository.resetPassword(token, newPassword);
    }
}
