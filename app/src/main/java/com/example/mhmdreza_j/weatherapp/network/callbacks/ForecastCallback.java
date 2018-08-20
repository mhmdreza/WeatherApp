package com.example.mhmdreza_j.weatherapp.network.callbacks;

import android.widget.Toast;

import com.example.mhmdreza_j.weatherapp.models.forecast.ForecastModel;
import com.example.mhmdreza_j.weatherapp.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastCallback implements Callback<ForecastModel> {
    private MainActivity mainActivity;

    public ForecastCallback(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onResponse(Call<ForecastModel> call, Response<ForecastModel> response) {
        if (response.isSuccessful()) {
            mainActivity.setForecastModel(response.body());
            Toast.makeText(mainActivity, "FORECAST", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mainActivity
                    , "forecast:HTTP request fail with code:" + response.code()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ForecastModel> call, Throwable t) {
        Toast.makeText(mainActivity, "F Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
