package gluapps.Ampere.meter.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import gluapps.Ampere.meter.Activity.AlertServiceJob;

public class AlertServiceReceiver extends BroadcastReceiver {
    int checkActivity;
    int fullbatteryvalue,lowbatteryvalue,hightempbatteryvalue;
    int fullbatteryflag,lowbatteryflag,hightempbatteryflag;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static final String ACTION_DO_STUFF = "action_do_stuff";
     @Override
    public void onReceive(Context context, Intent intent) {
         Intent i = new Intent(context, AlertServiceJob.class);
         Log.d("receiverChecker","Received");
         AlertServiceJob.enqueueWork(context, i.setAction(ACTION_DO_STUFF));
//            sp = context.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
//            if (sp.contains("Key")) {
//                fullbatteryflag = sp.getInt("full_battery_key", 1);
//                lowbatteryflag = sp.getInt("low_battery_key", 0);
//                hightempbatteryflag = sp.getInt("high_temp_key", 0);
//            } else {
//                fullbatteryflag = intent.getIntExtra("highbattery_flagCB", 3);
//                lowbatteryflag =  intent.getIntExtra("lowbattery_flag", 3);
//                hightempbatteryflag = intent.getIntExtra("hightempbattery_flag", 3);
//            }
//
//            Log.d("receiverChecker", fullbatteryflag + "FBF");
//            Log.d("receiverChecker", lowbatteryflag + "LB");
//            Log.d("receiverChecker", hightempbatteryflag + "HTB");
//
//            Log.d("receiverChecker", "received");
////        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
//        String action = intent.getAction();
//            Log.d("receiverCheckerIntent", action);
//            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
//                Log.d("receiverChecker", "receivedFBPowerConnected");
//                if (fullbatteryflag == 1) {
//                    Log.d("receiverChecker", "receivedFB");
//                    Intent i = new Intent(context, AlertServiceJob.class);
//                    i.putExtra("BatteryIntentFB", 0);
//                    AlertServiceJob.enqueueWork(context, i.setAction(ACTION_DO_STUFF));
//                }
//            }
//            if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
//                if (lowbatteryflag == 1) {
//                    Log.d("receiverChecker", "receivedLB");
//                    Intent i = new Intent(context, AlertServiceJob.class);
//                    i.putExtra("BatteryIntentLB", 1);
//                    // Log.d("MySerive12", "current battery level ASR: " + percentage);
//                    AlertServiceJob.enqueueWork(context, i.setAction(ACTION_DO_STUFF));
//                }
//            }
//            if (hightempbatteryflag == 1) {
//                Log.d("receiverChecker", "receivedHT");
//                Intent i = new Intent(context, AlertServiceJob.class);
//                i.putExtra("BatteryIntentHTB", 2);
//                AlertServiceJob.enqueueWork(context, i.setAction(ACTION_DO_STUFF));
//            }
////            }
////        }
////        else {
////
////        }

        }


}
