package com.ayungi.travelappserver.user;

import com.ayungi.travelappserver.dto.user.UserCredentialsDto;
import com.ayungi.travelappserver.dto.user.UserResponseDto;
import com.ayungi.travelappserver.exception.UserNotFoundException;
import com.ayungi.travelappserver.exception.UserUpdateAvatarException;
import com.ayungi.travelappserver.utils.PasswordEncoderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public UserService(UserRepository userRepository, PasswordEncoderUtil passwordEncoderUtil) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login '" + user.getLogin() + "' is already in use");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' is already in use");
        }
        // Хэширование пароля
        user.setPassword(passwordEncoderUtil.encode(user.getPassword()));

        user.setAvatarId(1L);
        user.setIsAvatarEmpty(false);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoderUtil.encode(updatedUser.getPassword()));
        }
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

        if (updatedUser.getAvatarId() != null) {
            existingUser.setAvatarId(updatedUser.getAvatarId());
        }

        if (updatedUser.getIsAvatarEmpty() != null) {
            existingUser.setIsAvatarEmpty(updatedUser.getIsAvatarEmpty());
        }
        return userRepository.save(existingUser);
    }

    @Transactional
    public User updateCredentials(Long id, UserCredentialsDto dto) {
        User existingUser = getUserById(id);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoderUtil.encode(dto.getPassword()));
        }
        if (dto.getLogin() != null && !dto.getLogin().isEmpty()) {
            existingUser.setLogin(dto.getLogin());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            existingUser.setGender(dto.getGender());
        }
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUserAvatar(Long userId, Long avatarId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserUpdateAvatarException("User with ID " + userId + " not found"));
        user.setAvatarId(avatarId);
        user.setIsAvatarEmpty(false);
        userRepository.save(user);
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getAvatarId(),
                user.getIsAvatarEmpty(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
