package com.ayungi.travelappserver.authorization;

import com.ayungi.travelappserver.dto.authorization.AuthorizationRequestDto;
import com.ayungi.travelappserver.dto.authorization.AuthorizationResponseDto;
import com.ayungi.travelappserver.service.EmailVerificationService;
import com.ayungi.travelappserver.service.PasswordResetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    public AuthorizationController(AuthorizationService authorizationService,
                                   EmailVerificationService emailVerificationService,
                                   PasswordResetService passwordResetService) {
        this.authorizationService = authorizationService;
        this.emailVerificationService = emailVerificationService;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Авторизация: если ОК -> 200 с userId.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponseDto> login(
            @Valid @RequestBody AuthorizationRequestDto request
    ) {
        AuthorizationResponseDto response = authorizationService.authorize(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }

    // Эндпоинт для подтверждения email
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok("Email confirmed successfully.");
    }

    // Эндпоинт для запроса сброса пароля
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    // GET‑endpoint для отображения формы сброса пароля.
    // Эта страница возвращает HTML‑форму, которая будет отправлена методом POST.
    @GetMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> showResetPasswordForm(@RequestParam("token") String token) {
        String htmlForm =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "    <meta charset='utf-8' />" +
                        "    <title>Reset Password</title>" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        "        .container { max-width: 400px; margin: 50px auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                        "        h2 { text-align: center; color: #333; }" +
                        "        label { display: block; margin-top: 10px; font-weight: bold; color: #555; }" +
                        "        input[type='password'] { width: 100%; padding: 10px; margin: 5px 0 15px 0; border: 1px solid #ccc; border-radius: 4px; }" +
                        "        input[type='submit'] { width: 100%; padding: 10px; background-color: #4CAF50; color: #fff; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; }" +
                        "        input[type='submit']:hover { background-color: #45a049; }" +
                        "    </style>" +
                        "    <script>" +
                        "        function validateForm() {" +
                        "            var newPassword = document.getElementById('newPassword').value;" +
                        "            var confirmPassword = document.getElementById('confirmPassword').value;" +
                        "            if (newPassword !== confirmPassword) {" +
                        "                alert('Пароли не совпадают');" +
                        "                return false;" +
                        "            }" +
                        "            return true;" +
                        "        }" +
                        "    </script>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <h2>Сброс пароля</h2>" +
                        "        <form method='post' action='/api/auth/reset-password' onsubmit='return validateForm();'>" +
                        "            <input type='hidden' name='token' value='" + token + "' />" +
                        "            <label for='newPassword'>Новый пароль:</label>" +
                        "            <input type='password' id='newPassword' name='newPassword' required />" +
                        "            <label for='confirmPassword'>Подтверждение пароля:</label>" +
                        "            <input type='password' id='confirmPassword' name='confirmPassword' required />" +
                        "            <input type='submit' value='Сбросить пароль' />" +
                        "        </form>" +
                        "    </div>" +
                        "</body>" +
                        "</html>";
        return ResponseEntity.ok(htmlForm);
    }

    // POST‑endpoint для фактического сброса пароля
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
