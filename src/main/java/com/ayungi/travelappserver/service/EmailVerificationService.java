package com.ayungi.travelappserver.service;

import com.ayungi.travelappserver.token.EmailVerificationToken;
import com.ayungi.travelappserver.token.EmailVerificationTokenRepository;
import com.ayungi.travelappserver.user.User;
import com.ayungi.travelappserver.user.UserRepository;
import com.ayungi.travelappserver.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.verification.token.expiration.minutes}")
    private int expirationMinutes;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository,
                                    UserRepository userRepository,
                                    EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user, LocalDateTime.now().plusMinutes(expirationMinutes));
        tokenRepository.save(verificationToken);

        String verificationLink = "http://194.87.110.27:8080/api/auth/confirm?token=" + token;
        String subject = "Email Confirmation";
        String body = "Please confirm your email by clicking the following link: " + verificationLink;

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    public void verifyEmail(String token) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);

        // Если токен не найден, возможно он уже был использован и удалён — считаем, что подтверждение уже было выполнено.
        if (optionalToken.isEmpty()) {
            return;
        }

        EmailVerificationToken verificationToken = optionalToken.get();

        // Если токен просрочен — выбрасываем исключение с соответствующим сообщением.
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token expired");
        }

        // Подтверждаем пользователя
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        // Удаляем токен после успешного подтверждения
        tokenRepository.delete(verificationToken);
    }
}
