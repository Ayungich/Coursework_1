package com.ayungi.travelapp.model.data.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponseDto {

    @SerializedName("name")
    private String name;  // Название города

    @SerializedName("main")
    private MainData main;

    @SerializedName("weather")
    private List<WeatherInfo> weather;

    // Геттеры и сеттеры для всех полей

    public String getName() {
        return name;
    }

    public MainData getMain() {
        return main;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    // Вложенные классы для структуры main и weather

    public static class MainData {
        @SerializedName("temp")
        private double temp;

        @SerializedName("pressure")
        private int pressure;

        @SerializedName("humidity")
        private int humidity;

        // Геттеры
        public double getTemp() { return temp; }
        public int getPressure() { return pressure; }
        public int getHumidity() { return humidity; }
    }

    public static class WeatherInfo {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        // Геттеры
        public String getMain() { return main; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }
}
