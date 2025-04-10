package com.ayungi.travelappserver.user;

import com.ayungi.travelappserver.dto.user.UserCredentialsDto;
import com.ayungi.travelappserver.dto.user.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей (Entity).
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Получить конкретного пользователя (возвращаем DTO без пароля).
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userService.toUserResponseDto(user));
    }

    // Создать пользователя
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        UserResponseDto dto = userService.toUserResponseDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        User saved = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(userService.toUserResponseDto(saved));
    }

    // Обновить пользователя (PATCH для частичного обновления)
    @PatchMapping("/{id}/credentials")
    public ResponseEntity<UserResponseDto> updateCredentials(
            @PathVariable Long id,
            @RequestBody UserCredentialsDto dto
    ) {
        User saved = userService.updateCredentials(id, dto);
        return ResponseEntity.ok(userService.toUserResponseDto(saved));
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
