package com.ayungi.travelapp.view.activities.registration;

import static com.ayungi.travelapp.utils.Utils.isAtLeastOneYearOld;
import static com.ayungi.travelapp.utils.Utils.isValidDate;
import static com.ayungi.travelapp.utils.Utils.isValidEmail;
import static com.ayungi.travelapp.utils.Utils.isValidLogin;
import static com.ayungi.travelapp.utils.Utils.isValidPassword;
import static com.ayungi.travelapp.utils.Utils.togglePasswordVisibility;
import static com.ayungi.travelapp.utils.Utils.showGenderSelectionDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.requests.RegistrationRequestDto;
import com.ayungi.travelapp.view.activities.login.LoginActivity;
import com.ayungi.travelapp.viewmodel.RegistrationViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput, loginInput, lastNameInput, firstNameInput, birthdayInput;
    private EditText passwordInput, passwordConfirmInput;
    private ImageButton passwordToggle, passwordConfirmToggle, sexChoiceButton;
    private TextView backToAuthButton, genderLabel;
    private Button registrationButton;
    private ProgressBar progressBar;
    private String selectedGender;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        passwordToggle.setOnClickListener(v -> togglePasswordVisibility(passwordInput, passwordToggle));
        passwordConfirmToggle.setOnClickListener(v -> togglePasswordVisibility(passwordConfirmInput, passwordConfirmToggle));
        sexChoiceButton.setOnClickListener(v -> selectedGender = showGenderSelectionDialog(sexChoiceButton, genderLabel, this));

        birthdayInput.setFocusable(false);
        birthdayInput.setOnClickListener(v -> showDatePickerDialog());

        // Переход на экран авторизации (Login)
        backToAuthButton.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            finish();
        });

        registrationButton.setOnClickListener(v -> handleRegistration());
    }

    private void initViews() {
        emailInput = findViewById(R.id.email_input);
        loginInput = findViewById(R.id.login_input);
        lastNameInput = findViewById(R.id.last_name_input);
        firstNameInput = findViewById(R.id.first_name_input);
        birthdayInput = findViewById(R.id.birthday_input);

        passwordInput = findViewById(R.id.password_input);
        passwordConfirmInput = findViewById(R.id.password_confirm_input);

        passwordToggle = findViewById(R.id.password_toggle);
        passwordConfirmToggle = findViewById(R.id.password_confirm_toggle);
        sexChoiceButton = findViewById(R.id.gender_choice_menu_button);

        backToAuthButton = findViewById(R.id.back_to_auth_page_text);
        genderLabel = findViewById(R.id.gender_choice);
        registrationButton = findViewById(R.id.registration_button);

        progressBar = findViewById(R.id.progressBar);
    }

    // Показываем выбор даты с помощью DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrationActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());
                    birthdayInput.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void handleRegistration() {
        boolean isInputDataValid = true;

        String email = emailInput.getText().toString().trim();
        String login = loginInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String birthdayText = birthdayInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String passwordConfirm = passwordConfirmInput.getText().toString().trim();

        String gender = selectedGender;
        String dateOfBirth = null;

        // Валидация
        if (email.isEmpty()) {
            isInputDataValid = false;
            emailInput.setError("Поле обязательно");
        } else if (!isValidEmail(email)) {
            isInputDataValid = false;
            emailInput.setError("Некорректный адрес");
        }

        if (login.isEmpty()) {
            isInputDataValid = false;
            loginInput.setError("Поле обязательно");
        } else if (!isValidLogin(login)) {
            isInputDataValid = false;
            loginInput.setError("Логин должен быть 5-20 символов");
        }

        if (lastName.isEmpty()) {
            isInputDataValid = false;
            lastNameInput.setError("Поле обязательно");
        }
        if (firstName.isEmpty()) {
            isInputDataValid = false;
            firstNameInput.setError("Поле обязательно");
        }

        if (birthdayText.isEmpty()) {
            isInputDataValid = false;
            birthdayInput.setError("Поле обязательно");
        } else if (!isValidDate(birthdayText, "dd.MM.yyyy")) {
            isInputDataValid = false;
            birthdayInput.setError("Неверный формат dd.MM.yyyy");
        } else if (!isAtLeastOneYearOld(birthdayText, "dd.MM.yyyy")) {
            isInputDataValid = false;
            birthdayInput.setError("Вам должен быть как минимум 1 год");
        } else {
            dateOfBirth = birthdayText;
        }

        if (password.isEmpty()) {
            isInputDataValid = false;
            passwordInput.setError("Поле обязательно");
        } else if (!isValidPassword(password)) {
            isInputDataValid = false;
            passwordInput.setError("Слишком простой пароль");
        }

        if (passwordConfirm.isEmpty()) {
            isInputDataValid = false;
            passwordConfirmInput.setError("Поле обязательно");
        } else if (!password.equals(passwordConfirm)) {
            isInputDataValid = false;
            passwordConfirmInput.setError("Пароли не совпадают");
        }

        if (!isInputDataValid) {
            Log.w("RegistrationActivity", "Валидация не пройдена.");
            return;
        }

        RegistrationRequestDto dto = new RegistrationRequestDto(
                login, email, password, firstName, lastName, gender, dateOfBirth
        );

        progressBar.setVisibility(View.VISIBLE);

        registrationViewModel.register(dto).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    // Вместо немедленного перехода на логин – уведомляем, что письмо для подтверждения отправлено
                    Toast.makeText(RegistrationActivity.this,
                            "Регистрация успешна! Проверьте почту для подтверждения email.",
                            Toast.LENGTH_LONG).show();
                    // Переходим на экран логина
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    String errorMsg = resource.message;
                    Toast.makeText(RegistrationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    if (errorMsg != null && errorMsg.contains("существует")) {
                        emailInput.setError("Пользователь с таким email/логином уже существует");
                    }
                    break;
            }
        });
    }
}
