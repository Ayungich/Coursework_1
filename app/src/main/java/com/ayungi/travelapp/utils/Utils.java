package com.ayungi.travelapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.TextAppearanceSpan;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ayungi.travelapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    private static boolean isPasswordVisible = false;
    public Utils() {}

    public static void togglePasswordVisibility(EditText passwordInput, ImageButton passwordToggle) {
        if (isPasswordVisible) {
            // Скрыть пароль
            passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        } else {
            // Показать пароль
            passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordToggle.setImageResource(R.drawable.ic_visibility);
            isPasswordVisible = true;
        }
        // Перемещаем курсор в конец текста
        passwordInput.setSelection(passwordInput.getText().length());
    }

    public static String showGenderSelectionDialog(ImageButton sexChoiceButton, TextView label, Context context) {
        rotateImageButton(true, sexChoiceButton);
        final String[] genders = {"Мужской", "Женский", "Другое"};
        AtomicReference<String> selectedGender = new AtomicReference<>("");

        SpannableString title = new SpannableString("Выберите пол:");
        title.setSpan(new TextAppearanceSpan(context, R.style.CustomAlertDialogTitle), 0, title.length(), 0);

        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle(title)
                .setItems(genders, (dialog, i) -> {
                    String chosen = genders[i];
                    if ("Мужской".equals(chosen)) {
                        selectedGender.set("male");
                        label.setTextAppearance(R.style.InputtedTextStyle);
                        label.setHint(chosen);
                    } else if ("Женский".equals(chosen)) {
                        selectedGender.set("female");
                        label.setTextAppearance(R.style.InputtedTextStyle);
                        label.setHint(chosen);
                    } else {
                        selectedGender.set(null);
                        label.setTextAppearance(R.style.InputtedTextStyle);
                        label.setHint(chosen);
                    }
                })
                .setOnDismissListener(dialog -> rotateImageButton(false, sexChoiceButton))
                .show();

        return selectedGender.toString();
    }

    private static void rotateImageButton(boolean rotated, ImageButton sexChoiceButton) {
        sexChoiceButton.setImageResource(
                rotated ? R.drawable.sex_choice_arrow_rotated : R.drawable.sex_choice_arrow
        );
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidDate(String date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Включает строгую проверку
        try {
            Date parsedDate = sdf.parse(date);
            return true; // Если парсинг успешен, дата валидна
        } catch (ParseException e) {
            return false; // Если формат или дата некорректны
        }
    }

    public static boolean isAtLeastOneYearOld(String date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Строгая проверка формата
        try {
            Date parsedDate = sdf.parse(date);
            // Вычисляем дату, которая была ровно один год назад от текущего момента
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            Date oneYearAgo = cal.getTime();
            // Если введенная дата не позже, чем "один год назад", значит пользователь как минимум один год
            assert parsedDate != null;
            return parsedDate.compareTo(oneYearAgo) <= 0;
        } catch (ParseException e) {
            return false; // Дата некорректна
        }
    }

    public static boolean isValidPassword(String password) {
        // Проверка на длину
        if (password.length() < 8 || password.length() > 20) {
            return false; // Пароль слишком короткий
        }
        // Проверка на сложность
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    public static boolean isValidLogin(String login) {
        // Регулярное выражение для логина
        String loginRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9._]{5,20}$";
        return login.matches(loginRegex);
    }

    public static void initDate(TextView currentDateTextView) {
        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        currentDateTextView.setText(today);
    }
}
