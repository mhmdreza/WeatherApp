package com.example.mhmdreza_j.weatherapp.network;

import com.example.mhmdreza_j.weatherapp.models.forecast.ForecastModel;
import com.example.mhmdreza_j.weatherapp.models.weather.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherModel> getCurrentWeather(@Query("q") String city
            , @Query("APPID") String appID
            , @Query("units") String units);
    @GET("forecast")
    Call<ForecastModel> getForecastWeather(@Query("q") String city
            , @Query("APPID") String appID
            , @Query("units") String units);
}
