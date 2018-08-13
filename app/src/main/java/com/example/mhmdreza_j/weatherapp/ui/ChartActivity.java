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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        LineChart chart = findViewById(R.id.chart);
        ArrayList<Integer> maxTemp = getIntent().getIntegerArrayListExtra(MAX_TEMP);
        ArrayList<Integer> minTemp = getIntent().getIntegerArrayListExtra(MIN_TEMP);
        String date = getIntent().getStringExtra(DATE);
        TextView textView = findViewById(R.id.textview_description);
        textView.setText(String.format("%s%s", getResources().getString(R.string.hourly_forecast), date));
        List<Entry> entries_min = new ArrayList<>();
        List<Entry> entries_max = new ArrayList<>();
        int size =  maxTemp.size();
        int base = 24 - 3 * size;
        for (int i = 0; i < minTemp.size(); i++) {
            entries_min.add(new Entry(base, minTemp.get(i)));
            base += 3;
        }
        base = 24 - 3 * size;
        for (int i = 0; i < maxTemp.size(); i++) {
            entries_max.add(new Entry(base, maxTemp.get(i)));
            base += 3;
        }
        LineDataSet maxDataSet = new LineDataSet(entries_max, "Max");
        LineDataSet minDataSet = new LineDataSet(entries_min, "Min");
        List<ILineDataSet> dataSets = new ArrayList<>();
        maxDataSet.setLineWidth(5f);
        minDataSet.setLineWidth(2f);
        maxDataSet.setColor(R.color.red);
        dataSets.add(maxDataSet);
        dataSets.add(minDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }
}