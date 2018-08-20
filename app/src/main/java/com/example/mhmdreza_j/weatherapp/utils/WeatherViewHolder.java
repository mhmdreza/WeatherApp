package com.example.mhmdreza_j.weatherapp.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mhmdreza_j.weatherapp.R;
import com.example.mhmdreza_j.weatherapp.models.DailyWeather;

import ir.huri.jcal.JalaliCalendar;

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout root;
    private TextView dayNameTextView;
    private TextView dateTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private TextView idTextView;
    private ImageView stateImageView;
    private int position;

     WeatherViewHolder(View itemView, final WeatherListener listener){
        super(itemView);
        dayNameTextView = itemView.findViewById(R.id.textview_day_name);
        dateTextView = itemView.findViewById(R.id.textview_date);
        idTextView = itemView.findViewById(R.id.textview_id);
        minTempTextView = itemView.findViewById(R.id.textview_min_temp);
        maxTempTextView = itemView.findViewById(R.id.textview_max_temp);
        stateImageView = itemView.findViewById(R.id.imageview_weather_state);
        root = itemView.findViewById(R.id.root);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, getAdapterPosition());
            }
        });
    }

    public void setTextViews(DailyWeather weather, int position) {
         this.position = position;
         int resID = IconResourceIdProvider.getResID(weather.getState(), true);
         stateImageView.setImageResource(resID);
         JalaliCalendar calendar = weather.getDate();
         dayNameTextView.setText(calendar.getDayOfWeekString());
         dateTextView.setText(calendar.toString());
         maxTempTextView.setText(String.valueOf(weather.getMaxTemp()));
         idTextView.setText(String.valueOf(weather.getID()));
         minTempTextView.setText(String.valueOf(weather.getMinTemp()));
    }

    public void setBackground(Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            root.setBackground(drawable);
        }
    }

    public int getViewPosition() {
        return position;
    }
}
