package com.ayungi.travelapp.view.activities.login;

import static com.ayungi.travelapp.utils.Utils.isValidEmail;
import static com.ayungi.travelapp.utils.Utils.isValidPassword;
import static com.ayungi.travelapp.utils.Utils.togglePasswordVisibility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.LoginResponseDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.main.MainPageActivity;
import com.ayungi.travelapp.view.activities.registration.RegistrationActivity;
import com.ayungi.travelapp.viewmodel.AuthViewModel;
import com.ayungi.travelapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private TextView errorText, backToLoginTextView, forgotPasswordText;
    private View progressBar;
    private Button loginButton;
    private ImageButton passwordToggle;
    private LoginViewModel loginViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        loginButton.setOnClickListener(v -> doLogin());
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility(passwordInput, passwordToggle));

        // Переход на экран регистрации
        backToLoginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Обработка клика "Забыли пароль?"
        forgotPasswordText.setOnClickListener(v -> showPasswordResetDialog());
    }

    private void initViews() {
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        errorText = findViewById(R.id.errorText);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.login_button);
        passwordToggle = findViewById(R.id.password_toggle);
        backToLoginTextView = findViewById(R.id.back_to_auth_page_text);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
    }

    private void doLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            emailInput.setError("Введите корректный email");
            return;
        }
        if (password.isEmpty() || !isValidPassword(password)) {
            passwordInput.setError("Введите пароль");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        loginViewModel.login(email, password).observe(this, new Observer<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Resource<LoginResponseDto> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        errorText.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.data != null) {
                            long userId = resource.data.getUserId();

                            getSharedPreferences("TravelApp", MODE_PRIVATE)
                                    .edit()
                                    .putLong("USER_ID", userId)
                                    .apply();

                            Log.i("LoginActivity", "Успешный вход, userId=" + userId);
                            Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(resource.message);
                        break;
                }
            }
        });
    }

    // Показываем диалог для ввода email для сброса пароля
    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сброс пароля");
        builder.setMessage("Введите адрес электронной почты для получения ссылки сброса пароля:");

        final EditText inputEmail = new EditText(this);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(inputEmail);

        builder.setPositiveButton("Отправить", (dialog, which) -> {

            String email = inputEmail.getText().toString().trim();
            if (email.isEmpty() || !isValidEmail(email)) {
                Toast.makeText(LoginActivity.this, "Введите корректный email", Toast.LENGTH_LONG).show();
                return;
            }
            // Вызываем метод запроса ссылки для сброса пароля через AuthViewModel
            authViewModel.requestPasswordReset(email).observe(LoginActivity.this, resource -> {
                progressBar.setVisibility(View.GONE);
                if (resource == null) return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Toast.makeText(LoginActivity.this,
                                "Ссылка для сброса пароля отправлена на " + email,
                                Toast.LENGTH_LONG).show();
                        break;
                    case ERROR:
                        Toast.makeText(LoginActivity.this,
                                "Ошибка: " + resource.message,
                                Toast.LENGTH_LONG).show();
                        break;
                }
            });
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
