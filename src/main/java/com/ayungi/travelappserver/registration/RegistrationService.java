package com.ayungi.travelappserver.registration;

import com.ayungi.travelappserver.dto.registration.RegistrationRequestDto;
import com.ayungi.travelappserver.dto.registration.RegistrationResponseDto;
import com.ayungi.travelappserver.exception.RegistrationException;
import com.ayungi.travelappserver.user.User;
import com.ayungi.travelappserver.user.UserRepository;
import com.ayungi.travelappserver.utils.PasswordEncoderUtil;
import com.ayungi.travelappserver.service.EmailVerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final EmailVerificationService emailVerificationService;

    public RegistrationService(UserRepository userRepository,
                               PasswordEncoderUtil passwordEncoderUtil,
                               EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
        this.emailVerificationService = emailVerificationService;
    }

    @Transactional
    public RegistrationResponseDto register(RegistrationRequestDto dto) {
        if (dto == null) {
            throw new RegistrationException("Request data is null");
        }
        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new RegistrationException("Login '" + dto.getLogin() + "' is already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("Email '" + dto.getEmail() + "' is already in use");
        }

        User user = new User();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoderUtil.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAvatarId(1L);
        user.setIsAvatarEmpty(false);
        // Поле enabled остаётся false до подтверждения email
        userRepository.save(user);

        // Отправка письма для подтверждения email
        emailVerificationService.sendVerificationEmail(user);

        return new RegistrationResponseDto(user.getId());
    }
}
