package com.example.mhmdreza_j.weatherapp;


import java.util.ArrayList;

public class Weather {
    private final String date;
    private final ArrayList<Integer> minTemp;
    private final ArrayList<Integer> maxTemp;
    private final String state;

    public String getState() {
        return state;
    }

    public ArrayList<Integer> getMinTempList() {
        return minTemp;
    }

    public ArrayList<Integer> getMaxTempList() {
        return maxTemp;
    }

    public String getDate() {
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

    public Weather(String date, ArrayList<Integer> minTemp, ArrayList<Integer> maxTemp, String state) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.state = state;
    }
}
