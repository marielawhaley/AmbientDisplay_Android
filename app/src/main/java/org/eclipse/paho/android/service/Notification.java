package org.eclipse.paho.android.service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by marielawhaley on 22/03/16.
 */
public class Notification {
    private Context context;
    public void getContext(Context c){context = c;}

    public void sendNotification(int parameter, float value)
    {
        String condition = "";
        String highLow= "";
        if (parameter == 0) {
            condition = "Temperature";
            if(value > 20)
                highLow = "high";
            else
                highLow = "low";
        }
        else if(parameter ==1) {
            condition = "Humidity";
            highLow = "high";
        }
        else if (parameter == 2){
            condition = "Dust";
        highLow = "high";
        }
        else if (parameter == 3){
            condition = "Noise";
            highLow = "high";
        }
        else if(parameter == 4) {
            condition = "Light";
            highLow = "low";
        }

        String title = "User feedback: ";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        String text = "User thinks " + condition + " is too " + highLow;
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.desk_egg_icon);

        android.app.Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(8, notification);
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();

    }
}
