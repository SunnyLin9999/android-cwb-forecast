package com.sunnylinsj.forecastmvc.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.sunnylinsj.forecastmvc.R;
import com.sunnylinsj.forecastmvc.model.ApiService;
import com.sunnylinsj.forecastmvc.model.ApiServiceCallback;
import com.sunnylinsj.forecastmvc.model.WeatherInfo;
import com.sunnylinsj.forecastmvc.model.data.CwbResponse;
import com.sunnylinsj.forecastmvc.model.data.WeatherElement;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MvcActivity extends AppCompatActivity implements ApiServiceCallback, LocationListener {

    private static String TAG = MvcActivity.class.getName();

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT = 1;

    private EditText mSearchEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ApiService mModel;

    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private Location location;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);

        mSearchEditText = findViewById(R.id.search_edit);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "Search action " + v.getText().toString());
                    showForecast();
                    handled = true;
                }
                return handled;

            }
        });

        mRecyclerView = findViewById(R.id.forecast_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mModel = new ApiService(getApplicationContext());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestLocationPermission();
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasLocationPermission() {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
                showForecast();
            }
        }
    }

    /**
     * Available city values :
     * 宜蘭縣,花蓮縣,臺東縣,澎湖縣,金門縣,連江縣,臺北市,新北市,
     * 桃園市,臺中市,臺南市,高雄市,基隆市,新竹縣,新竹市,苗栗縣,
     * 彰化縣,南投縣,雲林縣,嘉義縣,嘉義市,屏東縣
     *
     * */
    private void showForecast() {
        String cityname = null;
        String search = mSearchEditText.getText().toString();

        if(search.isEmpty()) {
            if(location != null) {
                cityname = getCurrentCity(location);
            } else {
                cityname = "臺北市"; //default
            }
        } else {
            cityname = mSearchEditText.getText().toString();
        }

        if (cityname.subSequence(0, 1).equals("台")) {
            String sub = cityname.substring(1);
            cityname = "臺" + sub;
        }
        Log.d(TAG, "Search text: " + search);

        getSupportActionBar().setTitle(cityname);

        mModel.fetchData(cityname, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onForecastClicked(View view) {
        showForecast();
    }

    //Implement ApiServiceCallback
    @Override
    public void onSuccess(JSONObject response) {
        List<WeatherInfo> infos = new ArrayList<>();

        if (response != null) {
            Gson gson = new Gson();
            CwbResponse cwbResponse = gson.fromJson(response.toString(), CwbResponse.class);

            if (cwbResponse.getSuccess()) {
                String resourceId = cwbResponse.getResult().getResourceId();
                Log.d(TAG, "resourceId: " + resourceId);

                int locSize = cwbResponse.getRecords().getLocation().length;

                if(locSize > 0) {
                    WeatherElement[] elements =
                            cwbResponse.getRecords().getLocation()[0].getWeatherElement();
                    int timeSize = elements[0].getTime().length;

                    for (int i = 0; i < timeSize; i++) {
                        Log.d(TAG, "---------------------------------- i=" + i);
                        WeatherInfo info = new WeatherInfo();
                        info.setStartTime(elements[0].getTime()[i].getStartTime());
                        info.setEndTime(elements[0].getTime()[i].getEndTime());
                        for (WeatherElement element : elements) {
                            String elementName = element.getElementName();
                            String parameterName = element.getTime()[i].getParameter().getParameterName();
                            Log.d(TAG, "i=" + i + ", elementName: " + elementName + ", paramName: " + parameterName);
                            if (elementName.equals("Wx")) {
                                info.setWx(parameterName);
                            } else if (elementName.equals("PoP")) {
                                info.setPop(parameterName);
                            } else if (elementName.equals("MinT")) {
                                info.setMint(parameterName);
                            } else if (elementName.equals("CI")) {
                                info.setCi(parameterName);
                            } else if (elementName.equals("MaxT")) {
                                info.setMaxt(parameterName);
                            }
                        }
                        infos.add(info);
                        Log.e(TAG, "---------------------------------- infos s=" + infos.size());
                    }
                    for (WeatherInfo info : infos) {
                        Log.i(TAG, "info wx=" + info.getWx() + " pop=" + info.getPop() +
                                " mint=" + info.getMint() + " ci=" + info.getCi() + " maxt=" + info.getMaxt() +
                                ", startTime=" + info.getStartTime() +
                                ", endTime=" + info.getEndTime());
                    }
                }
            }
        }
        mAdapter = new WeatherAdapter(infos);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onError(String err) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d(TAG, "onLocationChanged Longitude: " + latitude);
            Log.d(TAG, "onLocationChanged Latitude: " + longitude);
        }
    }

    //get current city, country etc
    private String getCurrentCity(Location location) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(addresses != null) {
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            Log.d(TAG, "getCurrrntCity country: " + country + ", city: " + city + ", state: " + state);

            return state;
        }
        return null;
    }
}
