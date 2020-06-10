package com.sunnylinsj.forecastmvc.model.data;

public class Location {
    private String locationName;
    private WeatherElement[] weatherElement;

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public WeatherElement[] getWeatherElement() {
        return this.weatherElement;
    }

    public void setWeatherElement(WeatherElement[] weatherElement) {
        this.weatherElement = weatherElement;
    }
}
