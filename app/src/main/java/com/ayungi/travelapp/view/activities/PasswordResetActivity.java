package com.ayungi.travelapp.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.ayungi.travelapp.R;
import com.ayungi.travelapp.view.activities.login.LoginActivity;
import com.ayungi.travelapp.viewmodel.AuthViewModel;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText newPasswordInput;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;
    private String token; // Токен сброса пароля, полученный из deep link

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        newPasswordInput = findViewById(R.id.new_password_input);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        progressBar = findViewById(R.id.progressBar);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Извлечение токена из deep link
        Uri data = getIntent().getData();
        if (data != null) {
            token = data.getQueryParameter("token");
        }
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Отсутствует токен сброса пароля", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            if (newPassword.isEmpty()) {
                newPasswordInput.setError("Введите новый пароль");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            // Вызываем метод сброса пароля, который должен сделать POST-запрос к вашему эндпоинту
            authViewModel.resetPassword(token, newPassword).observe(PasswordResetActivity.this, resource -> {
                progressBar.setVisibility(View.GONE);
                if (resource == null) return;
                switch (resource.status) {
                    case SUCCESS:
                        Toast.makeText(PasswordResetActivity.this, "Пароль успешно изменён", Toast.LENGTH_LONG).show();
                        // Переход на страницу логина после успешного сброса
                        startActivity(new Intent(PasswordResetActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case ERROR:
                        Toast.makeText(PasswordResetActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            });
        });
    }
}
