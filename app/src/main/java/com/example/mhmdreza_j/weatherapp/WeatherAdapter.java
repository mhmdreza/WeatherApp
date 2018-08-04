package com.example.mhmdreza_j.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private ArrayList<Weather> list;

    WeatherAdapter(ArrayList<Weather> list) {
        this.list = list;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_card, null);
        return new WeatherViewHolder(view);
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
