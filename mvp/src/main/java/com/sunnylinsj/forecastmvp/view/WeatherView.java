package com.sunnylinsj.forecastmvp.view;

import com.sunnylinsj.forecastmvp.model.WeatherInfo;

import java.util.List;

public interface WeatherView {
    void setItems(List<WeatherInfo> currentWeatherResponse);
}
