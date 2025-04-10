package com.ayungi.travelapp.view.activities.analytics;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ayungi.travelapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsResponseDto;
import com.ayungi.travelapp.viewmodel.AnalyticsViewModel;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "travel_app_prefs";
    private static final String KEY_TOTAL_BUDGET = "total_budget";

    private EditText etTotalBudget;
    private Button btnCalculate;
    private ImageButton backButton;
    private TextView tvTotalSpent, tvRemainingBudget;
    private BarChart overallBarChart;
    private RecyclerView rvCategoryAnalytics;
    private AnalyticsViewModel analyticsViewModel;
    private CategoryAnalyticsAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        initViews();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        rvCategoryAnalytics.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAnalyticsAdapter(new ArrayList<>());
        rvCategoryAnalytics.setAdapter(adapter);

        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);

        String savedBudget = sharedPreferences.getString(KEY_TOTAL_BUDGET, null);
        if (!TextUtils.isEmpty(savedBudget)) {
            etTotalBudget.setText(savedBudget);
            double totalBudget = Double.parseDouble(savedBudget);
            fetchAnalytics(totalBudget);
        }

        backButton.setOnClickListener(v -> finish());

        btnCalculate.setOnClickListener(view -> {
            String budgetStr = etTotalBudget.getText().toString();
            if (TextUtils.isEmpty(budgetStr)) {
                Toast.makeText(AnalyticsActivity.this, "Введите общий бюджет", Toast.LENGTH_SHORT).show();
                return;
            }
            // Сохраняем введённое значение в SharedPreferences
            sharedPreferences.edit().putString(KEY_TOTAL_BUDGET, budgetStr).apply();
            double totalBudget = Double.parseDouble(budgetStr);
            fetchAnalytics(totalBudget);
        });
    }

    private void initViews() {
        etTotalBudget = findViewById(R.id.etTotalBudget);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
        overallBarChart = findViewById(R.id.overallBarChart);
        rvCategoryAnalytics = findViewById(R.id.rvCategoryAnalytics);
        backButton = findViewById(R.id.back_button);
    }

    private void fetchAnalytics(double totalBudget) {
        long tripId = 1;

        analyticsViewModel.getTripAnalytics(tripId, totalBudget).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case SUCCESS:
                    TripBudgetAnalyticsResponseDto analytics = resource.data;
                    assert analytics != null;
                    updateOverallStats(analytics);
                    adapter.setCategoryList(analytics.getCategories());
                    break;
                case ERROR:
                    Toast.makeText(AnalyticsActivity.this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateOverallStats(TripBudgetAnalyticsResponseDto analytics) {
        tvTotalSpent.setText("Потрачено: " + analytics.getTotalSpent());
        tvRemainingBudget.setText("Осталось: " + analytics.getRemainingBudget());

        List<BarEntry> overallEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (var category : analytics.getCategories()) {
            overallEntries.add(new BarEntry(index, (float) category.getSpentAmount()));
            labels.add(category.getName());
            index++;
        }
        BarDataSet dataSet = new BarDataSet(overallEntries, "Расходы по категориям");
        dataSet.setColors(com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(dataSet);
        overallBarChart.setData(data);
        overallBarChart.getDescription().setEnabled(false);
        overallBarChart.invalidate();
    }
}
