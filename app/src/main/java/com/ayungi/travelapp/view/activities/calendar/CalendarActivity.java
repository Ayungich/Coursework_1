package com.ayungi.travelapp.view.activities.calendar;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.TripResponseDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.trip.TripsActivity;
import com.ayungi.travelapp.viewmodel.TripViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private ImageButton backButton;
    private TripViewModel tripViewModel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    // Карта, в которую будем записывать даты границ (start или end) и соответствующее путешествие
    private final Map<CalendarDay, TripResponseDto> tripBoundariesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initViews();
        initViewModel();
        loadTrips();
        initDateClickListener();

        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        calendarView = findViewById(R.id.calendarView);
    }

    private void initViewModel() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
    }

    private void loadTrips() {
        tripViewModel.getAllTrips().observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                List<TripResponseDto> trips = resource.data;
                // Предопределяем массив цветов для разных поездок
                int[] colors = {0xFF009688, 0xFF795548, 0xFFE91E63, 0xFF3F51B5, 0xFF4CAF50};
                int colorIndex = 0;
                for (TripResponseDto trip : trips) {
                    Date startDate = parseDate(trip.getStartDate());
                    Date endDate = parseDate(trip.getEndDate());
                    if (startDate != null && endDate != null) {
                        CalendarDay startDay = CalendarDay.from(startDate);
                        CalendarDay endDay = CalendarDay.from(endDate);
                        int color = colors[colorIndex % colors.length];

                        // Сохраняем данные в карте: по дате начало/конца будем знать, какое путешествие выбрано
                        tripBoundariesMap.put(startDay, trip);
                        tripBoundariesMap.put(endDay, trip);

                        // Добавляем декоратор для даты начала
                        calendarView.addDecorator(new TripBoundaryDecorator(startDay, color, trip.getName() + " (старт)"));
                        // Добавляем декоратор для даты окончания
                        calendarView.addDecorator(new TripBoundaryDecorator(endDay, color, trip.getName() + " (финиш)"));
                        colorIndex++;
                    }
                }
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(CalendarActivity.this, "Ошибка загрузки поездок: " + resource.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Date parseDate(String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e("CalendarActivity", "Ошибка парсинга даты: " + dateStr, e);
            return null;
        }
    }

    /**
     * Добавляем слушатель, который отлавливает нажатие по ячейке календаря.
     * Если выбранная дата соответствует началу или окончанию какого-либо путешествия,
     * открываем TripsActivity с соответствующим идентификатором.
     */
    private void initDateClickListener() {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (tripBoundariesMap.containsKey(date)) {
                TripResponseDto trip = tripBoundariesMap.get(date);
                if (trip != null) {
                    // Запускаем TripsActivity с передачей идентификатора путешествия
                    Intent intent = new Intent(CalendarActivity.this, TripsActivity.class);
                    intent.putExtra("tripId", trip.getId());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Декоратор для выделения границ путешествия (начало или конец) в виде круга с обводкой и текстом.
     */
    private class TripBoundaryDecorator implements DayViewDecorator {
        private final CalendarDay boundaryDate;
        private final Paint fillPaint;
        private final Paint strokePaint;
        private final String tripName;

        public TripBoundaryDecorator(CalendarDay boundaryDate, int baseColor, String tripName) {
            this.boundaryDate = boundaryDate;
            this.tripName = tripName;
            fillPaint = new Paint();
            // Применяем базовый цвет с прозрачностью для заливки
            fillPaint.setColor((baseColor & 0x00FFFFFF) | 0x33000000); // альфа 0x33
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setAntiAlias(true);

            strokePaint = new Paint();
            strokePaint.setColor(baseColor);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(4);
            strokePaint.setAntiAlias(true);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(boundaryDate);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(new TripBoundaryDrawable(fillPaint, strokePaint, tripName));
        }
    }

    /**
     * Кастомный Drawable для отрисовки круга с заливкой, обводкой и текстом (название путешествия).
     */
    private static class TripBoundaryDrawable extends android.graphics.drawable.ColorDrawable {
        private final Paint fillPaint;
        private final Paint strokePaint;
        private final String tripName;
        private final Paint textPaint;

        public TripBoundaryDrawable(Paint fillPaint, Paint strokePaint, String tripName) {
            super(fillPaint.getColor());
            this.fillPaint = fillPaint;
            this.strokePaint = strokePaint;
            this.tripName = tripName;
            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(24);
            textPaint.setAntiAlias(true);
        }

        @Override
        public void draw(Canvas canvas) {
            RectF rect = new RectF(getBounds());
            float diameter = Math.min(rect.width(), rect.height());
            float cx = rect.left + rect.width() / 2;
            float cy = rect.top + rect.height() / 2;
            float radius = diameter / 2;
            // Рисуем заливку
            canvas.drawCircle(cx, cy, radius, fillPaint);
            // Рисуем обводку
            canvas.drawCircle(cx, cy, radius, strokePaint);
            // Рисуем текст, если имя задано и не слишком длинное
            if (tripName != null && !tripName.isEmpty()) {
                Paint.FontMetrics fm = textPaint.getFontMetrics();
                float textHeight = fm.descent - fm.ascent;
                float textY = cy + textHeight / 2 - fm.descent;
                String displayText = tripName.length() > 10 ? tripName.substring(0, 10) + "…" : tripName;
                canvas.drawText(displayText, cx, textY, textPaint);
            }
        }
    }
}
