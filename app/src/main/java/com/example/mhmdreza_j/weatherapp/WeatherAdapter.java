package com.example.mhmdreza_j.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private ArrayList<Weather> list;
    private WeatherListener listener;

    WeatherAdapter(ArrayList<Weather> list, WeatherListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_card, null);
        return new WeatherViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.setTextViews(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public Weather getItem(int position) {
//        return list.get(position);
//    }
}
