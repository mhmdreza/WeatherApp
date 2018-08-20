package com.example.mhmdreza_j.weatherapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhmdreza_j.weatherapp.R;
import com.example.mhmdreza_j.weatherapp.models.DailyWeather;
import com.example.mhmdreza_j.weatherapp.ui.MainActivity;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private ArrayList<DailyWeather> list;
    private WeatherListener listener;
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public WeatherAdapter(ArrayList<DailyWeather> list, WeatherListener listener) {
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
        holder.setTextViews(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
