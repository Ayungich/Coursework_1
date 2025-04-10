package com.ayungi.travelappserver.service;

import com.ayungi.travelappserver.token.PasswordResetToken;
import com.ayungi.travelappserver.token.PasswordResetTokenRepository;
import com.ayungi.travelappserver.user.User;
import com.ayungi.travelappserver.user.UserRepository;
import com.ayungi.travelappserver.email.EmailService;
import com.ayungi.travelappserver.utils.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoderUtil passwordEncoderUtil;

    @Value("${app.password.reset.token.expiration.minutes}")
    private int expirationMinutes;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                EmailService emailService,
                                PasswordEncoderUtil passwordEncoderUtil) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    public void createPasswordResetToken(String email) {
        // Ищем пользователя по email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Генерируем новый токен и срок его действия
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(expirationMinutes);

        // Проверяем, существует ли уже токен для данного пользователя
        Optional<PasswordResetToken> existingTokenOpt = tokenRepository.findByUser(user);
        if (existingTokenOpt.isPresent()) {
            // Обновляем существующий токен
            PasswordResetToken existingToken = existingTokenOpt.get();
            existingToken.setToken(newToken);
            existingToken.setExpiryDate(expiry);
            tokenRepository.save(existingToken);
        } else {
            // Создаем новый токен, если его еще нет
            PasswordResetToken resetToken = new PasswordResetToken(newToken, user, expiry);
            tokenRepository.save(resetToken);
        }

        // Формируем ссылку для сброса пароля
        String resetLink = "http://194.87.110.27:8080/api/auth/reset-password?token=" + newToken;
        String subject = "Password Reset Request";
        String body = "Click the following link to reset your password: " + resetLink;

        // Отправляем письмо
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    public void resetPassword(String token, String newPassword) {
        // Ищем запись по токену
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token expired");
        }

        // Изменяем пароль пользователя
        User user = resetToken.getUser();
        user.setPassword(passwordEncoderUtil.encode(newPassword));
        userRepository.save(user);

        // Удаляем токен, чтобы по данному пользователю не осталось дубликатов
        tokenRepository.delete(resetToken);
    }
}
