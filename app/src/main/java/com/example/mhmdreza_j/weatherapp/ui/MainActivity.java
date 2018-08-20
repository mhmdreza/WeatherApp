package com.example.mhmdreza_j.weatherapp.ui;

import android.Manifest;
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
import com.example.mhmdreza_j.weatherapp.models.forecast.ForecastModel;
import com.example.mhmdreza_j.weatherapp.models.weather.WeatherModel;
import com.example.mhmdreza_j.weatherapp.network.WeatherProvider;
import com.example.mhmdreza_j.weatherapp.network.WeatherService;
import com.example.mhmdreza_j.weatherapp.network.callbacks.ForecastCallback;
import com.example.mhmdreza_j.weatherapp.network.callbacks.WeatherCallBack;
import com.example.mhmdreza_j.weatherapp.network.listeners.WeatherWSListener;
import com.example.mhmdreza_j.weatherapp.utils.Constants;
import com.example.mhmdreza_j.weatherapp.utils.IconResourceIdProvider;
import com.example.mhmdreza_j.weatherapp.utils.WeatherAdapter;
import com.example.mhmdreza_j.weatherapp.utils.WeatherListener;
import com.example.mhmdreza_j.weatherapp.utils.WeatherViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ir.huri.jcal.JalaliCalendar;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements WeatherListener{

    public static final String MAX_TEMP = "maxTemp";
    public static final String MIN_TEMP = "minTemp";
    public static final String DATE = "date";
    private TextView cityTextView;
    private TextView dateTextView;
    private TextView minTempTextView;
    TextView maxTempTextView;
    TextView currentTempTextView;
    TextView humidityTextView;
    TextView windSpeedTextView;
    TextView unitTextView;
    ImageView backgroundImageView;
    ImageView windImageView;
    ImageView humidityImageView;
    ImageView weatherStateImageView;
    RecyclerView recyclerView;
    String city = null;
    WeatherAdapter weatherAdapter;
    WeatherModel weatherModel;
    ForecastModel forecastModel = null;
    JalaliCalendar jalaliCalendar;
    ArrayList<DailyWeather> weathers;
    View lastSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        initializeViews();
    }

    private void getPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    private void initializeViews() {
        backgroundImageView = findViewById(R.id.imageview_background);
        setBackground(backgroundImageView, R.drawable.weather_background_light);
        cityTextView = findViewById(R.id.textview_city);
        View weather_stat_layout = findViewById(R.id.weather_stat_layout);
        weather_stat_layout.bringToFront();
        View weather_state_layout = findViewById(R.id.weather_state_layout);
        weather_state_layout.bringToFront();
        cityTextView.bringToFront();
        unitTextView = findViewById(R.id.textview_unit);
        unitTextView.bringToFront();
        weatherStateImageView = findViewById(R.id.imageview_weather_state);
        weatherStateImageView.bringToFront();
        dateTextView = findViewById(R.id.textview_date);
        dateTextView.bringToFront();
        humidityImageView = findViewById(R.id.imageview_humidity);
        humidityImageView.bringToFront();
        windImageView = findViewById(R.id.imageview_wind);
        windImageView.bringToFront();
        minTempTextView = findViewById(R.id.textview_min);
        minTempTextView.bringToFront();
        maxTempTextView = findViewById(R.id.textview_max);
        maxTempTextView.bringToFront();
        currentTempTextView = findViewById(R.id.textview_current_temp);
        currentTempTextView.bringToFront();
        windSpeedTextView = findViewById(R.id.textview_wind_speed);
        windSpeedTextView.bringToFront();
        recyclerView = findViewById(R.id.recyclerview);
        humidityTextView = findViewById(R.id.textview_humidity);
        humidityTextView.bringToFront();
    }

    public void showCity(View view){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Access Denied!", Toast.LENGTH_SHORT).show();
                    return;
            }
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
                cityTextView.setText(city);
        }
    }

    public void getWeather(View view){
        WeatherProvider provider = new WeatherProvider();
        WeatherService service = provider.getService();
        Call<WeatherModel> weatherModelCall = service.getCurrentWeather(city
                , Constants.APPID
                , Constants.STANDARD_UNIT);
        weatherModelCall.enqueue(new WeatherCallBack(new WeatherWSListener() {
            @Override
            public void setData(WeatherModel weatherModel) {
                setWeatherModel(weatherModel);
                setTextViews();
            }
        })); // use Weather CallBack
        Call<ForecastModel> forecastModelCall = service.getForecastWeather(city
                , Constants.APPID
                , Constants.STANDARD_UNIT);
        forecastModelCall.enqueue(new ForecastCallback(this));
    }

    public void setWeatherModel(WeatherModel weatherModel) {
        this.weatherModel = weatherModel;
    }

    public void setForecastModel(ForecastModel forecastModel) {
        this.forecastModel = forecastModel;
    }

    public void setTextViews(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        jalaliCalendar = new JalaliCalendar(
                new GregorianCalendar(year, month, day));
        cityTextView.setText(weatherModel.getName());
        currentTempTextView.setText(String.valueOf(weatherModel.getTemp()));
        minTempTextView.setText(String.valueOf(weatherModel.getTemp_min()));
        maxTempTextView.setText(String.valueOf(weatherModel.getTemp_max()));
        humidityTextView.setText(String.format(getString(R.string.humidity_format), weatherModel.getHumidity()));
        windSpeedTextView.setText(String.format(getString(R.string.wind_speed_format), weatherModel.getSpeed()));
        Long sunrise = weatherModel.getSunrise();
        Long sunset = weatherModel.getSunset();
        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
        if (time > sunrise && time < sunset) {
            setBackground(backgroundImageView, R.drawable.weather_background_light);
            setBackground(windImageView, R.drawable.ic_ic_wind_speed_d);
            setBackground(humidityImageView, R.drawable.ic_humidity_d);
            cityTextView.setTextColor(getResources().getColor(R.color.black));
            dateTextView.setTextColor(getResources().getColor(R.color.black));
            minTempTextView.setTextColor(getResources().getColor(R.color.black));
            maxTempTextView.setTextColor(getResources().getColor(R.color.black));
            currentTempTextView.setTextColor(getResources().getColor(R.color.black));
            humidityTextView.setTextColor(getResources().getColor(R.color.black));
            windSpeedTextView.setTextColor(getResources().getColor(R.color.black));
            unitTextView.setTextColor(getResources().getColor(R.color.black));
        } else {
            setBackground(backgroundImageView, R.drawable.weather_background_dark);
            setBackground(windImageView, R.drawable.ic_ic_wind_speed_n);
            setBackground(humidityImageView, R.drawable.ic_humidity_n);
            cityTextView.setTextColor(getResources().getColor(R.color.white));
            dateTextView.setTextColor(getResources().getColor(R.color.white));
            minTempTextView.setTextColor(getResources().getColor(R.color.white));
            maxTempTextView.setTextColor(getResources().getColor(R.color.white));
            currentTempTextView.setTextColor(getResources().getColor(R.color.white));
            humidityTextView.setTextColor(getResources().getColor(R.color.white));
            windSpeedTextView.setTextColor(getResources().getColor(R.color.white));
            unitTextView.setTextColor(getResources().getColor(R.color.white));
        }
        dateTextView.setText(jalaliCalendar.toString());
        String state = weatherModel.getIcon();
        int resID = IconResourceIdProvider.getResID(state, false);
        setBackground(weatherStateImageView, resID);
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
        weatherAdapter = new WeatherAdapter(weathers, this);
        recyclerView.setAdapter(weatherAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    lastSelectedItem = recyclerView.getChildAt(0);
                    lastSelectedItem.setBackground(getResources().getDrawable(R.drawable.background_clicked));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            lastSelectedItem.setBackground(getResources().getDrawable(R.drawable.background));
        }
        View item = recyclerView.getChildAt(0);
        for (int i = 0; i < count; i++) {
            int id = ((WeatherViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).getViewPosition();
            if (id == position){
                item = recyclerView.getChildAt(i);
                break;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            item.setBackground(getResources().getDrawable(R.drawable.background_clicked));
        }
        showChart(weathers.get(position));
        lastSelectedItem = item;
    }

    private void setBackground(ImageView imageView, int resID){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(getResources().getDrawable(resID));
        }else {
            imageView.setImageResource(resID);
        }
    }
}