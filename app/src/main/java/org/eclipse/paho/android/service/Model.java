package org.eclipse.paho.android.service;

/**
 * Created by marielawhaley on 09/03/16.
 */
public class Model {

    private float []hourlyTemperature = new float[8];
    private float[] hourlyHumidity = new float[8];
    private float[] hourlyDust = new float[8];
    private float[] hourlyNoise = new float[8];
    private float[] hourlyLight = new float[8];
    private float[] liveValues = new float[5];

    public Model()
    {
        for (int j = 0 ; j<5 ; j++)
        {
         liveValues[j] = 0;
        }
        //Defaults
        for(int i = 0; i<8; i++)
        {
            hourlyTemperature[i] = 0;
            hourlyHumidity[i] = 0;
            hourlyDust[i] = 0;
            hourlyNoise[i] = 0;
            hourlyLight[i] =0;
        }
    }
    public void setLiveValues(float[] liveValues)
    {
        this.liveValues = liveValues; // temperature, humidity, dust, noise, light
    }

    public void setHourlyHumidity(float[] hourlyHumidity) {
        this.hourlyHumidity = hourlyHumidity;
    }

    public void setHourlyDust(float[] hourlyDust) {
        this.hourlyDust = hourlyDust;
    }

    public void setHourlyLight(float[] hourlyLight) {
        this.hourlyLight = hourlyLight;
    }

    public void setHourlyNoise(float[] hourlyNoise) {
        this.hourlyNoise = hourlyNoise;
    }

    public void setHourlyTemperature(float[] hourlyTemperature) {
        this.hourlyTemperature = hourlyTemperature;
    }

    public float[] getLiveValues()
    {
        return liveValues;
    }
    public float[] getHourlyDust() {
        return hourlyDust;
    }

    public float[] getHourlyHumidity() {
        return hourlyHumidity;
    }

    public float[] getHourlyLight() {
        return hourlyLight;
    }

    public float[] getHourlyNoise() {
        return hourlyNoise;
    }

    public float[] getHourlyTemperature() {
        return hourlyTemperature;
    }
}

