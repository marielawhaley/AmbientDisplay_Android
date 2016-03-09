package org.eclipse.paho.android.service;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.eclipse.paho.android.service.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Temperature extends Activity {

    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);


    }
    private void setBarData()
    {
        setContentView(R.layout.activity_temperature);
        //BarChart chart = (BarChart) findViewById(R.id.chart);
        chart = (LineChart) findViewById(R.id.temperature_chart);
        chart.setVisibility(View.VISIBLE);


        chart.setDescription("");
        chart.setDescriptionTextSize(15f);

        LineData data = new LineData(getXAxisValues(), getDataSet());
        data.setValueTextSize(14f);
        chart.setData(data);
        chart.animateXY(1500, 1500);
    }

    private ArrayList<LineDataSet> getDataSet()
    {
        ArrayList<LineDataSet> dataSets = null;
        float [] hourlyTemperature =   MainActivity.model.getHourlyTemperature();

        ArrayList<Entry> valueSetDevice1 = new ArrayList<>();

        // set data set for first device
        for(int i = 0; i< 7; i++ )
        {
            valueSetDevice1.add(new Entry (hourlyTemperature[i], i));
        }


        LineDataSet barDataSet1 = new LineDataSet(valueSetDevice1,"Temperature");

        barDataSet1.setColor(Color.BLUE);


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;

    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentTime = calendar.get(Calendar.HOUR_OF_DAY);

        int time[] = new int[24];
        for(int i=0; i<24; i++)
        {
            time[i] = currentTime - i - 1;
            if(time[i] == 0)
            {
                time[i]= 24;
            }
        }

        for(int i = 23; i >= 0; i--) {
            String d = getDay(days[i]);
            xAxis.add(d);
        }

        return xAxis;
    }



}
