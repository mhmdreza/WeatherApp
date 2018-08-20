package com.example.mhmdreza_j.weatherapp.models.forecast;

import java.util.ArrayList;

public class ForecastModel {
    private ArrayList<WeatherStatus> list;

    public String getDate(int index) {
        return list.get(index).getDt_txt();
    }

    public int getTemp_min(int index) {
        return (int) list.get(index).getMain().getTemp_min();
    }

    public int getTemp_max(int index) {
        return (int) list.get(index).getMain().getTemp_max();
    }

    public String getIcon(int index){
        return list.get(index).getWeather().get(0).getIcon();
    }

    public int getLength(){
        return list.size();
    }
}
