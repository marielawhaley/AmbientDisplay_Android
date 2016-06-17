package org.eclipse.paho.android.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.eclipse.paho.android.service.R;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {
    private int qos =1;
    private String topic = "AD/#";
    Context context ;
    public static Model model;
    public static Notification n;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private RelativeLayout eyeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new Model();
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        n = new Notification();
        n.getContext(context);
        try {
            wait(8000);  //Delay of 6 seconds

        } catch (Exception e) {
        }
         MqttInterface mqtt = new MqttInterface(context);
        mqtt.MqttAction("Subscribe", topic, "null");

        //MqttInterface mqtt2 = new MqttInterface(context);
        //mqtt2.MqttAction("Publish","AD/UI-server/request", "lala");

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        Thread welcomeThread = new Thread() {
            @Override
        public void run() {
            try {
                super.run();
                sleep(8000);  //Delay of 6 seconds

            } catch (Exception e) {

            } finally {

                Intent i = new Intent(context, LiveValues.class);
                startActivity(i);
                finish();
            }
        }
    };
    welcomeThread.start();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return true;

    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("main activity", "Inside gesture");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
                    Intent graphIntent = new Intent(context, Temperature.class);
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
        public void onLongPress(MotionEvent e) {
            Log.i("main activity", "Inside longpress");
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }



}
