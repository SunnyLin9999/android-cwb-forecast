package com.sunnylinsj.forecastmvp.model.data;

public class Records {
    private String datasetDescription;
    private Location[] location;

    public String getDatasetDescription() {
        return this.datasetDescription;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public Location[] getLocation() {
        return this.location;
    }

    public void setLocation(Location[] location) {
        this.location = location;
    }
}
