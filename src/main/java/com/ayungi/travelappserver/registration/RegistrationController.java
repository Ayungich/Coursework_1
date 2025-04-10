package com.ayungi.travelappserver.registration;

import com.ayungi.travelappserver.dto.registration.RegistrationRequestDto;
import com.ayungi.travelappserver.dto.registration.RegistrationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Регистрация:
     * - Если пользователь уже существует -> 409 Conflict
     * - Иначе создаём -> 201 Created
     */
    @PostMapping
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto dto
    ) {
        RegistrationResponseDto responseDto = registrationService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
