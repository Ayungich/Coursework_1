package com.ayungi.travelapp.view.activities.trip;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.TripViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripCreateActivity extends AppCompatActivity {

    private EditText tripName;
    private EditText tripStartDate, tripEndDate, tripType;
    private Button saveButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private CircleImageView avatarImage;
    private TextView accountName, currentDate;
    private TripViewModel tripViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);

        initViews();
        initDate(currentDate);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
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

        // Открытие DatePickerDialog при клике на поле даты
        tripStartDate.setOnClickListener(view -> showDatePickerDialog(true));
        // Открытие DatePickerDialog при клике на поле даты
        tripEndDate.setOnClickListener(view -> showDatePickerDialog(false));

        saveButton.setOnClickListener(view -> doSaveTrip());

        backButton.setOnClickListener(v -> {
            // Возвращаемся на предыдущий экран
            finish();
        });
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountName = findViewById(R.id.trip_account_name);
        currentDate = findViewById(R.id.trip_current_date);
        tripName = findViewById(R.id.tripName);
        tripStartDate = findViewById(R.id.tripStartDate);
        tripEndDate = findViewById(R.id.tripEndDate);
        tripType = findViewById(R.id.tripType);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
    }

    private void showDatePickerDialog(boolean flag) {
        // Получаем текущую дату для предварительной установки
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Месяцы начинаются с 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TripCreateActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());
                    if (!flag) {
                        tripEndDate.setText(formattedDate);
                    }
                    else {
                        tripStartDate.setText(formattedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void doSaveTrip() {
        String name = tripName.getText().toString().trim();
        String startDate = tripStartDate.getText().toString().trim();
        String endDate = tripEndDate.getText().toString().trim();
        String type = tripType.getText().toString().trim();

        if (name.isEmpty()) {
            tripName.setError("Введите название путешествия");
            return;
        }
        if (startDate.isEmpty()) {
            tripStartDate.setError("Выберите дату");
            return;
        }
        if (endDate.isEmpty()) {
            tripEndDate.setError("Выберите дату");
            return;
        }
        if (type.isEmpty()) {
            tripType.setError("Введите тип");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        tripViewModel.createTrip(name, startDate, endDate, type).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        Log.i("TripCreateActivity", "Путешествие создано, id = " + resource.data.getId());
                        Toast.makeText(TripCreateActivity.this, "Путешествие успешно создано", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(TripCreateActivity.this, TripsActivity.class);
                        startActivity(intent);
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TripCreateActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
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
