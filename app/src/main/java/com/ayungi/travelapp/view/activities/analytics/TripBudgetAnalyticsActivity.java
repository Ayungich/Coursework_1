package com.ayungi.travelapp.view.activities.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.BudgetCategoryAnalyticsDto;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.viewmodel.BudgetAnalyticsViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class TripBudgetAnalyticsActivity extends AppCompatActivity {

    private BudgetAnalyticsViewModel analyticsViewModel;
    private ColumnChartView columnChartView;
    private PieChartView pieChartView;
    private ImageButton backButton;
    private Long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_budget_analytics);

        // Инициализация вьюшек из layout
        columnChartView = findViewById(R.id.columnChartView);
        pieChartView = findViewById(R.id.pieChartView);
        backButton = findViewById(R.id.back_button);

        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if (tripId == -1L) {
            Toast.makeText(this, "Ошибка: ID поездки не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        backButton.setOnClickListener(v -> finish());

        analyticsViewModel = new ViewModelProvider(this).get(BudgetAnalyticsViewModel.class);

        loadAnalytics();
    }

    private void loadAnalytics() {
        analyticsViewModel.getTripBudgetAnalytics(tripId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                displayAnalytics(resource.data);
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(TripBudgetAnalyticsActivity.this, "Ошибка: " + resource.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayAnalytics(TripBudgetAnalyticsDto analytics) {
        // Отображаем столбчатую диаграмму для категорий
        displayBarChart(analytics);

        // Отображаем круговую диаграмму для общих расходов
        displayPieChart(analytics);
    }

    private void displayBarChart(TripBudgetAnalyticsDto analytics) {
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();

        int index = 0;
        if (analytics.getCategoryAnalytics() != null && !analytics.getCategoryAnalytics().isEmpty()) {
            for (BudgetCategoryAnalyticsDto cat : analytics.getCategoryAnalytics()) {
                float expenseValue = cat.getExpenses() != null ? cat.getExpenses().floatValue() : 0f;
                List<SubcolumnValue> values = new ArrayList<>();
                // Если значение равно нулю – задаём минимальное значение для визуализации
                if (expenseValue == 0f) {
                    expenseValue = 0.1f;
                }
                values.add(new SubcolumnValue(expenseValue, Color.rgb(63, 81, 181)));

                Column column = new Column(values);
                column.setHasLabels(true);
                columns.add(column);

                AxisValue axisValue = new AxisValue(index);
                axisValue.setLabel(cat.getName());
                axisValues.add(axisValue);
                index++;
            }
        } else {
            Toast.makeText(this, "Нет данных для столбчатой диаграммы", Toast.LENGTH_SHORT).show();
        }

        ColumnChartData barData = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        barData.setAxisXBottom(axisX);
        barData.setAxisYLeft(axisY);

        columnChartView.setColumnChartData(barData);
        columnChartView.invalidate();
    }

    private void displayPieChart(TripBudgetAnalyticsDto analytics) {
        // Если общий запланированный бюджет равен нулю, невозможно корректно посчитать долю.
        if (analytics.getTotalPlanned() == null || analytics.getTotalPlanned().compareTo(BigDecimal.ZERO) <= 0) {
            Toast.makeText(this, "Нет данных для круговой диаграммы", Toast.LENGTH_SHORT).show();
            return;
        }

        // Вычисляем оставшийся бюджет: planned - expenses (если больше 0)
        BigDecimal remaining = analytics.getTotalPlanned().subtract(analytics.getTotalExpenses());
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }
        float expenses = analytics.getTotalExpenses().floatValue();
        float remain = remaining.floatValue();

        List<lecho.lib.hellocharts.model.SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new lecho.lib.hellocharts.model.SliceValue(expenses, Color.rgb(244, 67, 54)).setLabel("Расходы"));
        sliceValues.add(new lecho.lib.hellocharts.model.SliceValue(remain, Color.rgb(76, 175, 80)).setLabel("Осталось"));

        lecho.lib.hellocharts.model.PieChartData pieData = new lecho.lib.hellocharts.model.PieChartData(sliceValues);
        pieData.setHasLabels(true);
        pieData.setValueLabelTextSize(14);

        // Устанавливаем данные диаграммы
        lecho.lib.hellocharts.view.PieChartView pieChartView = findViewById(R.id.pieChartView);
        pieChartView.setPieChartData(pieData);
        pieChartView.invalidate();
    }
}
