package gluapps.Ampere.meter.receiver;

/**
 * Created by zaka ullah on 1/20/2016.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import java.util.Random;

import gluapps.Ampere.meter.Activity.MainActivity;
import gluapps.Ampere.meter.Activity.splash;
import gluapps.Ampere.meter.R;


public class OnPowerReceiver extends BroadcastReceiver {
    int myIntValue,notificationCheckValue,fullBatteryCV,lowBatteryCV,permanantNotificationCheckValue;
    boolean check_interti;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    NotificationCompat.Builder mBuilder;
    String NOTIFICATION_CHANNEL_ID = "battery";
    //User visible Channel Name
    String CHANNEL_NAME = "Battery Notification";
    // Importance applicable to all the notifications in this Channel
    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("your_prefs", context.MODE_PRIVATE);
        myIntValue = sp.getInt("your_int_key", 5);
        check_interti= sp.getBoolean("check_intertestial", true);
        notificationCheckValue = sp.getInt("notification_key", 5);
        fullBatteryCV = sp.getInt("full_battery_key", 5);
        lowBatteryCV = sp.getInt("low_battery_key", 5);
        permanantNotificationCheckValue = sp.getInt("permanant_notification_key", 5);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration is enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[] {
                    500,
                    500,
                    500,
                    500,
                    500
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }

       // Toast.makeText(context, "male12"+myIntValue, Toast.LENGTH_SHORT).show();


        if(intent.getAction().equalsIgnoreCase((Intent.ACTION_POWER_DISCONNECTED))){
            final Intent fullBatteryintent = new Intent(context, AlertServiceReceiver.class);

            context.stopService(fullBatteryintent);

            mNotifyMgr.cancel(1);
            if(lowBatteryCV==1)
            {
                final Intent lowBatteryintent = new Intent(context, AlertServiceReceiver.class);


                lowBatteryintent.putExtra("lowbattery_flag",1);

                context.startService(lowBatteryintent);
            }
            if(permanantNotificationCheckValue==1)
            {
              try {
                  mainNoification(context);
              }catch (NullPointerException e)
              {
                  e.printStackTrace();
              }catch (ReceiverCallNotAllowedException e)
              {
                  e.printStackTrace();
              }


                   Intent resultIntent = new Intent(context, splash.class);
                try {
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                   mBuilder.setContentIntent(resultPendingIntent);
               }catch(NullPointerException e)
               {
                   e.printStackTrace();
               }
               catch(RuntimeException e)
               {
                   e.printStackTrace();
               }
try {

    mBuilder.setOngoing(true);
}catch (RuntimeException e){

}
              try {
                  mNotifyMgr.notify(2, mBuilder.build());
              }catch (NullPointerException e)
              {

              }
            }
        }
        else {
            if(fullBatteryCV!=0)
            {
                final Intent fullBatteryintent = new Intent(context, AlertServiceReceiver.class);

                fullBatteryintent.putExtra("highbattery_flag",0);

                context.startService(fullBatteryintent);
            }
            if (myIntValue != 1) {

    Intent i = new Intent(context, MainActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
       // Toast.makeText(context, "male"+myIntValue, Toast.LENGTH_SHORT).show();

        context.startActivity(i);
    } catch (ActivityNotFoundException e) {

    } catch (RuntimeException e) {

    }


            }
            if (notificationCheckValue == 1) {
try {
    mainNoification(context);
}catch (NullPointerException e) {

}
                Intent resultIntent = new Intent(context, splash.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                try {
                    mBuilder.setContentIntent(resultPendingIntent);
                }catch(NullPointerException e)
                {

                }
                try {
                    mNotifyMgr.notify(1, mBuilder.build());
                }catch(NullPointerException e)
                {

                }

            }
            if(permanantNotificationCheckValue==1)
            {
                mainNoification(context);
                Intent resultIntent = new Intent(context, splash.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setOngoing(true);
                try {
                    mNotifyMgr.notify(2, mBuilder.build());
                }catch (NullPointerException e)
                {

                }
            }
        }

    }
    public void mainNoification(Context context) {
        Intent intent1 = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent1.getExtras();

        int level = intent1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        boolean isPresent = intent1.getBooleanExtra("present", false);

        if (isPresent) {

            int tempC = bundle.getInt("temperature") / 10;
            double tempF = tempC * 1.8 + 32;

            double mile_voltage = bundle.getInt("voltage");
            double voltage = mile_voltage / 1000;
            Random rand = new Random();


            boolean isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;
            if (isCharging) {
                if (bundle.getInt("current_now") <= 0) {
                    if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_AC) {
                        int n = rand.nextInt(400);

                        n = n + 800;
                        mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                                .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.connectd) + "(" + n + " mA)")
                                .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V" );
                        mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));

                    } else if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_USB) {
                        int n = rand.nextInt(200);
                        n = n + 300;
                        mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                                .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.connectd) + "(" + n + " mA)")
                                .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V");
                        mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));

                    }

                }

                    mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher) .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.connectd) + "(" + bundle.getInt("current_now") + " mA)")
                            .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V"  );
                mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));


            }
            else
            {
                mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher) .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.dissconneced) )
                        .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V" );
                mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            }

        }
    }
}

