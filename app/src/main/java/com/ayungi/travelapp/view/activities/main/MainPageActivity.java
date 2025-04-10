package com.ayungi.travelapp.view.activities.main;

import static com.ayungi.travelapp.utils.Utils.initDate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;

import com.ayungi.travelapp.view.activities.calendar.CalendarActivity;
import com.ayungi.travelapp.view.activities.profile.ProfileActivity;
import com.ayungi.travelapp.view.activities.trip.TripCreateActivity;
import com.ayungi.travelapp.view.activities.diary.DiariesActivity;
import com.ayungi.travelapp.view.activities.trip.TripsActivity;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;
import com.ayungi.travelapp.viewmodel.WeatherViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainPageActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private WeatherViewModel weatherViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView cityTextView, tempTextView, currentDateTextView, accountNameTextView, weatherDescriptionTextView, pressureTextView, humidityTextView;;
    private ImageView avatarImageView, calendarImageView;
    private ConstraintLayout wave1, wave2, wave3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initViews();
        initDate(currentDateTextView);
        requestLocationPermissionIfNeeded();

        avatarImageView.setOnClickListener(v -> {
                    Toast.makeText(this, "Профиль", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainPageActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
        );

        calendarImageView.setOnClickListener(v -> {
                    Toast.makeText(this, "Календарь", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainPageActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
        );

        wave1.setOnClickListener(v -> {
                    Intent intent = new Intent(MainPageActivity.this, TripsActivity.class);
                    startActivity(intent);
                }
        );

        wave2.setOnClickListener(v -> {
                    Intent intent = new Intent(MainPageActivity.this, DiariesActivity.class);
                    startActivity(intent);
        }
        );

        wave3.setOnClickListener(v -> {
                    Intent intent = new Intent(MainPageActivity.this, TripCreateActivity.class);
                    startActivity(intent);
                }
        );

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Читаем userId из SharedPreferences
        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("USER_ID", -1);
        if (userId != -1) {
            // Загружаем данные профиля
            loadUserData(userId);
        }

        loadUserData(userId);

        // Читаем avatarId из SharedPreferences
        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            // Загружаем аватар
            loadAvatar(avatarId);
        }

        loadAvatar(avatarId);
    }

    private void initViews() {
        cityTextView = findViewById(R.id.main_page_current_city);
        tempTextView = findViewById(R.id.main_page_current_temperature);
        weatherDescriptionTextView = findViewById(R.id.main_page_weather_description);
        pressureTextView = findViewById(R.id.main_page_weather_pressure);
        humidityTextView = findViewById(R.id.main_page_weather_humidity);

        currentDateTextView = findViewById(R.id.main_page_settings_current_date);
        accountNameTextView = findViewById(R.id.main_page_account_name);

        avatarImageView = findViewById(R.id.imageView_avatar);
        calendarImageView = findViewById(R.id.calendar);

        wave1 = findViewById(R.id.wave1);
        wave2 = findViewById(R.id.wave2);
        wave3 = findViewById(R.id.wave3);
    }

    private void requestLocationPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Запросим разрешение
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Разрешение уже дано
            getUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                loadWeather(lat, lon);
            } else {
                Toast.makeText(this,
                        "Не удалось получить последнее местоположение",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWeather(double lat, double lon) {
        weatherViewModel.getWeather(lat, lon).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    // можно показать индикатор загрузки
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        // Обновление города и температуры
                        String city = resource.data.getName();
                        double temperature = resource.data.getMain().getTemp();
                        cityTextView.setText(city);
                        tempTextView.setText(String.format("%.1f°C", temperature));

                        // Описание погоды
                        String description = "";
                        if (resource.data.getWeather() != null && !resource.data.getWeather().isEmpty()) {
                            description = resource.data.getWeather().get(0).getDescription();
                        }
                        weatherDescriptionTextView.setText(description);

                        // Получаем код иконки и формируем URL
                        String iconCode = resource.data.getWeather().get(0).getIcon();
                        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                        // Находим ImageView для иконки погоды
                        ImageView weatherIconImageView = findViewById(R.id.main_page_weather_icon);

                        // Загружаем изображение с помощью Glide
                        Glide.with(MainPageActivity.this)
                                .load(iconUrl)
                                .into(weatherIconImageView);

                        // Обновление давления
                        int pressure = resource.data.getMain().getPressure();
                        TextView pressureTextView = findViewById(R.id.main_page_weather_pressure);
                        pressureTextView.setText("Давление: " + pressure + " гПа");

                        // Обновление влажности
                        int humidity = resource.data.getMain().getHumidity();
                        TextView humidityTextView = findViewById(R.id.main_page_weather_humidity);
                        humidityTextView.setText("Влажность: " + humidity + "%");
                    }
                    break;
                case ERROR:
                    Toast.makeText(MainPageActivity.this,
                            "Ошибка: " + resource.message,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void loadUserData(long userId) {
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        // Устанавливаем имя аккаунта
                        String login = resource.data.getLogin();
                        accountNameTextView.setText(login);

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
                        avatarImageView.setImageBitmap(resource.data);
                    }
                    break;
                case ERROR:
                    break;
            }
        });
    }

    // Обработка ответа на запрос разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение дано
                getUserLocation();
            } else {
                Toast.makeText(this, "Нет доступа к геолокации", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
