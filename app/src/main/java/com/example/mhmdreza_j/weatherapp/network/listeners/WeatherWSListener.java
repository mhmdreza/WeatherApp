package com.example.mhmdreza_j.weatherapp.network.listeners;

import com.example.mhmdreza_j.weatherapp.models.weather.WeatherModel;

public interface WeatherWSListener {
    void setData(WeatherModel weatherModel);
}
