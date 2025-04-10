package com.ayungi.travelapp.view.activities.diary;

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
import com.ayungi.travelapp.viewmodel.DiaryViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryCreateActivity extends AppCompatActivity {

    private EditText diaryName;
    private EditText diaryDate;
    private EditText diaryContent;
    private Button saveButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private TextView accountNameTextView, currentDateTextView, errorText;
    private CircleImageView avatarImage;
    private UserViewModel userViewModel;
    private DiaryViewModel diaryViewModel;
    private AvatarViewModel avatarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_create);

        initViews();
        initDate(currentDateTextView);

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        diaryDate.setOnClickListener(view -> showDatePickerDialog());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryCreateActivity.this, DiariesActivity.class);
            startActivity(intent);
        });
        saveButton.setOnClickListener(view -> doSaveDiary());

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

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountNameTextView = findViewById(R.id.diary_account_name);
        currentDateTextView = findViewById(R.id.diary_current_date);
        diaryName = findViewById(R.id.diaryName);
        diaryDate = findViewById(R.id.diaryDate);
        diaryContent = findViewById(R.id.diaryContent);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
    }

    private void showDatePickerDialog() {
        // Получаем текущую дату для предварительной установки в диалоге
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Месяцы начинаются с 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DiaryCreateActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Форматируем дату в формат dd.MM.yyyy
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());
                    diaryDate.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void doSaveDiary() {
        String title = diaryName.getText().toString().trim();
        String date = diaryDate.getText().toString().trim(); // Дата в формате "dd.MM.yyyy"
        String content = diaryContent.getText().toString().trim();

        if (title.isEmpty()) {
            diaryName.setError("Введите название дневника");
            return;
        }
        if (date.isEmpty()) {
            diaryDate.setError("Выберите дату");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        diaryViewModel.createDiary(title, date, content).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        Log.i("DiaryCreateActivity", "Дневник создан, id = " + resource.data.getId());
                        finish();
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(resource.message);
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
