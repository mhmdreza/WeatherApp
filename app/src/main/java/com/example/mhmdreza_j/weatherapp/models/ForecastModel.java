package com.example.mhmdreza_j.weatherapp.models;

import java.util.ArrayList;

public class ForecastModel {
    private ArrayList<WeatherStatus> list;

    private class WeatherStatus {
        private Main main;
        private ArrayList<Weather> weather;
        private String dt_txt;

        private class Main {

            private double temp_min;
            private double temp_max;

        }
        private class Weather {
            private String icon;
        }

    }
    public String getDate(int index) {
        return list.get(index).dt_txt;
    }

    public int getTemp_min(int index) {
        return (int) list.get(index).main.temp_min;
    }

    public int getTemp_max(int index) {
        return (int) list.get(index).main.temp_min;
    }

    public String getIcon(int index){
        return list.get(index).weather.get(0).icon;
    }

    public int getLength(){
        return list.size();
    }
}
