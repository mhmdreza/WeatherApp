package com.example.mhmdreza_j.weatherapp.models;


import java.util.ArrayList;

import ir.huri.jcal.JalaliCalendar;

public class DailyWeather {
    private final int ID;
    private final ArrayList<Integer> minTemp;
    private final ArrayList<Integer> maxTemp;
    private final String state;
    private final JalaliCalendar date;

    public int getID() {
        return ID;
    }
    public String getState() {
        return state;
    }
    public ArrayList<Integer> getMinTempList() {
        return minTemp;
    }

    public ArrayList<Integer> getMaxTempList() {
        return maxTemp;
    }

    public JalaliCalendar getDate() {
        return date;
    }

    public int getMinTemp() {
        int min = minTemp.get(0);
        for (int a:minTemp) {
            if (a < min){
                min = a;
            }
        }
        return min;
    }

    public int getMaxTemp() {
        int max = maxTemp.get(0);
        for (int a:maxTemp) {
            if (max < a){
                max = a;
            }
        }
        return max;
    }

    public DailyWeather(int id, JalaliCalendar date, ArrayList<Integer> minTemp, ArrayList<Integer> maxTemp, String state) {
        ID = id;
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.state = state;
    }
}
