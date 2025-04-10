package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.requests.UserCredentialsDto;
import com.ayungi.travelapp.model.data.responses.UserResponseDto;
import com.ayungi.travelapp.model.repository.UserRepository;
import com.ayungi.travelapp.utils.Resource;

public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = UserRepository.getInstance();
    }

    // Получаем пользователя по ID
    public LiveData<Resource<UserResponseDto>> getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

    public LiveData<Resource<UserResponseDto>> patchUserCredentials(long userId,
                                                                    String login,
                                                                    String email,
                                                                    String password,
                                                                    String gender) {
        UserCredentialsDto dto = new UserCredentialsDto(login, email, password, gender);
        return userRepository.patchUserCredentials(userId, dto);
    }

    public LiveData<Resource<Void>> deleteUser(long userId) {
        return userRepository.deleteUser(userId);
    }
}
