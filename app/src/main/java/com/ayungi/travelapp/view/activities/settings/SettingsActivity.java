package com.ayungi.travelapp.view.activities.settings;

import static com.ayungi.travelapp.utils.Utils.initDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.ayungi.travelapp.R;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.login.LoginActivity;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button logoutButton, deleteAccountButton;
    private CircleImageView avatarImage;
    private TextView accountName, currentDate;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initDate(currentDate);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        // Читаем userId из SharedPreferences
        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("USER_ID", -1);
        if (userId != -1) {
            // Загружаем данные профиля
            loadUserData(userId);
        }

        // Читаем avatarId из SharedPreferences
        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            // Загружаем аватар
            loadAvatar(avatarId);
        }

        prefs = getSharedPreferences("TravelApp", MODE_PRIVATE);

        setClickListeners();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountName = findViewById(R.id.settings_account_name);
        currentDate = findViewById(R.id.settings_current_date);
        backButton = findViewById(R.id.back_button);
        logoutButton = findViewById(R.id.logoutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> finish());

        // Вызываем метод logout при нажатии на кнопку "Выйти"
        logoutButton.setOnClickListener(v -> logout());

        // При нажатии "Удалить аккаунт" вызываем метод deleteAccount
        deleteAccountButton.setOnClickListener(v -> deleteAccount());
    }

    // Метод для выхода: очищает SharedPreferences и переходит на LoginActivity
    private void logout() {
        prefs.edit().clear().apply();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
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

    private void loadUserData(long userId) {
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    // Показать прогресс
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        // Устанавливаем имя аккаунта
                        String login = resource.data.getLogin();
                        accountName.setText(login);

                        long avatarId = resource.data.getAvatarId();
                        getSharedPreferences("TravelApp", MODE_PRIVATE)
                                .edit()
                                .putLong("AVATAR_ID", avatarId)
                                .apply();
                    }
                    break;
                case ERROR:
                    Toast.makeText(this,
                            "Ошибка загрузки пользователя: " + resource.message,
                            Toast.LENGTH_LONG).show();
                    Log.e("LoadUserData", resource.message.toString());
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
                        avatarImage.setImageBitmap(resource.data);
                    }
                    break;
                case ERROR:
                    Toast.makeText(this, "Ошибка загрузки аватара: " + resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
