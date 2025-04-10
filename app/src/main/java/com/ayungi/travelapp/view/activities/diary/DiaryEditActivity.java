package com.ayungi.travelapp.view.activities.diary;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.requests.DiaryRequestDto;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.DiaryViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiaryEditActivity extends AppCompatActivity {

    private EditText diaryTitle;
    private EditText diaryDate;
    private EditText diaryContent;
    private Button saveButton;
    private ImageButton backButton;
    private CircleImageView avatarImage;
    private TextView accountName, currentDate;
    private ProgressBar progressBar;
    private DiaryViewModel diaryViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private Long diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        initViews();
        initDate(currentDate);

        diaryId = getIntent().getLongExtra("DIARY_ID", -1);
        if (diaryId == -1L) {
            Toast.makeText(this, "Ошибка: ID дневника не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        diaryViewModel.getDiaryById(diaryId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS && resource.data != null) {
                diaryTitle.setText(resource.data.getTitle());
                diaryDate.setText(resource.data.getDate());
                diaryContent.setText(resource.data.getContent());
            }
        });

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

        saveButton.setOnClickListener(v -> doUpdateDiary());
        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountName = findViewById(R.id.diary_account_name);
        currentDate = findViewById(R.id.diary_current_date);
        diaryTitle = findViewById(R.id.diaryName);
        diaryDate = findViewById(R.id.diaryDate);
        diaryContent = findViewById(R.id.diaryContent);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
    }

    private void doUpdateDiary() {
        String title = diaryTitle.getText().toString().trim();
        String date = diaryDate.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();

        if (title.isEmpty()) {
            diaryTitle.setError("Введите название дневника");
            return;
        }
        if (date.isEmpty()) {
            diaryDate.setError("Введите дату");
            return;
        }

        DiaryRequestDto request = new DiaryRequestDto(title, date, content);

        progressBar.setVisibility(android.view.View.VISIBLE);
        diaryViewModel.updateDiary(diaryId, request).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(android.view.View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(DiaryEditActivity.this, "Дневник обновлен", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(DiaryEditActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
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
