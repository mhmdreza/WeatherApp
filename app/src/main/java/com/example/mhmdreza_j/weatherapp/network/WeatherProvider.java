package com.example.mhmdreza_j.weatherapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherProvider {

    private WeatherService service;

    public WeatherProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .client(new OkHttpClient())
                .build();
        service = retrofit.create(WeatherService.class);
    }

    public WeatherService getService() {
        return service;
    }
}
