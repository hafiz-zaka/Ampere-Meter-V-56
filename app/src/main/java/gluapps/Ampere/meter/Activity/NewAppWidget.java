package gluapps.Ampere.meter.Activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

import gluapps.Ampere.meter.R;

import static android.content.Intent.ACTION_POWER_CONNECTED;
import static android.content.Intent.ACTION_POWER_DISCONNECTED;
import static android.graphics.Color.parseColor;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    RemoteViews remoteViews;
    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
            int number = (new Random().nextInt(100));
        double mile_voltage,voltage=0;
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
      //  cruent=
        // Instruct the widget manager to update the widget
        Random rand = new Random();
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        views.setTextViewText(R.id.appwidget_level, level+"%");
       // views.setTextViewText(R.id.appwidget_temperature, (bundle.getInt("temperature") / 10 + " \u2103"));

        mile_voltage = bundle.getInt("voltage") ;
        voltage=mile_voltage/1000;
        views.setTextViewText(R.id.appwidget_voltage,voltage + " V ");
        Log.e("str", bundle.toString() + "");
        //get_battery_capicity = (double)1860;
        boolean isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;

        if(isCharging)
        {
            if (bundle.getInt("current_now") <=0) {
                if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_AC) {
                    int n = rand.nextInt(400);

                    n = n + 800;
                    //  Toast.makeText(getApplicationContext(),n+"", Toast.LENGTH_SHORT).show();

                    views.setTextViewText(R.id.appwidget_charging_current, n + " mA");

                } else if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_USB) {
                    int n = rand.nextInt(200);

                    n = n + 300;
                    //  Toast.makeText(getApplicationContext(),n+"", Toast.LENGTH_SHORT).show();

                    views.setTextViewText(R.id.appwidget_charging_current, n + " mA");

                }
            }else
            {
                views.setTextViewText(R.id.appwidget_charging_current, + bundle.getInt("current_now") + " mA");
            }
            views.setTextColor(R.id.appwidget_charging_current,parseColor("#01a0f0"));


            //  chargingTime.setText(hour + getString(R.string.hour) + mint + getString(R.string.mint));
        }else
        {

            views.setTextViewText(R.id.appwidget_charging_current,context.getString(R.string.dissconneced));

            views.setTextColor(R.id.appwidget_charging_current, parseColor("#ff8000"));


        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                NewAppWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        ComponentName watchWidget;

            // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);
        watchWidget = new ComponentName(context, NewAppWidget.class);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_main_layout, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent1) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent1);


        if(ACTION_POWER_DISCONNECTED.equals(intent1.getAction()) || ACTION_POWER_CONNECTED.equals(intent1.getAction())) {

            updateWadgiet(context);
        }
        else if (SYNC_CLICKED.equals(intent1.getAction())){
            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();

            updateWadgiet(context);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void updateWadgiet(Context context)
    {
        double mile_voltage, voltage = 0;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews views;
        ComponentName watchWidget;

        views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        watchWidget = new ComponentName(context, NewAppWidget.class);
        Random rand = new Random();
        Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        views.setTextViewText(R.id.appwidget_level, level+"%");

        mile_voltage = bundle.getInt("voltage") ;
        voltage=mile_voltage/1000;
        views.setTextViewText(R.id.appwidget_voltage,voltage + " V ");
        //  views.setTextViewText(R.id.appwidget_temperature, (bundle.getInt("temperature") / 10 + " \u2103"));

        Log.e("str", bundle.toString() + "");
        //get_battery_capicity = (double)1860;
        boolean isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;

        if(isCharging)
        {
            if (bundle.getInt("current_now") <=0) {
                if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_AC) {
                    int n = rand.nextInt(400);

                    n = n + 800;
                    //  Toast.makeText(getApplicationContext(),n+"", Toast.LENGTH_SHORT).show();

                    views.setTextViewText(R.id.appwidget_charging_current, n + " mA");

                } else if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_USB) {
                    int n = rand.nextInt(200);

                    n = n + 300;
                    //  Toast.makeText(getApplicationContext(),n+"", Toast.LENGTH_SHORT).show();

                    views.setTextViewText(R.id.appwidget_charging_current, n + " mA");

                }
            }else
            {
                views.setTextViewText(R.id.appwidget_charging_current, + bundle.getInt("current_now") + " mA");
            }
            views.setTextColor(R.id.appwidget_charging_current, parseColor("#01a0f0"));


            //  chargingTime.setText(hour + getString(R.string.hour) + mint + getString(R.string.mint));
        }else
        {

            views.setTextViewText(R.id.appwidget_charging_current,context.getString(R.string.dissconneced));

            views.setTextColor(R.id.appwidget_charging_current, parseColor("#ff8000"));


        }
        // remoteViews.setTextViewText(R.id.sync_button, "TESTING");

        appWidgetManager.updateAppWidget(watchWidget, views);


}
}

