package com.sunnylinsj.forecastmvp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sunnylinsj.forecastmvp.model.ApiService;
import com.sunnylinsj.forecastmvp.model.ApiServiceCallback;
import com.sunnylinsj.forecastmvp.model.WeatherInfo;
import com.sunnylinsj.forecastmvp.model.data.CwbResponse;
import com.sunnylinsj.forecastmvp.model.data.WeatherElement;
import com.sunnylinsj.forecastmvp.view.WeatherView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherPresenter extends Presenter implements ApiServiceCallback {

    private static String TAG = WeatherPresenter.class.getName();

    private WeatherView mMainView;
    private ApiService mModel;

    public WeatherPresenter(Context context, WeatherView view) {
        this.mMainView = view;
        this.mModel = new ApiService(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onSearchClicked(String cityname) {
        mModel.fetchData(cityname, this);
    }

    /**
     * Implements ApiServiceCallback
     */
    @Override
    public void onSuccess(JSONObject response) {
        List<WeatherInfo> infos = new ArrayList<>();

        if (response != null) {
            Gson gson = new Gson();
            CwbResponse cwbResponse = gson.fromJson(response.toString(), CwbResponse.class);

            if (cwbResponse.getSuccess()) {
                String resourceId = cwbResponse.getResult().getResourceId();
                Log.d(TAG, "resourceId: " + resourceId);

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

        mMainView.setItems(infos);
    }

    @Override
    public void onError(String err) {

    }

}
