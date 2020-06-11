package com.sunnylinsj.forecastmvp.model.data;

public class Time {
    private TimeParameter parameter;
    private String startTime;
    private String endTime;

    public TimeParameter getParameter() {
        return this.parameter;
    }

    public void setParameter(TimeParameter parameter) {
        this.parameter = parameter;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
