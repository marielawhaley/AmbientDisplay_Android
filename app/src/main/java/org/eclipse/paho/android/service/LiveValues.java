package org.eclipse.paho.android.service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Float2;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;


public class LiveValues extends Activity{
    private ListView conditionsListView;
    //private View view;
    private GestureDetector gestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 30;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 150;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_values);
        update();
        context = this.getApplicationContext();
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
    }

    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return true;

    }
    public void update() {
        String[] dataString = new String[4];
        float[] data = new float[4];
        //Get from model
        data = MainActivity.model.getLiveValues();
        dataString[0] = "Temperature:     " + Float.toString( data[0])+ " ºC";
        dataString[1] = "Dust:                    " +  Float.toString(data[2])+ "mg/m^3" ;
        dataString[2] = "Noise:                  " +Float.toString(data[3]);
        dataString[3] = "Light:                   " +Float.toString(data[4]) + " Lux";

        Log.i("DEV FRAG", "update");

        ListAdapter devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataString);
        conditionsListView = (ListView) findViewById(R.id.listView2);
        conditionsListView.setAdapter(devicesAdapter);
        conditionsListView.setClickable(false);
        conditionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("temp", "Inside g esture");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent graphIntent = new Intent(context, Temperature.class);
                    startActivity(graphIntent);

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent graphIntent = new Intent(context, Light.class);
                    startActivity(graphIntent);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            update();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
