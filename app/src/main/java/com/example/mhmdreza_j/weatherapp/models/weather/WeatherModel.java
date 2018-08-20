package com.example.mhmdreza_j.weatherapp.models.weather;

import java.util.ArrayList;

public class WeatherModel {
    private ArrayList<Weather> weather;
    private Main main;
    private Wind wind;
    private Sys sys;
    private String name;


    public String getIcon(){
        return weather.get(0).getIcon();
    }

    public int getTemp() {
        return (int) main.getTemp();
    }

    public int getHumidity() {
        return (int) main.getHumidity();
    }
    public int getTemp_max() {
        return (int) main.getTemp_max();
    }

    public int getTemp_min() {
        return (int) main.getTemp_min();
    }

    public int getSpeed() {
        return (int) wind.getSpeed();
    }

    public long getSunrise() {
        return sys.getSunrise();
    }

    public long getSunset() {
        return sys.getSunset();
    }

    public String getName() {
        return name;
    }

}
