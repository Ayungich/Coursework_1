package com.ayungi.travelapp.view.activities.packing;

import static com.ayungi.travelapp.utils.Utils.initDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.PackingItemResponseDto;
import com.ayungi.travelapp.model.data.requests.PackingItemEditRequestDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.PackingItemViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackingItemsActivity extends AppCompatActivity {

    private LinearLayout packingItemsContainer;
    private ImageButton addItemButton, backButton, sortItemButton;
    private EditText searchEditText;
    private CircleImageView avatarImage;
    private PackingItemViewModel packingItemViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private TextView accountNameTextView, emptyMessageTextView, errorText, currentDateTextView;
    private ProgressBar progressBar;
    private Long tripId;
    // Полный список вещей, полученных с сервера
    private List<PackingItemResponseDto> allPackingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_items);

        initViews();
        initDate(currentDateTextView);

        // Получаем tripId из Intent
        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if (tripId == -1L) {
            Toast.makeText(this, "Ошибка: ID путешествия не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        packingItemViewModel = new ViewModelProvider(this).get(PackingItemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(PackingItemsActivity.this, PackingItemCreateActivity.class);
            intent.putExtra("TRIP_ID", tripId);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

        // Обработчик сортировки вещей по названию
        sortItemButton.setOnClickListener(v -> {
            if (!allPackingItems.isEmpty()) {
                Collections.sort(allPackingItems, (i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
                // После сортировки применяем фильтрацию по текущему поисковому запросу
                filterPackingItems(searchEditText.getText().toString());
            }
        });

        // Фильтрация при вводе в поле поиска
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // не требуется
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPackingItems(s.toString());
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {
                // не требуется
            }
        });

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1);
        if (userId != -1) {
            loadUserData(userId);
        }

        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            loadAvatar(avatarId);
        }

        fetchPackingItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPackingItems();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountNameTextView = findViewById(R.id.packing_account_name);
        currentDateTextView = findViewById(R.id.packing_current_date);
        packingItemsContainer = findViewById(R.id.packing_items_container);
        addItemButton = findViewById(R.id.add_item_button);
        backButton = findViewById(R.id.back_button);
        sortItemButton = findViewById(R.id.sort_item_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    private void fetchPackingItems() {
        packingItemViewModel.getAllPackingItems(tripId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    List<PackingItemResponseDto> items = resource.data;
                    allPackingItems = items != null ? items : new ArrayList<>();
                    filterPackingItems(searchEditText.getText().toString());
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
     * Фильтрует список вещей по названию, используя поисковый запрос.
     */
    private void filterPackingItems(String query) {
        List<PackingItemResponseDto> filteredList = new ArrayList<>();
        for (PackingItemResponseDto item : allPackingItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        updatePackingItemsList(filteredList);
    }

    /**
     * Обновляет список вещей в контейнере.
     */
    private void updatePackingItemsList(List<PackingItemResponseDto> items) {
        packingItemsContainer.removeAllViews();
        if (items == null || items.isEmpty()) {
            if (emptyMessageTextView == null) {
                emptyMessageTextView = new TextView(PackingItemsActivity.this);
                emptyMessageTextView.setText("Вещей пока нет");
                emptyMessageTextView.setPadding(16, 16, 16, 16);
                emptyMessageTextView.setGravity(android.view.Gravity.CENTER);
                emptyMessageTextView.setTextAppearance(PackingItemsActivity.this, R.style.CurrentCityStyle);
            }
            packingItemsContainer.addView(emptyMessageTextView);
        } else {
            for (PackingItemResponseDto item : items) {
                View itemView = inflatePackingItem(item);
                packingItemsContainer.addView(itemView);
            }
        }
    }

    private View inflatePackingItem(PackingItemResponseDto item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_item, packingItemsContainer, false);
        TextView nameTextView = view.findViewById(R.id.item_name);
        CheckBox itemCheckbox = view.findViewById(R.id.item_checkbox);
        ImageButton btnEdit = view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete);

        nameTextView.setText(item.getName());
        itemCheckbox.setChecked(item.getTaken() != null && item.getTaken());

        // При изменении состояния чекбокса обновляем только флаг вещи
        itemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            packingItemViewModel.editPackingItem(item.getId(),
                            new PackingItemEditRequestDto(item.getName(), isChecked))
                    .observe(PackingItemsActivity.this, resource -> {
                        if (resource == null) return;
                        if (resource.status == Resource.Status.ERROR) {
                            Toast.makeText(PackingItemsActivity.this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
                        } else if (resource.status == Resource.Status.SUCCESS) {
                            Log.i("PackingItemsActivity", "Флаг обновлён для вещи id=" + item.getId());
                        }
                    });
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(PackingItemsActivity.this, PackingItemEditActivity.class);
            intent.putExtra("ITEM_ID", item.getId());
            intent.putExtra("TRIP_ID", tripId);
            intent.putExtra("ITEM_NAME", item.getName());
            intent.putExtra("ITEM_TAKEN", item.getTaken());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            packingItemViewModel.deletePackingItem(item.getId()).observe(PackingItemsActivity.this, resource -> {
                if (resource == null) return;
                if (resource.status == Resource.Status.SUCCESS) {
                    Toast.makeText(PackingItemsActivity.this, "Вещь удалена", Toast.LENGTH_SHORT).show();
                    fetchPackingItems(); // обновляем список
                } else if (resource.status == Resource.Status.ERROR) {
                    Toast.makeText(PackingItemsActivity.this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
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
