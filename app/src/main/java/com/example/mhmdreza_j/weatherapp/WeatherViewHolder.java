package com.example.mhmdreza_j.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.huri.jcal.JalaliCalendar;

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    private TextView dayNameTextView;
    private TextView dateTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private ImageView stateImageView;
    public WeatherViewHolder(View itemView, final WeatherListener listener){
        super(itemView);
        dayNameTextView = itemView.findViewById(R.id.textview_day_name);
        dateTextView = itemView.findViewById(R.id.textview_date);
        minTempTextView = itemView.findViewById(R.id.textview_min_temp);
        maxTempTextView = itemView.findViewById(R.id.textview_max_temp);
        stateImageView = itemView.findViewById(R.id.imageview_weather_state);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, getAdapterPosition());
            }
        });
    }

    public void setTextViews(Weather weather) {
        int resID;
        switch (weather.getState()){
            case "01d":
            case "01n":
                resID = R.drawable.d01;
                break;
            case "02d":
            case "02n":
                resID = R.drawable.d02;
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
            case "10n":
                resID = R.drawable.d10;
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
        stateImageView.setImageResource(resID);
        String date = weather.getDate();
        JalaliCalendar calendar = new JalaliCalendar(Integer.parseInt(date.substring(0,4)),
                Integer.parseInt(date.substring(5,7)), Integer.parseInt(date.substring(8,10)));
        dayNameTextView.setText(calendar.getDayOfWeekString());
        dateTextView.setText(calendar.toString());
        maxTempTextView.setText(String.valueOf(weather.getMaxTemp()));
        minTempTextView.setText(String.valueOf(weather.getMinTemp()));
    }

//    @Override
//    public void onClick(View view) {
//        ArrayList<View> views = itemView.getRootView().getTouchables();
//        for (View v: views) {
//            if(v.getBackground() == itemView.getResources().getDrawable(R.drawable.background)
//                    || v.getBackground() == itemView.getResources().getDrawable(R.drawable.background_clicked)){
//                v.setBackgroundResource(R.drawable.background_clicked);
//                v.bringToFront();
//            }
//        }
//        int pos = getAdapterPosition();
//        if(pos != RecyclerView.NO_POSITION){
//        }
//    }
}
