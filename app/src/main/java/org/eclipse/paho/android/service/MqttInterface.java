package org.eclipse.paho.android.service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by marielawhaley on 09/03/16.
 */
public class MqttInterface {
    int qos = 1;
    Context context ;
    MqttAndroidClient client;

    public MqttInterface( Context con)
   {
        String clientID = MqttClient.generateClientId();
        context = con;


        client = new MqttAndroidClient(con, "tcp://85.119.83.194:1883", clientID);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i("Main Activity", "Connection Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i("MQTT INTERFACE", "message Arrived");

                String mssgString;
                mssgString = message.toString();
                String[] data = mssgString.split(",");
                float [] value =  new float[data.length];
                for (int i= 0; i <data.length; ++i)
                {
                    float number = Float.parseFloat(data[i]);
                    float rounded = (int) Math.round(number * 1000) / 1000f;
                    value[i] = rounded;
                }

                if (topic.contentEquals("AD/server-UI/history/temp"))
                {

                    MainActivity.model.setHourlyTemperature(value);
                }
                else if (topic.contentEquals("AD/server-UI/history/dew_point"))
                {
                  MainActivity.model.setHourlyHumidity(value);

                }
                else if (topic.contentEquals("AD/server-UI/history/dust"))
                {
                  MainActivity.model.setHourlyDust(value);
                }
                else if (topic.contentEquals("AD/server-UI/history/noise"))
                {
                    MainActivity.model.setHourlyNoise(value);

                }
                else if (topic.contentEquals("AD/server-UI/history/light"))
                {
                    MainActivity.model.setHourlyLight(value);

                }
                else if(topic.contentEquals("AD/server-UI/feedback"))
                {

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("Main Activity", "Delivery Complete");
            }
        });
    }


    void MqttAction( final String act,  String top, final String message )
    {
        final String action = act;
        final String topicAction = top;
        try {
            client.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken token) {
                    Log.i("Main Activity", "Client connected");
                    Log.i("Main Activity", "Topics=" + token.getTopics());

                    if(action.contentEquals("Subscribe"))
                    {
                        subscribe( topicAction);
                    }
                    if(action.contentEquals("Publish"))
                    {
                        publish(  topicAction,  message);
                    }
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

    void subscribe( String topic)
    {
        try {
            client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Main Activity", "Subscribed");
                    publish("AD/UI-server/request", "lala");
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

    void publish(String topic, String payload)
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
    void unsubscribe( String topic)
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
    void disconnect()
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
