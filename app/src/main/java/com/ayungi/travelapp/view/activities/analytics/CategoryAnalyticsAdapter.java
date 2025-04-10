package com.ayungi.travelapp.view.activities.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.BudgetCategoryAnalyticsResponseDto;
import com.ayungi.travelapp.model.data.responses.BudgetItemAnalyticsResponseDto;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoryAnalyticsAdapter extends RecyclerView.Adapter<CategoryAnalyticsAdapter.CategoryViewHolder> {

    private List<BudgetCategoryAnalyticsResponseDto> categoryList;

    public CategoryAnalyticsAdapter(List<BudgetCategoryAnalyticsResponseDto> categoryList) {
        this.categoryList = categoryList;
    }

    public void setCategoryList(List<BudgetCategoryAnalyticsResponseDto> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_analytics, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        BudgetCategoryAnalyticsResponseDto category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        PieChart pieChart;
        BarChart barChart;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            pieChart = itemView.findViewById(R.id.itemPieChart);
            barChart = itemView.findViewById(R.id.itemBarChart);
        }

        public void bind(BudgetCategoryAnalyticsResponseDto category) {
            tvCategoryName.setText(category.getName());

            // Инициализация PieChart для разбивки расходов внутри категории
            List<BudgetItemAnalyticsResponseDto> items = category.getItems();
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            for (BudgetItemAnalyticsResponseDto item : items) {
                pieEntries.add(new PieEntry((float) item.getAmount(), item.getName()));
            }
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Расходы " + category.getName());
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();

            // Инициализация BarChart для отображения соотношения "Потрачено vs Осталось"
            float spent = (float) category.getSpentAmount();
            float planned = (float) category.getPlannedAmount();
            float remaining = planned - spent;
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(0, spent));
            barEntries.add(new BarEntry(1, remaining));

            BarDataSet barDataSet = new BarDataSet(barEntries, "План vs Факт");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.invalidate();
        }
    }
}
