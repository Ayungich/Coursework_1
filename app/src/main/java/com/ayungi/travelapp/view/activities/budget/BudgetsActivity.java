package com.ayungi.travelapp.view.activities.budget;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import com.ayungi.travelapp.model.data.responses.BudgetCategoryDto;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.BudgetViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetsActivity extends AppCompatActivity {

    private LinearLayout budgetsContainer;
    private ImageButton addBudgetButton, backButton, sortCategoryButton;
    private EditText searchEditText;
    private BudgetViewModel budgetViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private TextView accountNameTextView, emptyMessageTextView, errorText, currentDate;
    private ProgressBar progressBar;
    private CircleImageView avatarImage;
    private long tripId;
    private List<BudgetCategoryDto> allBudgets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_categories);

        initViews();
        initDate(currentDate);

        // Получаем tripId из Intent
        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if (tripId == -1L) {
            Toast.makeText(this, "Ошибка: ID путешествия не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        addBudgetButton.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetsActivity.this, BudgetCreateActivity.class);
            intent.putExtra("TRIP_ID", tripId);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

        // Обработчик для сортировки категорий
        sortCategoryButton.setOnClickListener(v -> {
            if (!allBudgets.isEmpty()) {
                Collections.sort(allBudgets, (b1, b2) -> b1.getName().compareToIgnoreCase(b2.getName()));
                // Применяем фильтрацию по текущему тексту поиска
                filterBudgets(searchEditText.getText().toString());
            }
        });

        // Реализуем фильтрацию при изменении текста поиска
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBudgets(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
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

        fetchBudgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBudgets();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        currentDate = findViewById(R.id.budget_current_date);
        accountNameTextView = findViewById(R.id.budget_account_name);
        budgetsContainer = findViewById(R.id.budgets_container);
        addBudgetButton = findViewById(R.id.add_item_button);
        backButton = findViewById(R.id.back_button);
        sortCategoryButton = findViewById(R.id.sort_category_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    private void fetchBudgets() {
        budgetViewModel.getBudgetCategories(tripId).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    budgetsContainer.removeAllViews();
                    List<BudgetCategoryDto> budgets = resource.data;
                    allBudgets = budgets != null ? budgets : new ArrayList<>();
                    filterBudgets(searchEditText.getText().toString());
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
     * Фильтрует список категорий по названию с учётом регистра.
     */
    private void filterBudgets(String query) {
        List<BudgetCategoryDto> filteredList = new ArrayList<>();
        for (BudgetCategoryDto budget : allBudgets) {
            if (budget.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(budget);
            }
        }
        updateBudgetsList(filteredList);
    }

    /**
     * Обновляет список категорий на экране.
     */
    private void updateBudgetsList(List<BudgetCategoryDto> budgets) {
        budgetsContainer.removeAllViews();
        if (budgets == null || budgets.isEmpty()) {
            if (emptyMessageTextView == null) {
                emptyMessageTextView = new TextView(BudgetsActivity.this);
                emptyMessageTextView.setText("Категорий пока нет");
                emptyMessageTextView.setPadding(16, 16, 16, 16);
                emptyMessageTextView.setGravity(android.view.Gravity.CENTER);
                emptyMessageTextView.setTextAppearance(BudgetsActivity.this, R.style.CurrentCityStyle);
            }
            budgetsContainer.addView(emptyMessageTextView);
        } else {
            for (BudgetCategoryDto budget : budgets) {
                View budgetItemView = inflateBudgetItem(budget);
                budgetsContainer.addView(budgetItemView);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private View inflateBudgetItem(BudgetCategoryDto budget) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_budget_category, budgetsContainer, false);

        TextView categoryTextView = view.findViewById(R.id.budget_category_name);
        TextView amountTextView = view.findViewById(R.id.budget_category_amount);

        categoryTextView.setText(budget.getName());
        if (budget.getPlannedAmount() != null) {
            amountTextView.setText(budget.getPlannedAmount().toString());
        }

        ImageButton btnItems = view.findViewById(R.id.btn_items);
        ImageButton btnEdit = view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete);

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetsActivity.this, BudgetEditActivity.class);
            intent.putExtra("BUDGET_ID", budget.getId());
            intent.putExtra("BUDGET_CATEGORY_NAME", budget.getName());
            intent.putExtra("BUDGET_CATEGORY_AMOUNT", budget.getPlannedAmount());
            startActivity(intent);
        });

        btnItems.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetsActivity.this, BudgetItemsActivity.class);
            intent.putExtra("BUDGET_CATEGORY_ID", budget.getId());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            budgetViewModel.deleteBudgetCategory(budget.getId()).observe(BudgetsActivity.this, resource -> {
                if (resource == null) return;
                if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS) {
                    Toast.makeText(BudgetsActivity.this, "Категория удалена", Toast.LENGTH_SHORT).show();
                    fetchBudgets();
                } else if (resource.status == com.ayungi.travelapp.utils.Resource.Status.ERROR) {
                    Toast.makeText(BudgetsActivity.this, "Ошибка удаления: " + resource.message, Toast.LENGTH_SHORT).show();
                }
            });
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
