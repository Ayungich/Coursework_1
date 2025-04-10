package com.ayungi.travelappserver.avatar;

import com.ayungi.travelappserver.exception.FileStoreException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Avatar storeFile(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Avatar avatar = new Avatar();
            avatar.setFileName(fileName);
            avatar.setFileType(file.getContentType());
            avatar.setData(file.getBytes());
            return avatarRepository.save(avatar);
        } catch (Exception e) {
            throw new FileStoreException("Could not store file: " + e.getMessage());
        }
    }

    public Optional<Avatar> getAvatar(Long id) {
        return avatarRepository.findById(id);
    }

    public void deleteAvatar(Long id) {
        if (avatarRepository.existsById(id)) {
            avatarRepository.deleteById(id);
        }
    }
}
