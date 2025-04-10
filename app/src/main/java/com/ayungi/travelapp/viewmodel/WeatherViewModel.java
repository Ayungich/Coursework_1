package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.WeatherResponseDto;
import com.ayungi.travelapp.model.repository.WeatherRepository;
import com.ayungi.travelapp.utils.Resource;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository;

    public WeatherViewModel() {
        repository = WeatherRepository.getInstance();
    }

    // Получаем LiveData с WeatherResponse
    public LiveData<Resource<WeatherResponseDto>> getWeather(double lat, double lon) {
        return repository.getWeatherByLocation(lat, lon);
    }
}
