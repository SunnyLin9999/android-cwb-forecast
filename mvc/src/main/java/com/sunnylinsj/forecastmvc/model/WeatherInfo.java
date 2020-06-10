package com.sunnylinsj.forecastmvc.model;

public class WeatherInfo {
    private final String TAG = WeatherInfo.class.getName();

    private String startTime = null;
    private String endTime = null;
    private String wx = null;
    private String pop = null;
    private String ci = null;
    private String mint = null;
    private String maxt = null;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getWx() {
        return wx;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getPop() {
        return pop;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getCi() {
        return ci;
    }

    public void setMint(String mint) {
        this.mint = mint;
    }

    public String getMint() {
        return mint;
    }

    public void setMaxt(String maxt) {
        this.maxt = maxt;
    }

    public String getMaxt() {
        return maxt;
    }
}