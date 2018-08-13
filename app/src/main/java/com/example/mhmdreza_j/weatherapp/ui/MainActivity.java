package com.example.mhmdreza_j.weatherapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhmdreza_j.weatherapp.R;
import com.example.mhmdreza_j.weatherapp.models.DailyWeather;
import com.example.mhmdreza_j.weatherapp.models.ForecastModel;
import com.example.mhmdreza_j.weatherapp.models.WeatherModel;
import com.example.mhmdreza_j.weatherapp.network.WeatherProvider;
import com.example.mhmdreza_j.weatherapp.network.WeatherService;
import com.example.mhmdreza_j.weatherapp.utils.WeatherAdapter;
import com.example.mhmdreza_j.weatherapp.utils.WeatherListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ir.huri.jcal.JalaliCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WeatherListener {

    public static final String MAX_TEMP = "maxTemp";
    public static final String MIN_TEMP = "minTemp";
    public static final String DATE = "date";
    TextView textView_city;
    TextView textView_date;
    TextView textView_min_temp;
    TextView textView_max_temp;
    TextView textView_curr_temp;
    TextView textView_humidity;
    TextView textView_wind_speed;
    TextView textView_unit;
    ImageView imageView_background;
    ImageView imageView_wind;
    ImageView imageView_humidity;
    ImageView imageView_weather_state;
    RecyclerView recyclerView;
    String city = null;
    WeatherAdapter adapter;
    WeatherModel weatherModel;
    ForecastModel forecastModel = null;
    JalaliCalendar jalaliCalendar;
    ArrayList<DailyWeather> weathers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        initializeViews();
    }

    private void getPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void initializeViews() {
        city = "Tokyo";
        imageView_background = findViewById(R.id.imageview_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView_background.setBackground(getResources().getDrawable(R.drawable.weather_background_light));
        }else {
            imageView_background.setImageResource(R.drawable.weather_background_light);
        }
        textView_city = findViewById(R.id.textview_city);
        View weather_stat_layout = findViewById(R.id.weather_stat_layout);
        weather_stat_layout.bringToFront();
        View weather_state_layout = findViewById(R.id.weather_state_layout);
        weather_state_layout.bringToFront();
        textView_city.bringToFront();
        textView_unit = findViewById(R.id.textview_unit);
        textView_unit.bringToFront();
        imageView_weather_state = findViewById(R.id.imageview_weather_state);
        imageView_weather_state.bringToFront();
        textView_date = findViewById(R.id.textview_date);
        textView_date.bringToFront();
        imageView_humidity = findViewById(R.id.imageview_humidity);
        imageView_humidity.bringToFront();
        imageView_wind = findViewById(R.id.imageview_wind);
        imageView_wind.bringToFront();
        textView_min_temp = findViewById(R.id.textview_min);
        textView_min_temp.bringToFront();
        textView_max_temp = findViewById(R.id.textview_max);
        textView_max_temp.bringToFront();
        textView_curr_temp = findViewById(R.id.textview_current_temp);
        textView_curr_temp.bringToFront();
        textView_wind_speed = findViewById(R.id.textview_wind_speed);
        textView_wind_speed.bringToFront();
        recyclerView = findViewById(R.id.recyclerview);
        textView_humidity = findViewById(R.id.textview_humidity);
        textView_humidity.bringToFront();
    }

    public void showCity(View view){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Access Denied!", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null){
                Toast.makeText(this, "Location not Found!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "gps", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Network", Toast.LENGTH_SHORT).show();
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0){
                city = addresses.get(0).getLocality();
                textView_city.setText(city);
        }
    }

    public void getWeather(View view){
        WeatherProvider provider = new WeatherProvider();
        WeatherService service = provider.getService();
        Call<WeatherModel> weatherModelCall = service.getCurrentWeather(city
                , "aed3f3107de7197e5d7e65d01bbfb2e0"
                , "metric");
        weatherModelCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    weatherModel = response.body();
                    Toast.makeText(MainActivity.this, "weather Success!!!", Toast.LENGTH_SHORT).show();
                    setTextViews();
                }
                else {
                    Toast.makeText(MainActivity.this
                            , "Weather:HTTP request fail with code:" + response.code()
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "WeatherError:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Call<ForecastModel> forecastModelCall = service.getForecastWeather(city
                , "aed3f3107de7197e5d7e65d01bbfb2e0"
                , "metric");
        forecastModelCall.enqueue(new Callback<ForecastModel>() {
            @Override
            public void onResponse(Call<ForecastModel> call, Response<ForecastModel> response) {
                if (response.isSuccessful()) {
                    forecastModel = response.body();
                    Toast.makeText(MainActivity.this, "FORECAST", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this
                            , "forecast:HTTP request fail with code:" + response.code()
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "F Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTextViews(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String georgianDate = formatter.format(date);
        jalaliCalendar = new JalaliCalendar(new GregorianCalendar(
                Integer.parseInt(georgianDate.substring(0, 4)), Integer.parseInt(georgianDate.substring(5, 7)) - 1, Integer.parseInt(georgianDate.substring(8))));
        textView_city.setText(weatherModel.getName());
        textView_curr_temp.setText(String.valueOf(weatherModel.getTemp()));
        textView_min_temp.setText(String.valueOf(weatherModel.getTemp_min()));
        textView_max_temp.setText(String.valueOf(weatherModel.getTemp_max()));
        textView_humidity.setText(String.format(getString(R.string.humidity_format), weatherModel.getHumidity()));
        textView_wind_speed.setText(String.format(getString(R.string.wind_speed_format), weatherModel.getSpeed()));
        Long sunrise = weatherModel.getSunrise();
        Long sunset = weatherModel.getSunset();
        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
        if (time > sunrise && time < sunset) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView_background.setBackground(getResources().getDrawable(R.drawable.weather_background_light));
                textView_city.setTextColor(getResources().getColor(R.color.black));
                textView_date.setTextColor(getResources().getColor(R.color.black));
                textView_min_temp.setTextColor(getResources().getColor(R.color.black));
                textView_max_temp.setTextColor(getResources().getColor(R.color.black));
                imageView_wind.setBackground(getResources().getDrawable(R.drawable.ic_ic_wind_speed_d));
                imageView_humidity.setBackground(getResources().getDrawable(R.drawable.ic_humidity_d));
                textView_curr_temp.setTextColor(getResources().getColor(R.color.black));
                textView_humidity.setTextColor(getResources().getColor(R.color.black));
                textView_wind_speed.setTextColor(getResources().getColor(R.color.black));
                textView_unit.setTextColor(getResources().getColor(R.color.black));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView_background.setBackground(getResources().getDrawable(R.drawable.weather_background_dark));
                textView_date.setTextColor(getResources().getColor(R.color.white));
                imageView_wind.setBackground(getResources().getDrawable(R.drawable.ic_ic_wind_speed_n));
                imageView_humidity.setBackground(getResources().getDrawable(R.drawable.ic_humidity_n));
                textView_city.setTextColor(getResources().getColor(R.color.white));
                textView_min_temp.setTextColor(getResources().getColor(R.color.white));
                textView_max_temp.setTextColor(getResources().getColor(R.color.white));
                textView_curr_temp.setTextColor(getResources().getColor(R.color.white));
                textView_humidity.setTextColor(getResources().getColor(R.color.white));
                textView_wind_speed.setTextColor(getResources().getColor(R.color.white));
                textView_unit.setTextColor(getResources().getColor(R.color.white));
            }
        }
        textView_date.setText(jalaliCalendar.toString());
        String state = weatherModel.getIcon();
        int resID = getResID(state);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView_weather_state.setBackground(getResources().getDrawable(resID));
        }
    }


    private int getResID(String state) {
        int resID;
        switch (state){
            case "01d":
                resID = R.drawable.d01;
                break;
            case "02d":
                resID = R.drawable.d02;
                break;
            case "01n":
                resID = R.drawable.n01;
                break;
            case "02n":
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

    public void forecastWeather(View view){
        if(forecastModel == null){
            return;
        }
        weathers = new ArrayList<>();
        int index = 0;
        String currDate = null;
        int id = 0;
        String preDate;
        ArrayList<Integer> maxTemp = new ArrayList<>();
        ArrayList<Integer> minTemp = new ArrayList<>();
        String state = null;
        int min;
        int max;
        try{
            JalaliCalendar calendar = jalaliCalendar;
            DailyWeather weather;
            while (index < forecastModel.getLength()){
                min = forecastModel.getTemp_min(index);
                max = forecastModel.getTemp_max(index);
                preDate = currDate;
                currDate = forecastModel.getDate(index).substring(0, 10);
                String time = forecastModel.getDate(index).substring(11);
                if (time.equals("15:00:00") || (state == null && time.equals("21:00:00"))) {
                    state =forecastModel.getIcon(index);
                }
                if (!(preDate == null) && !preDate.equals(currDate)){
                    weather = new DailyWeather(id, calendar, minTemp, maxTemp, state);
                    id++;
                    weathers.add(weather);
                    maxTemp = new ArrayList<>();
                    minTemp = new ArrayList<>();
                    calendar = calendar.getTomorrow();
                }
                maxTemp.add(max);
                minTemp.add(min);
                index++;
            }
            weather = new DailyWeather(id, calendar, minTemp, maxTemp, state);
            weathers.add(weather);
        }
        catch (Exception e){
            Toast.makeText(this, "something wrong",Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new WeatherAdapter(weathers, this);
        recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        recyclerView.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.background_clicked));
                    }
                }

            }
        }, 300);
    }

    public void showChart(DailyWeather weather) {
        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        intent.putIntegerArrayListExtra(MAX_TEMP, weather.getMaxTempList());
        intent.putIntegerArrayListExtra(MIN_TEMP, weather.getMinTempList());
        intent.putExtra(DATE, weather.getDate().toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG).show();
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                recyclerView.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.background));
            }
        }
        View item = recyclerView.getChildAt(0);
        for (int i = 0; i < count; i++) {
            TextView textViewId = recyclerView.getChildAt(i).findViewById(R.id.textview_id);
            int id = Integer.parseInt(textViewId.getText().toString());
            if (id == position){
                item = recyclerView.getChildAt(i);
                break;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setBackground(getResources().getDrawable(R.drawable.background_clicked));
        }
        showChart(weathers.get(position));
    }
}