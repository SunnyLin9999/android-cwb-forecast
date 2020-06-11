package com.sunnylinsj.forecastmvp.view;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationListener;
import com.sunnylinsj.forecastmvp.R;
import com.sunnylinsj.forecastmvp.model.WeatherInfo;
import com.sunnylinsj.forecastmvp.presenter.WeatherPresenter;

import java.io.IOException;
import java.util.List;

public class MvpActivity extends AppCompatActivity implements WeatherView, LocationListener {

    private static String TAG = MvpActivity.class.getName();

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT = 1;

    private WeatherPresenter mPresenter;

    private EditText mSearchEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LocationManager mLocationManager;
    private double mLongitude;
    private double mLatitude;
    private Location mLocation;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

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

        mPresenter = new WeatherPresenter(getApplicationContext(), this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestLocationPermission();
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION },
                MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasLocationPermission() {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_ACCESS_COARSE_LOCATION_REQUEST_RESULT: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocation = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
                    onLocationChanged(mLocation);
                    showForecast();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    /**
     * Available city values :
     * 宜蘭縣,花蓮縣,臺東縣,澎湖縣,金門縣,連江縣,臺北市,新北市,
     * 桃園市,臺中市,臺南市,高雄市,基隆市,新竹縣,新竹市,苗栗縣,
     * 彰化縣,南投縣,雲林縣,嘉義縣,嘉義市,屏東縣
     */
    private void showForecast() {
        String cityname = null;
        String search = mSearchEditText.getText().toString();

        if (search.isEmpty()) {
            if (mLocation != null) {
                cityname = getCurrentCity(mLocation);
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

        mPresenter.onSearchClicked(cityname);
    }

    /**
     * Implements WeatherView
     */
    @Override
    public void setItems(List<WeatherInfo> WeatherInfos) {
        mAdapter = new WeatherAdapter(WeatherInfos);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onSearchClicked(View view) {
        showForecast();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            Log.d(TAG, "onLocationChanged Longitude: " + mLatitude);
            Log.d(TAG, "onLocationChanged Latitude: " + mLongitude);
        }
    }

    //get current city, country etc
    private String getCurrentCity(Location location) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            Log.d(TAG, "getCurrrntCity country: " + country + ", city: " + city + ", state: " + state);

            return state;
        }
        return null;
    }
}
