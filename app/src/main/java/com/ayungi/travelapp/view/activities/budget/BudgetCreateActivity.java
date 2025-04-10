package com.ayungi.travelapp.view.activities.budget;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
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

public class BudgetCreateActivity extends AppCompatActivity {

    private EditText budgetCategoryName, budgetCategoryAmount;
    private Button saveButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private CircleImageView avatarImage;
    private TextView accountName, currentDate;
    private BudgetViewModel budgetViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_category_create);

        initViews();
        initDate(currentDate);

        // Получаем tripId из Intent
        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if(tripId == -1){
            Toast.makeText(this, "Ошибка: ID путешествия не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        saveButton.setOnClickListener(v -> doSaveBudget());
        backButton.setOnClickListener(v -> finish());

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

    private void doSaveBudget() {
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

        progressBar.setVisibility(View.VISIBLE);
        budgetViewModel.createBudgetCategory(tripId, category, plannedAmount)
                .observe(this, resource -> {
                    if (resource == null) return;
                    switch (resource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(BudgetCreateActivity.this, "Категория создана", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(BudgetCreateActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
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
