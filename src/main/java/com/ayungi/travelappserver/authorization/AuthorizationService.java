package com.ayungi.travelappserver.authorization;

import com.ayungi.travelappserver.dto.authorization.AuthorizationResponseDto;
import com.ayungi.travelappserver.exception.AuthorizationException;
import com.ayungi.travelappserver.exception.UserNotFoundException;
import com.ayungi.travelappserver.user.User;
import com.ayungi.travelappserver.user.UserRepository;
import com.ayungi.travelappserver.utils.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public AuthorizationService(UserRepository userRepository, PasswordEncoderUtil passwordEncoderUtil) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    /**
     * Проверяем email и пароль.
     * Если пользователь не найден -> UserNotFoundException (404)
     * Если пароль неверен -> AuthorizationException (401)
     * Если всё ок -> возвращаем DTO с userId.
     */
    public AuthorizationResponseDto authorize(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));

        boolean matches = passwordEncoderUtil.matches(rawPassword, user.getPassword());
        if (!matches) {
            throw new AuthorizationException("Invalid password for user with email '" + email + "'");
        }

        return new AuthorizationResponseDto(user.getId());
    }
}
