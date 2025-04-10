package com.ayungi.travelapp.view.activities.profile;

import static com.ayungi.travelapp.utils.Utils.isValidEmail;
import static com.ayungi.travelapp.utils.Utils.isValidLogin;
import static com.ayungi.travelapp.utils.Utils.isValidPassword;
import static com.ayungi.travelapp.utils.Utils.showGenderSelectionDialog;
import static com.ayungi.travelapp.utils.Utils.togglePasswordVisibility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.login.LoginActivity;
import com.ayungi.travelapp.view.activities.settings.SettingsActivity;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;
import com.ayungi.travelapp.view.activities.main.MainPageActivity;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatarIcon;
    private TextView changePhoto, errorText, genderLabel;
    private Button saveButton;
    private ImageButton passwordToggle, genderChoiceButton, backButton, logoutButton, deleteButton;
    private EditText emailInput, loginInput, passwordInput;
    private String selectedGender;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private SharedPreferences prefs;
    private static final int READ_PERMISSION_REQUEST_CODE = 101;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);
        prefs = getSharedPreferences("TravelApp", MODE_PRIVATE);

        // Запрос разрешения, если его нет
        if (!hasReadPermission()) {
            requestReadPermission();
        }

        // При клике по changePhoto или аватару открываем галерею
        changePhoto.setOnClickListener(v -> openImageChooser());
        avatarIcon.setOnClickListener(v -> openImageChooser());

        passwordToggle.setOnClickListener(v -> togglePasswordVisibility(passwordInput, passwordToggle));
        genderChoiceButton.setOnClickListener(v -> {
            selectedGender = showGenderSelectionDialog(genderChoiceButton, genderLabel, this);
        });
        saveButton.setOnClickListener(v -> updateCredentials());
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MainPageActivity.class));
        });
        // Вызываем метод logout при нажатии на кнопку "Выйти"
        logoutButton.setOnClickListener(v -> logout());

        // При нажатии "Удалить аккаунт" вызываем метод deleteAccount
        deleteButton.setOnClickListener(v -> deleteAccount());

        // Загружаем данные пользователя для заполнения полей
        loadUserData();

        // Загружаем аватар, если он уже сохранён
        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            loadAvatar(avatarId);
        }
    }

    private void initViews() {
        avatarIcon = findViewById(R.id.avatar);
        changePhoto = findViewById(R.id.changePhoto);
        emailInput = findViewById(R.id.emailInput);
        loginInput = findViewById(R.id.loginInput);
        genderLabel = findViewById(R.id.genderLabel);
        passwordInput = findViewById(R.id.passwordInput);
        passwordToggle = findViewById(R.id.passwordToggle);
        genderChoiceButton = findViewById(R.id.genderArrow);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logout_button);
        deleteButton = findViewById(R.id.delete_button);
        backButton = findViewById(R.id.back_button);
        errorText = findViewById(R.id.errorText);
    }

    // Проверка наличия разрешения на чтение изображений
    private boolean hasReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Запрос разрешения на чтение изображений
    private void requestReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    READ_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено
            } else {
                Toast.makeText(this, "Разрешение необходимо для выбора изображения", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Обновление данных пользователя
    @SuppressLint("SetTextI18n")
    private void updateCredentials() {
        String email = emailInput.getText().toString().trim();
        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Сначала проверяем, что поля не пустые
        if (email.isEmpty()) {
            emailInput.setError("Email не может быть пустым");
            return;
        }
        if (login.isEmpty()) {
            loginInput.setError("Логин не может быть пустым");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Пароль не может быть пустым");
            return;
        }

        // Затем проверяем корректность формата
        if (!isValidEmail(email)) {
            emailInput.setError("Введите корректный email");
            return;
        }
        if (!isValidLogin(login)) {
            loginInput.setError("Логин должен быть 5-20 символов");
            return;
        }
        if (!isValidPassword(password)) {
            passwordInput.setError("Слишком простой пароль");
            return;
        }

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1L);
        if (userId == -1L) {
            Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_LONG).show();
            return;
        }
        errorText.setVisibility(View.GONE);

        userViewModel.patchUserCredentials(userId, login, email, password, selectedGender)
                .observe(this, resource -> {
                    if (resource == null) return;
                    switch (resource.status) {
                        case LOADING:
                            errorText.setVisibility(View.GONE);
                            break;
                        case SUCCESS:
                            Toast.makeText(ProfileActivity.this, "Профиль обновлен", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case ERROR:
                            errorText.setText("Ошибка: " + resource.message);
                            errorText.setVisibility(View.VISIBLE);
                            break;
                    }
                });
    }

    // Метод для загрузки данных пользователя и заполнения полей
    private void loadUserData() {
        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1L);
        if (userId == -1L) {
            Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        loginInput.setText(resource.data.getLogin());
                        emailInput.setText(resource.data.getEmail());
                    }
                    break;
                case ERROR:
                    Toast.makeText(ProfileActivity.this,
                            "Ошибка загрузки пользователя: " + resource.message,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void loadAvatar(long avatarId) {
        // Запрашиваем аватар по его ID
        avatarViewModel.getAvatar(avatarId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        avatarIcon.setImageBitmap(resource.data);
                    }
                    break;
                case ERROR:
                    Toast.makeText(this, "Ошибка загрузки аватара: " + resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void logout() {
        prefs.edit().clear().apply();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteAccount() {
        long userId = prefs.getLong("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }
        userViewModel.deleteUser(userId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == Resource.Status.SUCCESS) {
                Toast.makeText(this, "Аккаунт удален", Toast.LENGTH_SHORT).show();
                logout();
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Ошибка при удалении аккаунта: " + resource.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Открытие галереи для выбора изображения
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            avatarIcon.setImageURI(selectedImageUri);
            File file = getFileFromUri(selectedImageUri);
            if (file != null) {
                uploadAvatar(file);
            } else {
                Toast.makeText(this, "Ошибка при получении файла", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Преобразование Uri в File
    private File getFileFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return new File(filePath);
        }
        return null;
    }

    // Загрузка аватара на сервер
    private void uploadAvatar(File file) {
        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_LONG).show();
            return;
        }
        avatarViewModel.uploadAvatar(userId, file).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    break;
                case ERROR:
                    Toast.makeText(this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}
