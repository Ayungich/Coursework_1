package com.ayungi.travelapp.view.activities.diary;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.ayungi.travelapp.model.data.requests.DiaryRequestDto;
import com.ayungi.travelapp.model.data.responses.DiaryResponseDto;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.DiaryViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryActivity extends AppCompatActivity {

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
    private long diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        initViews();
        initDate(currentDateTextView);

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
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

        // Получаем diaryId из Intent
        diaryId = getIntent().getLongExtra("DIARY_ID", -1);
        if (diaryId == -1) {
            Toast.makeText(this, "Ошибка: diaryId не найден", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fetchDiaryDetails();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryActivity.this, DiariesActivity.class);
            startActivity(intent);
        });
        // При нажатии на кнопку "Сохранить" обновляем дневник
        saveButton.setOnClickListener(v -> updateDiary());
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

    private void fetchDiaryDetails() {
        diaryViewModel.getDiaryById(diaryId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    DiaryResponseDto diary = resource.data;
                    if (diary != null) {
                        diaryName.setText(diary.getTitle());
                        diaryDate.setText(diary.getDate()); // Ожидается формат "dd.MM.yyyy"
                        diaryContent.setText(diary.getContent());
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(resource.message);
                    Log.e("DiaryActivity", "Ошибка получения дневника: " + resource.message);
                    break;
            }
        });
    }

    private void updateDiary() {
        String title = diaryName.getText().toString().trim();
        String date = diaryDate.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();

        if (title.isEmpty()) {
            diaryName.setError("Введите название дневника");
            return;
        }
        if (date.isEmpty()) {
            diaryDate.setError("Введите дату");
            return;
        }

        DiaryRequestDto request = new DiaryRequestDto(title, date, content);

        diaryViewModel.updateDiary(diaryId, request).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DiaryActivity.this, "Дневник обновлен", Toast.LENGTH_SHORT).show();
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
