package com.example.mhmdreza_j.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ir.huri.jcal.JalaliCalendar;

public class MainActivity extends AppCompatActivity implements WeatherListener {

    public static final String MAX_TEMP = "maxTemp";
    public static final String MIN_TEMP = "minTemp";
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
    JSONObject data = null;
    JSONObject forecastData = null;
    JalaliCalendar jalaliCalendar;
    ArrayList<Weather> weathers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        city = "Tijuana";
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

    @SuppressLint("StaticFieldLeak")
    public void getWeather(View view){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                data = getData("weather");
                forecastData = getData("forecast");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setTextViews();
            }
        }.execute();
    }
    private JSONObject getData(String type){
        JSONObject result;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/" + type + "?q=" + city + "&APPID=aed3f3107de7197e5d7e65d01bbfb2e0&units=metric");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();
            result = new JSONObject(json.toString());
            if(result.getInt("cod") != 200) {
                System.out.println("Cancelled");
                return null;
            }
        } catch (Exception e) {

            System.out.println("Exception "+ e.getMessage());
            return null;
        }

        return result;
    }
    private void setTextViews(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String georgianDate = formatter.format(date);
        jalaliCalendar = new JalaliCalendar(new GregorianCalendar(
                Integer.parseInt(georgianDate.substring(0, 4)), Integer.parseInt(georgianDate.substring(5, 7)) - 1, Integer.parseInt(georgianDate.substring(8))));
        textView_date.setText(jalaliCalendar.toString());
        try {
            String state = data.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");
            int resID = getResID(state);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView_weather_state.setBackground(getResources().getDrawable(resID));
            }
            JSONObject main = data.getJSONObject("main");
            textView_city.setText(data.getString("name"));
            textView_curr_temp.setText(main.getString("temp").substring(0, 2));
            textView_min_temp.setText(main.getString("temp_min"));
            textView_max_temp.setText(main.getString("temp_max"));
            textView_humidity.setText(String.format(getString(R.string.humidity_format), main.getString("humidity")));
            JSONObject wind = data.getJSONObject("wind");
            textView_wind_speed.setText(String.format(getString(R.string.wind_speed_format), wind.getInt("speed")));
            JSONObject sys = data.getJSONObject("sys");
            Long sunrise = Long.parseLong(sys.getString("sunrise"));
            Long sunset = Long.parseLong(sys.getString("sunset"));
            Long time = Calendar.getInstance().getTimeInMillis() / 1000;
            if(time > sunrise && time < sunset){
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
            }
            else {
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
                Toast.makeText(this, "night", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

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

    private void getPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void forecastWeather(View view){
        weathers = new ArrayList<>();
        int index = 0;
        String currDate = null;
        String preDate;
        ArrayList<Integer> maxTemp = new ArrayList<>();
        ArrayList<Integer> minTemp = new ArrayList<>();
        String state = null;
        int min;
        int max;
        try{
            JalaliCalendar calendar = jalaliCalendar;
            JSONArray list = forecastData.getJSONArray("list");
            Weather weather;
            while (index < list.length()){
                JSONObject timeData = list.getJSONObject(index);
                JSONObject main = timeData.getJSONObject("main");
                min = main.getInt("temp_min");
                max = main.getInt("temp_max");
                preDate = currDate;
                currDate = timeData.getString("dt_txt").substring(0, 10);
                String time = timeData.getString("dt_txt").substring(11);
                if (time.equals("15:00:00") || (state == null && time.equals("21:00:00"))) {
                    state = timeData.getJSONArray("weather")
                            .getJSONObject(0).getString("icon");
                }
                if (!(preDate == null) && !preDate.equals(currDate)){
                    weather = new Weather(calendar.toString(), minTemp, maxTemp, state);
                    weathers.add(weather);
                    maxTemp = new ArrayList<>();
                    minTemp = new ArrayList<>();
                    calendar = calendar.getTomorrow();
                }
                maxTemp.add(max);
                minTemp.add(min);
                index++;
            }
            weather = new Weather(calendar.toString(), minTemp, maxTemp, state);
            weathers.add(weather);
        }
        catch (Exception e){
            Toast.makeText(this, "something wrong",Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.recyclerview);
        WeatherAdapter adapter = new WeatherAdapter(weathers, this);
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
        }, 1000);
    }

    public void showChart(Weather weather) {
        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        intent.putIntegerArrayListExtra(MAX_TEMP, weather.getMaxTempList());
        intent.putIntegerArrayListExtra(MIN_TEMP, weather.getMinTempList());
        if (weather.getMaxTempList() == null || weather.getMinTempList() == null) {
            Toast.makeText(this, "kha",Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View view, int position) {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                recyclerView.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.background));
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.getChildAt(position).setBackground(getResources().getDrawable(R.drawable.background_clicked));
        }
        showChart(weathers.get(position));
    }
}