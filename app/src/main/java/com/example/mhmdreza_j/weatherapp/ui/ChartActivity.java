package com.example.mhmdreza_j.weatherapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mhmdreza_j.weatherapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    public static final String MIN_TEMP = "minTemp";
    public static final String MAX_TEMP = "maxTemp";
    public static final String DATE = "date";
    public static final int NUM_OF_HOURS_PER_DAY = 24;
    public static final int PERIOD_OF_TIME = 3;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initializeViews();
        showChart();
    }

    private void showChart() {
        ArrayList<Integer> maxTemp = getIntent().getIntegerArrayListExtra(MAX_TEMP);
        ArrayList<Integer> minTemp = getIntent().getIntegerArrayListExtra(MIN_TEMP);
        List<Entry> entries_min = new ArrayList<>();
        List<Entry> entries_max = new ArrayList<>();
        List<ILineDataSet> dataSets = new ArrayList<>();
        addEntry(minTemp, entries_min);
        addEntry(maxTemp, entries_max);
        LineDataSet maxDataSet = new LineDataSet(entries_max, "Max");
        LineDataSet minDataSet = new LineDataSet(entries_min, "Min");
        maxDataSet.setLineWidth(5f);
        minDataSet.setLineWidth(2f);
        maxDataSet.setColor(R.color.red);
        dataSets.add(maxDataSet);
        dataSets.add(minDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    private void initializeViews(){
        chart = findViewById(R.id.chart);
        String date = getIntent().getStringExtra(DATE);
        TextView textView = findViewById(R.id.textview_description);
        textView.setText(String.format("%s%s", getResources().getString(R.string.hourly_forecast), date));
    }
    private void addEntry(ArrayList<Integer> tempData, List<Entry> entries){
        int base = NUM_OF_HOURS_PER_DAY - PERIOD_OF_TIME * tempData.size();
        for (int i = 0; i < tempData.size(); i++) {
            entries.add(new Entry(base, tempData.get(i)));
            base += 3;
        }
    }
}