package com.sunnylinsj.forecastmvp.model.data;

public class WeatherElement {
    private Time[] time;
    private String elementName;

    public Time[] getTime() {
        return this.time;
    }

    public void setTime(Time[] time) {
        this.time = time;
    }

    public String getElementName() {
        return this.elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
