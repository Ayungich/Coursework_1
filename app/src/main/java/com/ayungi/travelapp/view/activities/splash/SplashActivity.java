package com.ayungi.travelapp.view.activities.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.login.LoginActivity;
import com.ayungi.travelapp.view.activities.main.MainPageActivity;
import com.ayungi.travelapp.view.activities.registration.RegistrationActivity;
import com.ayungi.travelapp.viewmodel.UserViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("TravelApp", MODE_PRIVATE);
        long savedUserId = prefs.getLong("USER_ID", -1);
        Log.d(TAG, "Saved USER_ID: " + savedUserId);

        if (savedUserId != -1) {
            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            // Проверяем наличие пользователя по сохраненному USER_ID
            userViewModel.getUserById(savedUserId).observe(this, resource -> {
                if (resource == null) return;
                Log.d(TAG, "User resource status: " + resource.status);
                if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                    Log.d(TAG, "User found: " + resource.data.getLogin());
                    // Пользователь найден – переходим на главный экран
                    startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
                    finish();
                } else if (resource.status == Resource.Status.ERROR) {
                    // Пользователь не найден или произошла ошибка – очищаем USER_ID и переходим к регистрации
                    prefs.edit().remove("USER_ID").apply();
                    Toast.makeText(SplashActivity.this,
                            "Пользователь не найден. Пожалуйста, зарегистрируйтесь.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
                    finish();
                }
            });
        } else {
            // Если USER_ID не сохранён, переходим к экрану логина
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
