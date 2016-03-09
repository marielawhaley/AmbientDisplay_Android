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

import org.eclipse.paho.android.service.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Light extends Activity {

    LineChart chart;
    private GestureDetector gestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        setBarData();
        context = this.getApplicationContext();
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
    }

    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return true;

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



    }

    private LineDataSet getDataSet()
    {
        ArrayList<LineDataSet> dataSets = null;
        float [] hourlyLight =   MainActivity.model.getHourlyLight();
        for(int i = 0; i <12 ; i++)
        {
            hourlyLight[i] = (float)21.1;
        }

        ArrayList<Entry> valueSet = new ArrayList<>();

        // set data set for first device
        for(int i = 0; i< 12; i++ )
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
        int time[] = new int[12];
        for(int i=0; i<12; i++)
        {
            time[i] = tempCurrentTime - 1;
            if(time[i] == 0)
            {
                time[i]= 12;

            }
            tempCurrentTime = time[i];
        }

        for(int i = 11; i >= 0; i--) {
            String d = "" +time[i];
            xAxis.add(d);
        }

        return xAxis;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("temp", "Inside gesture");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
                    Intent graphIntent = new Intent(context, MainActivity.class);
                    startActivity(graphIntent);

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(context, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }


}
