package org.eclipse.paho.android.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private String topic = "mbed-sample";
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String clientID = MqttClient.generateClientId();
        context = getApplicationContext();


        final MqttAndroidClient client = new MqttAndroidClient(this, "tcp://85.119.83.194:1883", clientID);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i("Main Activity", "Connection Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, topic + " " + message, duration);
                toast.show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("Main Activity", "Delivery Complete");
            }
        });

        try {
            client.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken token) {
                    Log.i("Main Activity", "Client connected");
                    Log.i("Main Activity", "Topics=" + token.getTopics());

                    subscribe(client, topic);
                    publish( client, topic,  "Desde Android");

                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    // TODO Auto-generated method stub
                    Log.i("Main Window", "Client connection failed: " + arg1.getMessage());

                }
            });

        }
        catch (MqttException e)
        {
            Log.i("MainActivity", "Error conected");
        }
    }

    void subscribe(MqttAndroidClient client, String topic)
    {
        try {
            IMqttToken token2 = client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Main Activity", "Subscribed");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Main Activity", "NOT SUBS");

                }
            });
        } catch (MqttException e) {
            Log.i(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle ", e);
            String text = "Connected";
        }
    }

    void publish(MqttAndroidClient client, String topic, String payload)
    {
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes();
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Main Activity", "Published");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Main Activity", "Not Published");
                }
            });

        } catch ( MqttException e) {
            e.printStackTrace();
        }


    }
    void unsubscribe(MqttAndroidClient client, String topic)
    {
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    void disconnect(MqttAndroidClient client)
    {
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


}
