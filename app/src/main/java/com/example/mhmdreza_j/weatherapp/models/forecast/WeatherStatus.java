package com.example.mhmdreza_j.weatherapp.models.forecast;

import java.util.ArrayList;

class WeatherStatus {
    private Main main;
    private ArrayList<Weather> weather;
    private String dt_txt;

    public Main getMain() {
        return main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}
