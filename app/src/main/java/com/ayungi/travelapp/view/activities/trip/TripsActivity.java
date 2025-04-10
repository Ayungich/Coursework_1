package com.ayungi.travelapp.view.activities.trip;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.TripResponseDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.MapActivity;
import com.ayungi.travelapp.view.activities.analytics.AnalyticsActivity;
import com.ayungi.travelapp.view.activities.budget.BudgetsActivity;
import com.ayungi.travelapp.view.activities.main.MainPageActivity;
import com.ayungi.travelapp.view.activities.packing.PackingItemsActivity;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.TripViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripsActivity extends AppCompatActivity {

    private LinearLayout tripListContainer;
    private ImageButton addTripButton, backButton, sortTripButton;;
    private CircleImageView avatarImage;
    private TripViewModel tripViewModel;
    private EditText searchEditText;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private TextView accountNameTextView, emptyMessageTextView, errorText, currentDateTextView;
    private ProgressBar progressBar;
    private List<TripResponseDto> allTrips = new ArrayList<>();  // Оригинальный список поездок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        initViews();
        initDate(currentDateTextView);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        addTripButton.setOnClickListener(view -> {
            Intent intent = new Intent(TripsActivity.this, TripCreateActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, MainPageActivity.class);
            startActivity(intent);
        });

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("USER_ID", -1);
        if (userId != -1) {
            loadUserData(userId);
        }

        // Читаем avatarId из SharedPreferences
        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            // Загружаем аватар
            loadAvatar(avatarId);
        }

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        sortTripButton.setOnClickListener(v -> {
            if (allTrips != null && !allTrips.isEmpty()) {
                allTrips.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
                // После сортировки применяем фильтрацию по текущему запросу поиска:
                filterTrips(searchEditText.getText().toString());
            }
        });

        fetchTrips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTrips();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountNameTextView = findViewById(R.id.trip_account_name);
        currentDateTextView = findViewById(R.id.trip_current_date);
        tripListContainer = findViewById(R.id.trip_list_container);
        addTripButton = findViewById(R.id.add_trip_button);
        sortTripButton = findViewById(R.id.sort_trip_button);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    private void fetchTrips() {
        tripViewModel.getAllTrips().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    List<TripResponseDto> trips = resource.data;
                    allTrips = trips != null ? trips : new ArrayList<>();
                    updateTripList(allTrips);
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(resource.message);
                    Log.e("TripsActivity", "Ошибка получения путешествий: " + resource.message);
                    break;
            }
        });
    }

    private void updateTripList(List<TripResponseDto> trips) {
        tripListContainer.removeAllViews();
        if (trips == null || trips.isEmpty()) {
            if (emptyMessageTextView == null) {
                emptyMessageTextView = new TextView(TripsActivity.this);
                emptyMessageTextView.setText("Путешествий пока нет");
                emptyMessageTextView.setPadding(16, 16, 16, 16);
                emptyMessageTextView.setGravity(android.view.Gravity.CENTER);
                emptyMessageTextView.setTextAppearance(TripsActivity.this, R.style.CurrentCityStyle);
            }
            tripListContainer.addView(emptyMessageTextView);
        } else {
            for (TripResponseDto trip : trips) {
                View tripItemView = inflateTripItem(trip);
                tripListContainer.addView(tripItemView);
            }
        }
    }

    private void filterTrips(String query) {
        List<TripResponseDto> filteredList = new ArrayList<>();
        for (TripResponseDto trip : allTrips) {
            if (trip.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(trip);
            }
        }
        updateTripList(filteredList);
    }

    @SuppressLint("SetTextI18n")
    private View inflateTripItem(TripResponseDto trip) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_trip, tripListContainer, false);

        TextView titleTextView = view.findViewById(R.id.trip_title);
        TextView typeTextView = view.findViewById(R.id.trip_type);
        TextView dateTextView = view.findViewById(R.id.trip_dates);
        ImageButton btnAnalytics = view.findViewById(R.id.btn_analytics);
        ImageButton btnBudget = view.findViewById(R.id.btn_budget);
        ImageButton btnRoute = view.findViewById(R.id.btn_route);
        ImageButton btnPacking = view.findViewById(R.id.btn_packing);
        ImageButton btnEdit = view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete);

        titleTextView.setText(trip.getName());
        typeTextView.setText(trip.getType());
        // Отображаем даты как "startDate - endDate"
        dateTextView.setText(trip.getStartDate() + " - " + trip.getEndDate());

        // Обработчик для кнопки "Аналитика"
        btnAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, AnalyticsActivity.class);
            intent.putExtra("TRIP_ID", trip.getId());
            startActivity(intent);
        });

        // Обработчик для кнопки "Бюджет"
        btnBudget.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, BudgetsActivity.class);
            intent.putExtra("TRIP_ID", trip.getId());
            startActivity(intent);
        });

        // Обработчик для кнопки "Маршрут"
        btnRoute.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, MapActivity.class);
            intent.putExtra("TRIP_ID", trip.getId());
            startActivity(intent);
        });

        // Обработчик для кнопки "Список вещей"
        btnPacking.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, PackingItemsActivity.class);
            intent.putExtra("TRIP_ID", trip.getId());
            startActivity(intent);
        });

        // Обработчик для кнопки "Редактировать"
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, TripEditActivity.class);
            intent.putExtra("TRIP_ID", trip.getId());
            intent.putExtra("TRIP_NAME", trip.getName());
            intent.putExtra("TRIP_START_DATE", trip.getStartDate());
            intent.putExtra("TRIP_END_DATE", trip.getEndDate());
            intent.putExtra("TRIP_TYPE", trip.getType());
            startActivity(intent);
        });

        // Обработчик для удаления путешествия
        btnDelete.setOnClickListener(v -> {
            tripViewModel.deleteTrip(trip.getId()).observe(TripsActivity.this, resource -> {
                if (resource == null) return;
                if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS) {
                    Toast.makeText(TripsActivity.this, "Путешествие удалено", Toast.LENGTH_SHORT).show();
                    fetchTrips(); // Обновляем список
                } else if (resource.status == com.ayungi.travelapp.utils.Resource.Status.ERROR) {
                    Toast.makeText(TripsActivity.this, "Ошибка удаления: " + resource.message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void loadUserData(long userId) {
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                accountNameTextView.setText(resource.data.getLogin());
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Ошибка загрузки пользователя: " + resource.message, Toast.LENGTH_LONG).show();
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
