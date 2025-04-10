package com.ayungi.travelapp.view.activities.budget;

import static com.ayungi.travelapp.utils.Utils.initDate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.ayungi.travelapp.R;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.BudgetItemViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class BudgetItemCreateActivity extends AppCompatActivity {

    private EditText itemNameEditText, itemAmountEditText;
    private TextView accountNameTextView, errorText, currentDate;
    private CircleImageView avatarImage;
    private Button saveButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private BudgetItemViewModel budgetItemViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private Long categoryId; // ID категории, к которой добавляется статья расходов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_item_create);

        initViews();
        initDate(currentDate);

        // Получаем CATEGORY_ID из Intent
        categoryId = getIntent().getLongExtra("CATEGORY_ID", -1);
        if (categoryId == -1L) {
            Toast.makeText(this, "Ошибка: ID категории не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        budgetItemViewModel = new ViewModelProvider(this).get(BudgetItemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        avatarViewModel = new ViewModelProvider(this).get(AvatarViewModel.class);

        saveButton.setOnClickListener(v -> createBudgetItem());
        backButton.setOnClickListener(v -> finish());

        long userId = getSharedPreferences("TravelApp", MODE_PRIVATE).getLong("USER_ID", -1);
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
        currentDate = findViewById(R.id.budget_current_date);
        accountNameTextView = findViewById(R.id.budget_account_name);
        itemNameEditText = findViewById(R.id.item_name);
        itemAmountEditText = findViewById(R.id.item_amount);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
    }

    private void createBudgetItem() {
        String name = itemNameEditText.getText().toString().trim();
        String amountStr = itemAmountEditText.getText().toString().trim();

        if(name.isEmpty()){
            itemNameEditText.setError("Введите название статьи");
            return;
        }
        if(amountStr.isEmpty()){
            itemAmountEditText.setError("Введите сумму");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        budgetItemViewModel.createBudgetItem(categoryId, name, amountStr)
                .observe(this, resource -> {
                    if (resource == null) return;
                    switch (resource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            errorText.setVisibility(View.GONE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(BudgetItemCreateActivity.this, "Статья расходов создана", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(resource.message);
                            Toast.makeText(BudgetItemCreateActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
                            break;
                    }
                });
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
