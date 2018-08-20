package com.example.mhmdreza_j.weatherapp.network.callbacks;

import com.example.mhmdreza_j.weatherapp.models.weather.WeatherModel;
import com.example.mhmdreza_j.weatherapp.network.listeners.WeatherWSListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherCallBack implements Callback<WeatherModel> {
    private WeatherWSListener listener;

    public WeatherCallBack(WeatherWSListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
        if (response.isSuccessful()) {
            listener.setData(response.body());
        }
        else {
//            Toast.makeText(mainActivity
//                    , "Weather:HTTP request fail with code:" + response.code()
//                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<WeatherModel> call, Throwable t) {

    }
}
