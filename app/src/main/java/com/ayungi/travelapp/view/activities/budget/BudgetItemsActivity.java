package com.ayungi.travelapp.view.activities.budget;

import static com.ayungi.travelapp.utils.Utils.initDate;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.BudgetItemResponseDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.BudgetItemViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetItemsActivity extends AppCompatActivity {

    private LinearLayout itemsContainer;
    private ImageButton addItemButton, backButton, sortItemButton;
    private ProgressBar progressBar;
    private CircleImageView avatarImage;
    private TextView accountNameTextView, errorText, currentDate;
    private EditText searchEditText;
    private BudgetItemViewModel budgetItemViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private Long categoryId;
    private List<BudgetItemResponseDto> allBudgetItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_items);

        initViews();
        initDate(currentDate);

        // Получаем CATEGORY_ID из Intent
        categoryId = getIntent().getLongExtra("BUDGET_CATEGORY_ID", -1);
        if (categoryId == -1L) {
            Toast.makeText(this, "Ошибка: ID категории не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        budgetItemViewModel = new ViewModelProvider(this).get(BudgetItemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1);
        if (userId != -1) {
            loadUserData(userId);
        }
        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            loadAvatar(avatarId);
        }

        backButton.setOnClickListener(v -> finish());

        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetItemsActivity.this, BudgetItemCreateActivity.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            startActivity(intent);
        });

        // Обработчик сортировки статей расходов по названию
        sortItemButton.setOnClickListener(v -> {
            if (!allBudgetItems.isEmpty()) {
                Collections.sort(allBudgetItems, (item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()));
                filterBudgetItems(searchEditText.getText().toString());
            }
        });

        // Реализуем фильтрацию при изменении текста поиска
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // не требуется
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBudgetItems(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // не требуется
            }
        });

        fetchBudgetItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBudgetItems();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        currentDate = findViewById(R.id.budget_current_date);
        accountNameTextView = findViewById(R.id.budget_account_name);
        itemsContainer = findViewById(R.id.budget_items_container);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        addItemButton = findViewById(R.id.add_item_button);
        backButton = findViewById(R.id.back_button);
        sortItemButton = findViewById(R.id.sort_item_button);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    private void fetchBudgetItems() {
        budgetItemViewModel.getBudgetItems(categoryId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    List<BudgetItemResponseDto> items = resource.data;
                    allBudgetItems = items != null ? items : new ArrayList<>();
                    filterBudgetItems(searchEditText.getText().toString());
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
     * Фильтрует список статей расходов по названию.
     */
    private void filterBudgetItems(String query) {
        List<BudgetItemResponseDto> filteredList = new ArrayList<>();
        for (BudgetItemResponseDto item : allBudgetItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        updateBudgetItemsList(filteredList);
    }

    /**
     * Обновляет список статей расходов на экране.
     */
    private void updateBudgetItemsList(List<BudgetItemResponseDto> items) {
        itemsContainer.removeAllViews();
        if (items == null || items.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("Нет статей расходов");
            emptyMessage.setPadding(16, 16, 16, 16);
            emptyMessage.setGravity(android.view.Gravity.CENTER);
            emptyMessage.setTextAppearance(this, R.style.CurrentCityStyle);
            itemsContainer.addView(emptyMessage);
        } else {
            for (BudgetItemResponseDto item : items) {
                View itemView = inflateBudgetItem(item);
                itemsContainer.addView(itemView);
            }
        }
    }

    private View inflateBudgetItem(BudgetItemResponseDto item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_budget, itemsContainer, false);

        TextView nameTextView = view.findViewById(R.id.budget_item_name);
        TextView amountTextView = view.findViewById(R.id.budget_item_amount);
        ImageButton btnEdit = view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete);

        nameTextView.setText(item.getName());
        amountTextView.setText(item.getAmount().toPlainString());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetItemsActivity.this, BudgetItemEditActivity.class);
            intent.putExtra("BUDGET_ITEM_ID", item.getId());
            intent.putExtra("BUDGET_ITEM_NAME", item.getName());
            intent.putExtra("BUDGET_ITEM_AMOUNT", item.getAmount().toPlainString());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            budgetItemViewModel.deleteBudgetItem(item.getId()).observe(BudgetItemsActivity.this, resource -> {
                if (resource == null) return;
                if (resource.status == Resource.Status.SUCCESS) {
                    Toast.makeText(BudgetItemsActivity.this, "Статья расходов удалена", Toast.LENGTH_SHORT).show();
                    fetchBudgetItems();
                } else if (resource.status == Resource.Status.ERROR) {
                    Toast.makeText(BudgetItemsActivity.this, "Ошибка удаления: " + resource.message, Toast.LENGTH_SHORT).show();
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
