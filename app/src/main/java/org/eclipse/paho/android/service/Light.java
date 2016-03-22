package org.eclipse.paho.android.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import org.eclipse.paho.android.service.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Light extends Activity {

    LineChart chart;
    private GestureDetector gestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 30;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        context = this.getApplicationContext();
        setBarData();
    }

    private void setBarData()
    {
        setContentView(R.layout.activity_light);
        //BarChart chart = (BarChart) findViewById(R.id.chart);
        chart = (LineChart) findViewById(R.id.light_chart);
        chart.setVisibility(View.VISIBLE);


        chart.setDescription("");
        chart.setDescriptionTextSize(15f);

        LineData data = new LineData(getXAxisValues(), getDataSet());
        data.setValueTextSize(14f);
        chart.setData(data);
        chart.setScaleEnabled(true);
        chart.animateXY(1500, 1500);
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent motionEvent) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent motionEvent) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent motionEvent) {

            }

            @Override
            public void onChartFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
                try {
                    //if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(v) > SWIPE_THRESHOLD_VELOCITY) {
                        Intent graphIntent = new Intent(context, Temperature.class);
                        startActivity(graphIntent);

                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(v) > SWIPE_THRESHOLD_VELOCITY) {
                        Intent graphIntent = new Intent(context, Noise.class);
                        startActivity(graphIntent);
                    }
                } catch (Exception e) {
                    // nothing
                }
            }

            @Override
            public void onChartScale(MotionEvent motionEvent, float v, float v1) {

            }

            @Override
            public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

            }
        });



    }

    private LineDataSet getDataSet()
    {
        ArrayList<LineDataSet> dataSets = null;
        float [] hourlyLight =   MainActivity.model.getHourlyLight();

        ArrayList<Entry> valueSet = new ArrayList<>();

        // set data set for first device
        for(int i = 0; i< 8; i++ )
        {
            valueSet.add(new Entry (hourlyLight[i], i));
        }


        LineDataSet barDataSet1 = new LineDataSet(valueSet,"Light");

        barDataSet1.setColor(Color.BLUE);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return barDataSet1;

    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
        int tempCurrentTime = currentTime;
        int time[] = new int[8];
        for(int i=0; i<8; i++)
        {
            time[i] = tempCurrentTime - 1;
            if(time[i] == 0)
            {
                time[i]= 8;

            }
            tempCurrentTime = time[i];
        }

        for(int i = 7; i >= 0; i--) {
            String d = "" +time[i];
            xAxis.add(d);
        }

        return xAxis;
    }
}
