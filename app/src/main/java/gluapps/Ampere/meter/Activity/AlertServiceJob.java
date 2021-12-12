package gluapps.Ampere.meter.Activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

import gluapps.Ampere.meter.R;

public class AlertServiceJob extends JobIntentService {
    int checkActivity;
    int fullbatteryvalue,lowbatteryvalue,hightempbatteryvalue,permanantNotificationCheckValue, notificationCheckValue;
    int fullbatteryflag,lowbatteryflag,hightempbatteryflag, notificationCB, checkBoxValue;
    SharedPreferences sp;
    boolean isCharging, isNotCharging;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    Context context;
    static final int JOB_ID = 1001;
    public static final String ACTION_DO_STUFF = "action_do_stuff";
    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,AlertServiceJob.class,JOB_ID,work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
            Log.d("MyService", "onStartCommand");
            // int cmd = super.onStartCommand(intent, flags, startId);
        mNotifyMgr = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);  //**************low battery without charging notification**********//
        getApplicationContext().registerReceiver(batteryChangeReceiver, intentFilter);

        context = getApplicationContext();
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DO_STUFF:
                    }
            }
    }

    BroadcastReceiver batteryChangeReceiver = new BroadcastReceiver() {        @Override

    public void onReceive(final Context context, final Intent intent) {            // Toast.makeText(getApplicationContext(),"staart"+fullbatteryvalue+lowbatteryvalue, Toast.LENGTH_SHORT).show();

        //if(fullbatteryflag==0) {
        checkActivity = intent.getIntExtra("lowbattery", 2);
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        fullbatteryvalue = sp.getInt("full_battery_value", 100);
        lowbatteryvalue = sp.getInt("low_battery_value", 20);
        hightempbatteryvalue = sp.getInt("high_temp_value", 60);
        Log.d("MyService","EnteredinSwitchCase");
        fullbatteryflag = sp.getInt("full_battery_key",1);
        Log.d("MyService", "fullbatterylevel: " + fullbatteryflag);
        lowbatteryflag = sp.getInt("low_battery_key",1);
        Log.d("MyService", "lowbatteryflag: " + lowbatteryflag);
        hightempbatteryflag = sp.getInt("high_temp_key",0);
       // notificationCB = sp.getInt("notification_key",1);
        //checkBoxValue = sp.getInt("your_int_key",1);

        String action = intent.getAction();
        if (action.equals(Intent.ACTION_POWER_CONNECTED)){
            if (fullbatteryflag == 1 ) {
                Log.d("MyService", "fullbatterylevel: intent send " );
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);
                fullBatteryLevel(batteryStatus);
            }
        }
        //**************low battery without charging notification**********//
//        Intent i = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        Bundle bundle = i.getExtras();
//        isNotCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;
//        Log.d("MyService",isNotCharging+"");
//        if (isNotCharging!= true){
//            Toast.makeText(AlertServiceJob.this,"is not Charging",Toast.LENGTH_SHORT).show();;
//        }
//        if (lowbatteryflag == 1){// ***** Change Below Condition Like this
//            if (action.equals(Intent.ACTION_POWER_DISCONNECTED) || isNotCharging != true)
        //**************low battery without charging notification**********//
        if (action.equals(Intent.ACTION_POWER_DISCONNECTED)){
            if (lowbatteryflag == 1){
                Log.d("MyService", "lowbatteryflag: intent send " );
                IntentFilter lifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, lifilter);
                lowBatteryLevel(batteryStatus);
            }
        }
        if (hightempbatteryflag ==1) {
            Log.d("MyService", "hightempbatteryflag");
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            hightempBattery(batteryStatus);
        }
        permanantNotificationCheckValue = sp.getInt("permanant_notification_key", 5);
        notificationCheckValue = sp.getInt("notification_key", 5);

        if(permanantNotificationCheckValue==1)
        {
            mainNoification(context);
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
            }catch (NullPointerException e)
            {

            }
            mBuilder.setOngoing(true);
            mNotifyMgr.notify(2, mBuilder.build());
        }
        if(notificationCheckValue==1)
        {
            mainNoification(context);
            try {
                if (isCharging) {

                    Intent resultIntent = new Intent(context, splash.class);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    mNotifyMgr.notify(1, mBuilder.build());
                } else {
                    mNotifyMgr.cancel(1);
                }
            }catch (IllegalStateException e){

            }
        }
        // }
        }

    };
    public void mainNoification(Context context) {
        permanantNotificationCheckValue = sp.getInt("permanant_notification_key", 5);
        notificationCheckValue = sp.getInt("notification_key", 5);

        Intent intent1 = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent1.getExtras();

        int level = intent1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        boolean isPresent = intent1.getBooleanExtra("present", false);

        if (isPresent) {

            int tempC = bundle.getInt("temperature") / 10;
            double tempF = tempC * 1.8 + 32;
            Log.d("permanentNotification","permanent notification called");
            double mile_voltage = bundle.getInt("voltage");
            double voltage = mile_voltage / 1000;
            Random rand = new Random();

            // This is the Notification Channel ID. More about this in the next section
            String NOTIFICATION_CHANNEL_ID = "battery";
            //User visible Channel Name
            String CHANNEL_NAME = "Battery Notification";
            // Importance applicable to all the notifications in this Channel
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
            try {
                isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;
            }catch (IllegalStateException e){

            }
            try {


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
                                .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V" );
                        mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));

                    }

                }

                mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.connectd) + "(" + bundle.getInt("current_now") + " mA)")
                        .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V" );
                mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));


            }
            else if(permanantNotificationCheckValue==1)
            {
                mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher) .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(context.getString(R.string.dissconneced) )
                        .setContentText(context.getString(R.string.voltage_text_view) + " : " + voltage + " V");
                mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            }
            }catch (IllegalStateException e){

            }
        }
        try{
            unregisterReceiver(batteryChangeReceiver);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fullBatteryLevel(Intent batteryChangeIntent){
            int level = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float percentage = (float) (level / (float) scale) * 100;
            percentage = Math.round(percentage);
            Log.d("MySerive12", "current battery level: " + percentage);

            // full battery
            if (percentage >= fullbatteryvalue) {
                // some calculations
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                // This is the Notification Channel ID. More about this in the next section
                String NOTIFICATION_CHANNEL_ID = "full_battery";
                //User visible Channel Name
                String CHANNEL_NAME = "Full Battery Notification";
                // Importance applicable to all the notifications in this Channel
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                //Notification channel should only be created for devices running Android 26
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel =
                            new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
                    //Boolean value to set if lights are enabled for Notifications from this Channel
                    notificationChannel.enableLights(true);
                    //Boolean value to set if vibration is enabled for Notifications from this Channel
                    notificationChannel.enableVibration(true);
                    //Sets the color of Notification Light
                    notificationChannel.setLightColor(Color.GREEN);
                    //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
                    notificationChannel.setVibrationPattern(new long[]{
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
                NotificationCompat.Builder mBuilder;


                mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.full_battery)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentTitle(getString(R.string.Alert_custom__full_level))
                        .setContentText(getString(R.string.level_text_view) + " : " + percentage + "%");
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                mBuilder.setSound(uri);

                mBuilder.setColor(getResources().getColor(R.color.colorfullbattery));
                Log.d("MySerive12", "battery fully loaded");
                Intent resultIntent = new Intent(this, splash.class);
                resultIntent.putExtra("smsMsg", true);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);
                try {
                    mNotifyMgr.notify(1, mBuilder.build());
                } catch (NullPointerException e) {

                }
                // unregisterReceiver(batteryChangeReceiver);
                stopSelf();
            }
    }
    public void lowBatteryLevel(Intent batteryChangeIntent) {
            int level = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float percentage = (float) (level / (float) scale) * 100;
            percentage = Math.round(percentage);
            Log.d("MySerive12", "current battery level: " + percentage);
            Log.d("MySerive12", "lowBatteryValue: " + lowbatteryvalue);
            // full battery
            if (percentage < lowbatteryvalue) {  //==  //**************low battery without charging notification Fix this value**********//

                // some calculations
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                // This is the Notification Channel ID. More about this in the next section
                String NOTIFICATION_CHANNEL_ID = "low_battery";
                //User visible Channel Name
                String CHANNEL_NAME = "Low Battery Notification";
                // Importance applicable to all the notifications in this Channel
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                //Notification channel should only be created for devices running Android 26
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel =
                            new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
                    //Boolean value to set if lights are enabled for Notifications from this Channel
                    notificationChannel.enableLights(true);
                    //Boolean value to set if vibration is enabled for Notifications from this Channel
                    notificationChannel.enableVibration(true);
                    //Sets the color of Notification Light
                    notificationChannel.setLightColor(Color.GREEN);
                    //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
                    notificationChannel.setVibrationPattern(new long[]{
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
                NotificationCompat.Builder mBuilder;
                mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.low_battery)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentTitle(getString(R.string.Alert_custom__low_level))
                        .setContentText(getString(R.string.level_text_view)  + " : " + percentage + "%");
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(uri);
                mBuilder.setColor(getResources().getColor(R.color.colorlowbattery));
                Log.d("MySerive12", "Notification Came of Battery Low");
                Intent resultIntent = new Intent(this, splash.class);
                resultIntent.putExtra("smsMsg", true);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);
                try {
                    if (checkActivity != 1)
                        mNotifyMgr.notify(1, mBuilder.build());
                } catch (NullPointerException e) {

                }
                // unregisterReceiver(batteryChangeReceiver);
                stopSelf();
        }
    }
    public void hightempBattery(Intent batteryChangeIntent) {

        Bundle bundle = batteryChangeIntent.getExtras();
        int tempC=bundle.getInt("temperature") / 10;
        // full battery
     //   Toast.makeText(getApplicationContext(),"staart"+tempC, Toast.LENGTH_SHORT).show();

        if (tempC >= hightempbatteryvalue) {
            // some calculations
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            // This is the Notification Channel ID. More about this in the next section
            String NOTIFICATION_CHANNEL_ID = "temp";
            //User visible Channel Name
            String CHANNEL_NAME = "Temperature Notification";
            // Importance applicable to all the notifications in this Channel
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
            NotificationCompat.Builder mBuilder;

            mBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.high_temp)
                    .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(getString(R.string.Alert_custom__high_temp))
                    .setContentText(getString(R.string.temperature_text_view)+" : "+hightempbatteryvalue + " ℃");
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            mBuilder.setColor(getResources().getColor(R.color.colorhightempbattery));
            Log.d("MySerive12", "battery fully loaded");
            Intent resultIntent = new Intent(this, splash.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            try{
            mNotifyMgr.notify(1, mBuilder.build());
            }catch(NullPointerException e)
            {

            }
        }
    }
}
