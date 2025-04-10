package com.ayungi.travelapp.view.activities.packing;

import static com.ayungi.travelapp.utils.Utils.initDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.viewmodel.AvatarViewModel;
import com.ayungi.travelapp.viewmodel.PackingItemViewModel;
import com.ayungi.travelapp.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackingItemCreateActivity extends AppCompatActivity {

    private EditText packingItemName;
    private CheckBox packingItemDone;
    private Button saveButton;
    private ImageButton backButton;
    private CircleImageView avatarImage;
    private ProgressBar progressBar;
    private TextView accountName, currentDate;
    private PackingItemViewModel packingItemViewModel;
    private UserViewModel userViewModel;
    private AvatarViewModel avatarViewModel;
    private Long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_item_create);

        initViews();
        initDate(currentDate);

        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if(tripId == -1L) {
            Toast.makeText(this, "Ошибка: ID путешествия не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        packingItemViewModel = new ViewModelProvider(this).get(PackingItemViewModel.class);
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

        saveButton.setOnClickListener(v -> doSavePackingItem());
        backButton.setOnClickListener(v -> finish());
    }

    private void initViews(){
        avatarImage = findViewById(R.id.avatar);
        accountName = findViewById(R.id.packing_account_name);
        currentDate = findViewById(R.id.packing_current_date);
        packingItemName = findViewById(R.id.packingItemName);
        packingItemDone = findViewById(R.id.packingItemDone);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
    }

    private void doSavePackingItem(){
        String name = packingItemName.getText().toString().trim();
        boolean taken = packingItemDone.isChecked();

        if(name.isEmpty()){
            packingItemName.setError("Введите название вещи");
            return;
        }
        progressBar.setVisibility(android.view.View.VISIBLE);
        packingItemViewModel.createPackingItem(name, taken, tripId).observe(this, resource -> {
            if(resource == null) return;
            switch(resource.status){
                case LOADING:
                    progressBar.setVisibility(android.view.View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(android.view.View.GONE);
                    if(resource.data != null){
                        Log.i("PackingItemCreate", "Вещь создана, id = " + resource.data.getId());
                        Toast.makeText(this, "Вещь успешно создана", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
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
