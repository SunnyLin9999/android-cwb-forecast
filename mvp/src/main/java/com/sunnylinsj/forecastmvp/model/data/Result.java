package com.sunnylinsj.forecastmvp.model.data;

public class Result {
    private String resource_id;
    private Fields[] fields;

    public String getResourceId() {
        return this.resource_id;
    }

    public void setResourceId(String resource_id) {
        this.resource_id = resource_id;
    }

    public Fields[] getFields() {
        return this.fields;
    }

    public void setFields(Fields[] fields) {
        this.fields = fields;
    }
}
