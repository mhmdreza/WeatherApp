package com.example.mhmdreza_j.weatherapp.utils;

import com.example.mhmdreza_j.weatherapp.R;

public class IconResourceIdProvider {

    public static int getResID(String state, boolean ignoreNight) {
        int resID;
        switch (state){
            case "01d":
                resID = R.drawable.d01;
                break;
            case "02d":
                resID = R.drawable.d02;
                break;
            case "01n":
                if (ignoreNight){
                    resID = R.drawable.d01;
                }else
                    resID = R.drawable.n01;
                break;
            case "02n":
                if (ignoreNight){
                    resID = R.drawable.d02;
                }else
                    resID = R.drawable.n02;
                break;
            case "03d":
            case "03n":
                resID = R.drawable.d03;
                break;
            case "04d":
            case "04n":
                resID = R.drawable.d04;
                break;
            case "09d":
            case "09n":
                resID = R.drawable.d09;
                break;
            case "10d":
                resID = R.drawable.d10;
                break;
            case "10n":
                if (ignoreNight){
                    resID = R.drawable.d10;
                }else
                    resID = R.drawable.n10;
                break;
            case "11d":
            case "11n":
                resID = R.drawable.d11;
                break;
            case "13d":
            case "13n":
                resID = R.drawable.d13;
                break;
            case "50d":
            case "50n":
                resID = R.drawable.d50;
                break;
            default:
                resID = R.drawable.d02;
        }
        return resID;
    }

}
