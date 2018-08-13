package com.example.mhmdreza_j.weatherapp.models;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class WeatherModel {
    public ArrayList<Weather> weather;
    private Main main;
    private Wind wind;
    private Sys sys;
    private String name;

    public class Weather {
        private String icon;

        public String getIcon() {
            return icon;
        }
    }
    private class Main {

        private float temp;
        private float humidity;
        private float temp_min;
        private float temp_max;

    }
    private class Wind {
        private float speed;

    }
    private class Sys {
        private long sunrise;
        private long sunset;

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

    }
    public String getIcon(){
        return weather.get(0).getIcon();
    }

    public int getTemp() {
        return (int) main.temp;
    }

    public int getHumidity() {
        return (int) main.humidity;
    }

    public int getTemp_max() {
        return (int) main.temp_max;
    }

    public int getTemp_min() {
        return (int) main.temp_min;
    }

    public int getSpeed() {
        return (int) wind.speed;
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
