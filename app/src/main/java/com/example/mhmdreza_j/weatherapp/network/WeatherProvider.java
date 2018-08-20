package com.example.mhmdreza_j.weatherapp.network;

import com.example.mhmdreza_j.weatherapp.utils.ClientConfigs;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherProvider {

    private WeatherService service;

    public WeatherProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ClientConfigs.BASE_URL)
                .client(new OkHttpClient())
                .build();
        service = retrofit.create(WeatherService.class);
    }

    public WeatherService getService() {
        return service;
    }
}
