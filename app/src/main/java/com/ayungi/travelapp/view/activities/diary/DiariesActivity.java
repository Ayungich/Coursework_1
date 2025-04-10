package com.ayungi.travelapp.view.activities.diary;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.DiaryResponseDto;
import com.ayungi.travelapp.view.activities.main.MainPageActivity;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.DiaryViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiariesActivity extends AppCompatActivity {

    private LinearLayout diaryListContainer;
    private ImageButton addDiaryButton, backButton, sortDiaryButton;
    private EditText searchEditText;
    private DiaryViewModel diaryViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private TextView accountNameTextView, emptyMessageTextView, errorText, currentDateTextView;
    private ProgressBar progressBar;
    private CircleImageView avatarImage;
    // Список всех дневников, полученных с сервера
    private List<DiaryResponseDto> allDiaries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaries);

        initViews();
        initDate(currentDateTextView);

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        addDiaryButton.setOnClickListener(view -> {
            Intent intent = new Intent(DiariesActivity.this, DiaryCreateActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DiariesActivity.this, MainPageActivity.class);
            startActivity(intent);
        });

        // Обработчик сортировки дневников по названию
        sortDiaryButton.setOnClickListener(v -> {
            if (!allDiaries.isEmpty()) {
                allDiaries.sort((d1, d2) -> d1.getTitle().compareToIgnoreCase(d2.getTitle()));
                // После сортировки применяем фильтрацию с учётом текущего поискового запроса
                filterDiaries(searchEditText.getText().toString());
            }
        });

        // Слушатель изменений текста в поле поиска для динамической фильтрации
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // не требуется
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDiaries(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // не требуется
            }
        });

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1);
        if (userId != -1) {
            loadUserData(userId);
        }

        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            loadAvatar(avatarId);
        }

        fetchDiaries();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDiaries();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountNameTextView = findViewById(R.id.diary_account_name);
        currentDateTextView = findViewById(R.id.diary_current_date);
        diaryListContainer = findViewById(R.id.diary_list_container);
        addDiaryButton = findViewById(R.id.add_diary_button);
        backButton = findViewById(R.id.back_button);
        sortDiaryButton = findViewById(R.id.sort_diary_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    private void fetchDiaries() {
        diaryViewModel.getAllDiaries().observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    List<DiaryResponseDto> diaries = resource.data;
                    allDiaries = diaries != null ? diaries : new ArrayList<>();
                    filterDiaries(searchEditText.getText().toString());
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(resource.message);
                    break;
            }
        });
    }

    /**
     * Фильтрует список дневников по названию, используя поисковый запрос.
     */
    private void filterDiaries(String query) {
        List<DiaryResponseDto> filteredList = new ArrayList<>();
        for (DiaryResponseDto diary : allDiaries) {
            if (diary.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(diary);
            }
        }
        updateDiaryList(filteredList);
    }

    /**
     * Обновляет список дневников в контейнере.
     */
    private void updateDiaryList(List<DiaryResponseDto> diaries) {
        diaryListContainer.removeAllViews();
        if (diaries == null || diaries.isEmpty()) {
            if (emptyMessageTextView == null) {
                emptyMessageTextView = new TextView(DiariesActivity.this);
                emptyMessageTextView.setText("Дневников пока нет");
                emptyMessageTextView.setPadding(16, 16, 16, 16);
                emptyMessageTextView.setGravity(android.view.Gravity.CENTER);
                emptyMessageTextView.setTextAppearance(DiariesActivity.this, R.style.CurrentCityStyle);
            }
            diaryListContainer.addView(emptyMessageTextView);
        } else {
            for (DiaryResponseDto diary : diaries) {
                View diaryItemView = inflateDiaryItem(diary);
                diaryListContainer.addView(diaryItemView);
            }
        }
    }

    private View inflateDiaryItem(DiaryResponseDto diary) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_diary, diaryListContainer, false);

        TextView titleTextView = view.findViewById(R.id.trip_title);
        TextView dateTextView = view.findViewById(R.id.trip_date);
        ImageButton btnEdit = view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete);

        titleTextView.setText(diary.getTitle());
        dateTextView.setText(diary.getDate());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(DiariesActivity.this, DiaryEditActivity.class);
            intent.putExtra("DIARY_ID", diary.getId());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            diaryViewModel.deleteDiary(diary.getId()).observe(DiariesActivity.this, resource -> {
                if (resource == null) return;
                if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS) {
                    Toast.makeText(DiariesActivity.this, "Дневник удален", Toast.LENGTH_SHORT).show();
                    fetchDiaries();
                } else if (resource.status == com.ayungi.travelapp.utils.Resource.Status.ERROR) {
                    Toast.makeText(DiariesActivity.this, "Ошибка удаления: " + resource.message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Обработчик для открытия дневника
        view.setOnClickListener(v -> {
            Intent intent = new Intent(DiariesActivity.this, DiaryActivity.class);
            intent.putExtra("DIARY_ID", diary.getId());
            startActivity(intent);
        });

        return view;
    }

    private void loadUserData(long userId) {
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS && resource.data != null) {
                accountNameTextView.setText(resource.data.getLogin());
            } else if (resource.status == com.ayungi.travelapp.utils.Resource.Status.ERROR) {
                Toast.makeText(this, "Ошибка загрузки пользователя: " + resource.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadAvatar(long avatarId) {
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
