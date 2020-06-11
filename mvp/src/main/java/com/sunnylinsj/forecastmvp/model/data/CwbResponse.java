package com.sunnylinsj.forecastmvp.model.data;

public class CwbResponse {
    private Result result;
    private Records records;
    private boolean success;

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Records getRecords() {
        return this.records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
