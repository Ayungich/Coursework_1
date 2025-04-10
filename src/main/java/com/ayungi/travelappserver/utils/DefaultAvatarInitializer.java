package com.ayungi.travelappserver.utils;

import com.ayungi.travelappserver.avatar.Avatar;
import com.ayungi.travelappserver.avatar.AvatarRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class DefaultAvatarInitializer {

    private final AvatarRepository avatarRepository;

    private static final String DEFAULT_AVATAR_FILE_NAME = "default_avatar.jpg";

    public DefaultAvatarInitializer(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        Optional<Avatar> existing = avatarRepository.findByFileName(DEFAULT_AVATAR_FILE_NAME);
        if (existing.isEmpty()) {
            try {
                // Загружаем файл из ресурсов (папка src/main/resources/images/)
                ClassPathResource imgFile = new ClassPathResource("images/" + DEFAULT_AVATAR_FILE_NAME);
                byte[] data = StreamUtils.copyToByteArray(imgFile.getInputStream());

                Avatar defaultAvatar = new Avatar();
                defaultAvatar.setFileName(DEFAULT_AVATAR_FILE_NAME);
                defaultAvatar.setFileType("image/jpeg");
                defaultAvatar.setData(data);
                Avatar saved = avatarRepository.save(defaultAvatar);
                System.out.println("Default avatar saved with id: " + saved.getId());
            } catch (IOException e) {
                throw new RuntimeException("Ошибка загрузки стандартного аватара", e);
            }
        }
    }
}
