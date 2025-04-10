package com.ayungi.travelapp.view.activities.budget;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.requests.BudgetCategoryRequestDto;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.BudgetViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import java.math.BigDecimal;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetEditActivity extends AppCompatActivity {

    private EditText budgetCategoryName, budgetCategoryAmount;
    private Button saveButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private BudgetViewModel budgetViewModel;
    private Long budgetId, budgetAmount;
    private String budgetName;
    private CircleImageView avatarImage;
    private TextView accountName, currentDate;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_category_edit);

        initViews();
        initDate(currentDate);

        budgetId = getIntent().getLongExtra("BUDGET_ID", -1);
        if (budgetId == -1L) {
            Toast.makeText(this, "Ошибка: ID категории не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        budgetName = getIntent().getStringExtra("BUDGET_CATEGORY_NAME");
        budgetCategoryName.setText(budgetName);

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        // Получаем данные категории и заполняем поле
        budgetViewModel.getBudgetCategoryById(budgetId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS && resource.data != null) {
                budgetCategoryName.setText(resource.data.getName());
            }
        });

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("USER_ID", -1);
        if (userId != -1) {
            loadUserData(userId);
        }

        long avatarId = getSharedPreferences("TravelApp", MODE_PRIVATE)
                .getLong("AVATAR_ID", -1);
        if (avatarId != -1) {
            loadAvatar(avatarId);
        }

        saveButton.setOnClickListener(v -> doUpdateBudget());
        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatar);
        accountName = findViewById(R.id.budget_account_name);
        currentDate = findViewById(R.id.budget_current_date);
        budgetCategoryName = findViewById(R.id.budgetCategoryName);
        budgetCategoryAmount = findViewById(R.id.budgetCategoryAmount);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
    }

    private void doUpdateBudget() {
        String category = budgetCategoryName.getText().toString().trim();
        String amountStr = budgetCategoryAmount.getText().toString().trim();

        if (category.isEmpty()) {
            budgetCategoryName.setError("Введите название категории");
            return;
        }

        // Парсинг планируемой суммы (BigDecimal)
        BigDecimal plannedAmount = null;
        if (!amountStr.isEmpty()) {
            try {
                plannedAmount = new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                budgetCategoryAmount.setError("Неверный формат суммы");
                return;
            }
        }

        progressBar.setVisibility(android.view.View.VISIBLE);
        budgetViewModel.updateBudgetCategory(budgetId, category, plannedAmount).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(android.view.View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(BudgetEditActivity.this, "Категория обновлена", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(BudgetEditActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void loadUserData(long userId) {
        userViewModel.getUserById(userId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == com.ayungi.travelapp.utils.Resource.Status.SUCCESS && resource.data != null) {
                accountName.setText(resource.data.getLogin());
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
