package com.sunnylinsj.forecastmvc.model;

import org.json.JSONObject;

public interface ApiServiceCallback {
    void onSuccess(JSONObject response);
    void onError(String err);
}
