package com.sunnylinsj.forecastmvp.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiService {

    private static String TAG = ApiService.class.getName();

    private String URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001";

    private String API_KEY = "CWB-0E098D83-B544-4C05-906E-5D66C330251D";

    private Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public void fetchData(String cityname, final ApiServiceCallback callback) {
        String url = URL + "?Authorization=" + API_KEY + "&format=JSON&locationName=" + cityname;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                        callback.onError(error.getMessage());
                    }
                });
        queue.add(request);
    }
}
