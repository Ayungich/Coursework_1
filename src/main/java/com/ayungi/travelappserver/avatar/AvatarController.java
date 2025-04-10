package com.ayungi.travelappserver.avatar;

import com.ayungi.travelappserver.dto.avatar.AvatarUploadResponseDto;
import com.ayungi.travelappserver.exception.UserNotFoundException;
import com.ayungi.travelappserver.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;

    public AvatarController(AvatarService avatarService, UserService userService) {
        this.avatarService = avatarService;
        this.userService = userService;
    }

    /**
     * Загрузка аватара: POST /api/avatar/{userId}/upload
     * Возвращает 201, устанавливает avatarId пользователю.
     */
    @PostMapping("/{userId}/upload")
    public ResponseEntity<AvatarUploadResponseDto> uploadAvatar(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        // Проверяем, что пользователь есть
        userService.getUserById(userId);

        Avatar avatar = avatarService.storeFile(file);
        userService.updateUserAvatar(userId, avatar.getId());

        AvatarUploadResponseDto responseDto = new AvatarUploadResponseDto(
                true,
                "Avatar uploaded successfully",
                avatar.getId()
        );
        return ResponseEntity.status(201).body(responseDto);
    }

    /**
     * Получить аватар: GET /api/avatar/{avatarId}
     * Возвращает 200 + байты. Если аватар не найден -> 404
     */
    @GetMapping("/{avatarId}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long avatarId) {
        Optional<Avatar> avatarOpt = avatarService.getAvatar(avatarId);
        if (avatarOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Avatar avatar = avatarOpt.get();
        String fileType = avatar.getFileType();
        // Если тип содержит wildcard, задаем конкретный MIME тип (например, image/jpeg)
        if (fileType.contains("*")) {
            fileType = "image/jpeg";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileType))
                .body(avatar.getData());
    }

    /**
     * Удалить аватар: DELETE /api/avatar/{avatarId}
     * Возвращает 204.
     */
    @DeleteMapping("/{avatarId}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long avatarId) {
        avatarService.deleteAvatar(avatarId);
        return ResponseEntity.noContent().build();
    }
}
