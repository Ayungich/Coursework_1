package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.LoginResponseDto;
import com.ayungi.travelapp.model.repository.LoginRepository;
import com.ayungi.travelapp.utils.Resource;

public class LoginViewModel extends ViewModel {

    private final LoginRepository loginRepository;

    public LoginViewModel() {
        loginRepository = LoginRepository.getInstance();
    }

    public LiveData<Resource<LoginResponseDto>> login(String email, String password) {
        return loginRepository.login(email, password);
    }
}
