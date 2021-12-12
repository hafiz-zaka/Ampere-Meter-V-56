package gluapps.Ampere.meter.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.ads.AdListener;


import gluapps.Ampere.meter.R;
import gluapps.Ampere.meter.receiver.AlertServiceReceiver;
import gluapps.Ampere.meter.util.IabHelper;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static gluapps.Ampere.meter.Activity.MyBilling.TAG;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout LanguageActivity;
    TextView tv_capcity_detaile, tv_technology_detaile, tv_health_detaile, tv_temperature_detaile,
            tv_voltage_detaile, tv_charger_detaile, tv_charging_state, tv_remaning_detaile, tv_status_detaile,
            tv_model_detaile, tv_build_id_detaile, tv_version_detaile, tv_level_detaile, tv_capcity, tv_technology, tv_health, tv_temperature,
            tv_voltage, tv_charger, tv_charging, tv_remaning, tv_status,
            tv_model, tv_build_id, tv_version, tv_level, message,alert_main_tv,tv_charging_deatail;
    ImageView alert_main_im;
   // private com.facebook.ads.InterstitialAd interstitialAd,moreTipsinterstitialAd,mavinterstitialAd;
    int fullbatteryflag, lowbatteryflag, hightempbatteryflag;
    AlertServiceReceiver alertServiceReceiver;
    String Battery_Full_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryFull";
    String Battery_Low_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryLow";
    String Battery_Power_Disconnected_Intent = "android.intent.action.ACTION_POWER_DISCONNECTED";
    String Battery_Power_Connected_Intent = "android.intent.action.ACTION_POWER_CONNECTED";
    String Battery_High_Temperature_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryHighTemperature";
   MyBilling myBilling=new MyBilling(this);
    Boolean check_chip;
    int CheckLowBattery;
    Constants constants;
    AdRequest adRequest1;
    Dialog dialog;
    TimerTask tt;
    Timer t;
    com.google.android.gms.ads.AdView mAdView;
    RelativeLayout relativeAdmob;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int myIntValue = 0;
    Context context = this;
    Double get_charging_time, charging_time, get_battery_capicity;
    int current_now;
    int hour, mint = 00;
    int remaninh_hour_main, remaning_mint_main = 00;
    //code od inpurchase
    boolean chek = true;
    int version;
    int level;
    int ratingCheck, temperatureCheckValue, chargingTimevalue,voltageCheckValue;
    static final String ITEM_SKU = "com.ampere.ad";
    IabHelper mHelper;
    NotificationCompat.Builder mBuilder;
   NavigationView navigationView;
     DrawerLayout drawerLayout;
LinearLayout tempLinearLayout,voltageLinearLayout,settingLL,alertsettingLL,playservicesLL;
RelativeLayout updatesoftwareLL;
    Intent intent;
    private BillingClient billingClient;

    // Interstitial interstitial_Ad;
//    private InterstitialAd interstitialAd;
    private BroadcastReceiver BatteryInfo = new BroadcastReceiver() {
        int status, health_, pluggedType;
        Dialog dialog1;


        @Override
        public void onReceive(Context ctxt, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);


            Bundle bundle = intent.getExtras();
            String str = bundle.toString();
            boolean isPresent = intent.getBooleanExtra("present", false);
            tv_level_detaile.setText(level + "%");
            if (isPresent) {
                int percent = (level * 100) / scale;

                //tv_capcity_detaile.setText(getBatteryCapacity() + " mA");
                // tv_capcity_detaile.setText("" + version); // because capcity have some issue so we are using ap level at this place
                tv_technology_detaile.setText(bundle.getString("technology"));
                tv_health_detaile.setText(getHealthString(bundle.getInt("health")));
                int tempC = bundle.getInt("temperature") / 10;
                double tempF = tempC * 1.8 + 32;
                temperatureCheckValue = sp.getInt("temperature_key", 5);
                 //Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
             //   Log.e("check cuuret",bundle.toString());
                if (temperatureCheckValue == 5) {
                    tv_temperature_detaile.setText(bundle.getInt("temperature") / 10 + " \u2103");

                } else {
                    tv_temperature_detaile.setText(((int) tempF) + "\u00b0" + "F");
                }
                double mile_voltage = bundle.getInt("voltage");
                double voltage = mile_voltage / 1000;
                if (voltageCheckValue == 5) {
                    tv_voltage_detaile.setText(voltage + " V");


                } else {
                    tv_voltage_detaile.setText(mile_voltage + " mV");


                }


                Random rand = new Random();
                double random = Math.random() * 1200 + 900;



                tv_charger_detaile.setText(getPlugTypeString(bundle.getInt("plugged")));
                tv_remaning_detaile.setText(scale - level + "%");
                tv_status_detaile.setText(getStatusString(bundle.getInt("status")));
                boolean isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;
              //  Toast.makeText(getApplicationContext(),bundle.getInt("current_now")+"", Toast.LENGTH_SHORT).show();
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
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
                if (isCharging) {
                    if (bundle.getInt("current_now") <= 0) {
                        if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_AC) {

                            int n = rand.nextInt(400);

                            n = n + 800;

                            mBuilder =
                                    new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setTicker(level + "").setSmallIcon(R.mipmap.ic_launcher)
                                            .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(getString(R.string.connectd) + "(" + n + " mA) *")
                                            .setContentText(getString(R.string.voltage_text_view) + " " + voltage + " V" + " || Battery level " + level).setAutoCancel(false);
                            tv_charging_state.setText(getString(R.string.connectd) + "(" + n + " mA) *");
                            tv_charging_deatail.setText(n + " mA");
                            message.setVisibility(View.VISIBLE);
                        } else if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_USB) {
                            int n = rand.nextInt(200);

                            n = n + 300;
                            //  Toast.makeText(getApplicationContext(),n+"", Toast.LENGTH_SHORT).show();
                            mBuilder =
                                    new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setTicker(level + "").setSmallIcon(R.mipmap.ic_launcher)
                                            .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(getString(R.string.connectd) + "(" + n + " mA) *")
                                            .setContentText(getString(R.string.voltage_text_view) + " " + voltage + " V" + " || Battery level " + level);
                            tv_charging_state.setText(getString(R.string.connectd) + "(" + n + " mA) *");
                            message.setVisibility(View.VISIBLE);
                            tv_charging_deatail.setText(n + " mA");

                        }

                        tv_charging_state.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog1 = new Dialog(MainActivity.this);
                                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog1.setContentView(R.layout.no_chip);
                                dialog1.show();
                                Button dismessButton = (Button) dialog1.findViewById(R.id.dialog_close_button);
                                dismessButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        });
                    } else {

                        mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setTicker(level + "").setSmallIcon(R.mipmap.ic_launcher)
                                .setStyle(new NotificationCompat.BigTextStyle()).setContentTitle(getString(R.string.connectd) + "(" + bundle.getInt("current_now") + " mA)")
                                .setContentText(getString(R.string.voltage_text_view) + " " + voltage + " V" + " || Battery level " + level);
                        tv_charging_state.setText(getString(R.string.connectd) + "(" + bundle.getInt("current_now") + " mA)");
                        message.setVisibility(View.GONE);
                        tv_charging_deatail.setText(bundle.getInt("current_now") + " mA");

                    }
                    tv_charging_state.setTextColor(Color.parseColor("#01a0f0"));

                } else {
                    if (bundle.getInt("current_now") < 0)
                        tv_charging_deatail.setText("" + bundle.getInt("current_now") + " mA");
                    else if(bundle.getInt("current_now") > 0) {
                        tv_charging_deatail.setText("- " + bundle.getInt("current_now") + " mA");
                    }
                    else {
                        int n = rand.nextInt(200);
                        n = n + 300;
                        tv_charging_deatail.setText("- " + "" + n + " mA ");
                    }
                    mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.dissconneced)).setContentText(getString(R.string.voltage_text_view) + " " + voltage + " V" + " || Battery level " + level);
                    // dialog.dismiss();
                    tv_charging_state.setText(getString(R.string.dissconneced));
                    tv_charging_state.setTextColor(Color.parseColor("#ff8000"));
                    tv_remaning_detaile.setText(getString(R.string.not_charging));
                }



            } else {
                // battery_percentage.setText("Battery not present!!!");
            }
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );



            // }
        }
    };


    private String getPlugTypeString(int plugged) {
        String plugType = getString(R.string.defult_plug_type);

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = getString(R.string.AC_plug_type);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = getString(R.string.Usb_plug_type);
                break;
        }
        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = getString(R.string.dead_health_String);
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = getString(R.string.dead_health_String);
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = getString(R.string.good_health_String);
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = getString(R.string.over_voltage_health_String);
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = getString(R.string.over_heat_health_String);
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = getString(R.string.faliur_health_String);
                break;
        }
        return healthString;
    }

    private String getStatusString(int status) {
        String statusString = getString(R.string.defult_status);

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = getString(R.string.charging_status);
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = getString(R.string.discharge_status);
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = getString(R.string.full_status);
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = getString(R.string.not_charging_status);
                break;
        }
        return statusString;
    }

    boolean check_admob;
    //AlertDialog alertDialog;
    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();

    private void querySkuDetails() {
        SkuDetailsParams.Builder skuDetailsParamsBuilder
                = SkuDetailsParams.newBuilder();
        ArrayList<String> list = new ArrayList<>();
        list.add(ITEM_SKU);
        skuDetailsParamsBuilder.setSkusList(list);
        skuDetailsParamsBuilder.setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(skuDetailsParamsBuilder.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<SkuDetails> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    for (SkuDetails skuDetails : list) {
                        mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    }
                }
            }
        });
    }

    private void startPurchase() {
        if (mSkuDetailsMap.size() > 0) {
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(mSkuDetailsMap.get(ITEM_SKU))
                    .build();

            billingClient.launchBillingFlow(this, billingFlowParams);
        }
    }

    protected void RestoringPurchases(){
        //To Query you have to provide skuType which is "BillingClient.SkuType.INAPP" or "BillingClient.SkuType.SUBS"
        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull @NotNull BillingResult billingResult, @NonNull @NotNull List<Purchase> list) {
                //here you can process your purchases.
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     try {
            setContentView(R.layout.activity_main);
        }catch (RuntimeException e)
        {

        }
         PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
             // To be implemented in a later section.
             Toast.makeText(getApplicationContext(), "" , Toast.LENGTH_SHORT).show();
             Log.d("fullbatteryMMMAAAvbn", billingResult.getResponseCode()+"");

             Toast.makeText(getApplicationContext(), "" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();

         };

         BillingClient billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

//        billingClient = BillingClient.newBuilder(getApplicationContext()).setListener(new PurchasesUpdatedListener() {
//            @Override
//            public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {
//                //here you can process the new purchases.
//                Toast.makeText(getApplicationContext(), "" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }).enablePendingPurchases().build();

//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingServiceDisconnected() {
//                //you can restart your client here.
//            }
//
//            @Override
//            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
//                Toast.makeText(getApplicationContext(), "" + billingResult.getResponseCode()+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
//
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
//                    //here billing Client is ready to be used.
//                    Toast.makeText(getApplicationContext(), "" + billingResult.getResponseCode()+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
//
//                    querySkuDetails();
//                }
//            }
//        });
         intent= getIntent();
        registerReceiver(BatteryInfo, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        myBilling.onCreate();
        alertServiceReceiver = new AlertServiceReceiver();
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);




        myIntValue = sp.getInt("ad_value", 5);
        if (myIntValue == 10) {
            check_admob = false;

        } else {
            check_admob = true;
        }


        version = Integer.valueOf(Build.VERSION.SDK_INT);


        fullbatteryflag = sp.getInt("full_battery_flag_value",3);
        final int interval = 60 * 60 * 1000;
        final Intent fullBatteryintentMA = new Intent();
        fullBatteryintentMA.setAction(Battery_Full_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        fullBatteryintentMA.setClass(this, AlertServiceReceiver.class);
        AlarmManager alarmManagerFBMA =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Random rS = new Random();
        int intent_codeS = rS.nextInt();
        PendingIntent pendingIntentFBMA;
        pendingIntentFBMA =
                PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, fullBatteryintentMA, 0);
        if (fullbatteryflag == 3){
            Log.d("fullbatteryMMMAAA","MainActivity Called");
            alarmManagerFBMA.setInexactRepeating
                    (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),interval, pendingIntentFBMA);

        }
        else {
            alarmManagerFBMA.cancel(pendingIntentFBMA);
        }
        final Intent lowBatteryIntentMA = new Intent();
        lowBatteryIntentMA.setAction(Battery_Low_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        lowBatteryIntentMA.setClass(this, AlertServiceReceiver.class);
        AlarmManager alarmManagerLBMA =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Random rSLB = new Random();
        int intent_codeSLB = rSLB.nextInt();
        PendingIntent pendingIntentLBMA;
        pendingIntentLBMA =
                PendingIntent.getBroadcast(getApplicationContext(), intent_codeSLB, lowBatteryIntentMA, 0);
        lowbatteryflag = sp.getInt("low_battery_flag_value",3);
        if (lowbatteryflag == 3){
            Log.d("fullbatteryMMMAAA","MainActivity Called");
            alarmManagerLBMA.setInexactRepeating
                    (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),interval, pendingIntentLBMA);
        }
        else {
            alarmManagerLBMA.cancel(pendingIntentLBMA);
        }
        hightempbatteryflag = sp.getInt("high_temp_flag_value",3);
        final Intent hightempbatteryIntentMA = new Intent();
        hightempbatteryIntentMA.setAction(Battery_Low_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        hightempbatteryIntentMA.setClass(this, AlertServiceReceiver.class);
        AlarmManager alarmManagerHTMA =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Random rSHT = new Random();
        int intent_codeSHT = rSHT.nextInt();
        PendingIntent pendingIntentHTMA;
        pendingIntentHTMA =
                PendingIntent.getBroadcast(getApplicationContext(), intent_codeSHT, hightempbatteryIntentMA, 0);
        if (hightempbatteryflag == 3){
            Log.d("fullbatteryMMMAAA","MainActivity Called");
            alarmManagerHTMA.setInexactRepeating
                    (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),interval, pendingIntentHTMA);
        }
        else {
            alarmManagerHTMA.cancel(pendingIntentHTMA);
        }
        getDeviceName();
        tv_version_detaile = (TextView) findViewById(R.id.tv_version_detaile);
        tv_model_detaile = (TextView) findViewById(R.id.tv_model_detaile);
        tv_capcity_detaile = (TextView) findViewById(R.id.tv_capacity_detail);
        tv_technology_detaile = (TextView) findViewById(R.id.tv_technology_detaile);
        tv_health_detaile = (TextView) findViewById(R.id.tv_health_detaile);
        tv_temperature_detaile = (TextView) findViewById(R.id.tv_temperature_detaile);
        tv_voltage_detaile = (TextView) findViewById(R.id.tv_voltage_detaile);
        tv_charger_detaile = (TextView) findViewById(R.id.tv_charger_detaile);
        tv_charging_state = (TextView) findViewById(R.id.tv_charging_state);
        tv_remaning_detaile = (TextView) findViewById(R.id.tv_remaning_detaile);
        tv_status_detaile = (TextView) findViewById(R.id.tv_status_detaile);
        tv_level_detaile = (TextView) findViewById(R.id.tv_level_detail);
        tv_build_id = (TextView) findViewById(R.id.tv_build_id);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_model = (TextView) findViewById(R.id.tv_Model);
        tv_capcity = (TextView) findViewById(R.id.tv_capacity);
        tv_technology = (TextView) findViewById(R.id.tv_technology);
        tv_health = (TextView) findViewById(R.id.tv_health);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_voltage = (TextView) findViewById(R.id.tv_voltage);
        tv_charger = (TextView) findViewById(R.id.tv_charger);
        tv_remaning = (TextView) findViewById(R.id.tv_remaning);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_level = (TextView) findViewById(R.id.tv_level);
        updatesoftwareLL =  findViewById(R.id.ADD_BTN_LAYOUT);
        updatesoftwareLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Smart+Hurricanes")));

            }
        });
       // StartAppAd.showSplash(this, savedInstanceState);
      //  playservicesLL=  findViewById(R.id.play_services);
        message = (TextView) findViewById(R.id.message);
        tv_build_id_detaile = (TextView) findViewById(R.id.tv_build_id_detaile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        dialog = new Dialog(MainActivity.this);
        message.setVisibility(View.GONE);
        alert_main_im=findViewById(R.id.alert_main_img);
        alert_main_tv=findViewById(R.id.alert_main_tv);
        tv_charging=findViewById(R.id.tv_main_flowing_current);
        tv_charging_deatail=findViewById(R.id.tv_main_flowing_current_detaile);

        tempLinearLayout=(LinearLayout) findViewById(R.id.temp_ll_main_activity);
        voltageLinearLayout=(LinearLayout) findViewById(R.id.voltage_ll_main_activity);
        settingLL=(LinearLayout) findViewById(R.id.setting_bt_main_activity);
        alertsettingLL=(LinearLayout) findViewById(R.id.alert_setting_bt_main_activity);



        temperatureCheckValue = sp.getInt("temperature_key", 5);
        voltageCheckValue = sp.getInt("voltage_key", 5);
        check_chip = check_chip(context);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            alert_main_tv.setText(R.string.status_text_view);
//            alert_main_im.setVisibility(View.GONE);
//            alertsettingLL.setEnabled(false);
//            hideItem();
//        }
//        else{
            tv_status_detaile.setVisibility(View.GONE);
            alert_main_im.setVisibility(View.VISIBLE);
            alertsettingLL.setEnabled(true);
//        }
      //  double checkca=getBatteryCapacity();
        Log.d("batterycapacity", getBatteryCapacity()+"Start sheadule work"+getBatteryCapacitynew() );
        if (getBatteryCapacitynew() > 0)
            get_battery_capicity = getBatteryCapacitynew();
        else if(getBatteryCapacity()>0)
        {
            get_battery_capicity = getBatteryCapacity();
        }
        else {
            get_battery_capicity = 1860.0;
        }
        tv_capcity_detaile.setText(get_battery_capicity+"Mah");

//        CheckLowBattery = sp.getInt("low_battery_key",1 );
//        if(!check_chip){
//            final Intent lowBatteryintent = new Intent(this, AlertService.class);
//
//            if(CheckLowBattery!=0){
//                editor = sp.edit();
//            editor.putInt("low_battery_key", 1);
//            editor.apply();
//           lowBatteryintent.putExtra("lowbattery", 5);
//            startService(lowBatteryintent);
//        }
//        }

        voltageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();


                if(voltageCheckValue==1) {

                    editor = sp.edit();
                    editor.putInt("voltage_key", 5);
                    editor.apply();
                    set_voltage(context);
                }
                else {
                    editor = sp.edit();
                    editor.putInt("voltage_key", 1);
                    editor.apply();
                    set_voltage(context);

                }

            }
        });
        tempLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();


                if(temperatureCheckValue==1) {

                    editor = sp.edit();
                    editor.putInt("temperature_key", 5);
                    editor.apply();
                    set_temperature(context);
                }
                else {
                    editor = sp.edit();
                    editor.putInt("temperature_key", 1);
                    editor.apply();
                    set_temperature(context);

                }

            }
        });

//        playservicesLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.playstore.playservices.info")));
//
//            }
//        });
      //  myBilling.checkpurchase();
        settingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {          // }
                    startActivity(new Intent(MainActivity.this, setting.class));
                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
                } catch (ActivityNotFoundException e) {

                } catch (NullPointerException e) {

                }

            }
        });

        alertsettingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = getIntent();
                try {          // }
                    // StartAppAd.showAd(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, AlertSetting.class));
                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);                    }
                catch (ActivityNotFoundException e) {

                }catch (NullPointerException c){

                }



            }
        });


        Intent intent1 = getIntent();
        if (check_admob) {
            loadNativeAd();
            Admenager admenager = new Admenager(getApplicationContext(), this);
            admenager.loadAd(getString(R.string.admobe_native_main));


           // get_InterstitialAd();
        }

        tv_model_detaile.setText(Build.MODEL);
        tv_build_id_detaile.setText(Build.ID + "");
        tv_version_detaile.setText(Build.VERSION.RELEASE + "");
        int detaile_text_size = 15;
        int text_size = 12;
        if (getResources().getDisplayMetrics().density < 2.0) {
            tv_model_detaile.setTextSize(detaile_text_size);
            tv_build_id_detaile.setTextSize(detaile_text_size);
            tv_version_detaile.setTextSize(detaile_text_size);
           // tv_status_detaile.setTextSize(detaile_text_size);
            tv_remaning_detaile.setTextSize(detaile_text_size);
            tv_level_detaile.setTextSize(detaile_text_size);
            tv_capcity_detaile.setTextSize(detaile_text_size);
            tv_voltage_detaile.setTextSize(detaile_text_size);
            tv_health_detaile.setTextSize(detaile_text_size);
            tv_temperature_detaile.setTextSize(detaile_text_size);
            tv_technology_detaile.setTextSize(detaile_text_size);
            tv_charger_detaile.setTextSize(detaile_text_size);
            tv_model_detaile.setTextSize(detaile_text_size);
            tv_build_id.setTextSize(text_size);
            tv_version.setTextSize(text_size);
           // tv_status.setTextSize(text_size);
            tv_remaning.setTextSize(text_size);
            tv_level.setTextSize(text_size);
            tv_capcity.setTextSize(text_size);
            tv_voltage.setTextSize(text_size);
            tv_health.setTextSize(text_size);
            tv_temperature.setTextSize(text_size);
            tv_technology.setTextSize(text_size);
            tv_charger.setTextSize(text_size);
            tv_charging_state.setTextSize(25);
            tv_model.setTextSize(text_size);
            alert_main_tv.setTextSize(text_size);
            tv_charging.setTextSize(text_size);
            tv_charging_deatail.setTextSize(text_size);

        }

        //if(tool_bar != null) {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch(RuntimeException e) {
            e.printStackTrace();

        }
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        /// getSupportActionBar()//.sett

        Intent intentLevel = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intentLevel.getExtras();
        int currentLevel = bundle.getInt("level");


//        Toast.makeText(getApplicationContext(),"charging"+hour+mint,Toast.LENGTH_SHORT).show();
        chargingTimevalue = sp.getInt("charging_time_key", 5);
        // Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
        check_chip = sp.getBoolean("check_chip", false);
        if (check_chip&& isConnected(context) && currentLevel < 100 && (chargingTimevalue == 0)) {
            //
            Log.d("batterycapacity", "Start sheadule"+check_chip);

            get_charging_time(context);
            try {
                set_alart_box();
            } catch (Exception e) {
            e.printStackTrace();
        }
        }
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
       // context=this;
        if (check_admob) {
//loadAd();

        } else {

            Menu menuNav=navigationView.getMenu();
            MenuItem nav_item2 = menuNav.findItem(R.id.action_pro_ico);
            nav_item2.setEnabled(false);
            nav_item2.setTitle("Ads removed");
            nav_item2.setIcon(R.drawable.action_pro_block);
            //Toast.makeText(getApplicationContext(),"you have purchased",Toast.LENGTH_SHORT).show();

        }
//        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.action_setting:
//                        if (mInterstitialAd.isLoaded()&&check_admob ) {
//                            mInterstitialAd.show();
//
//                        }else {
//
//                        } mInterstitialAd.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            startActivity(new Intent(MainActivity.this, setting.class));
//                            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                            load_InterstitialAd();
//                            super.onAdClosed();
//                        }
//                    });
                        startActivity(new Intent(MainActivity.this, setting.class));
                        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
                        return true;
//            case R.id.action_pro_ico:
//
//                return true;
                    case R.id.action_share:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Ampere meter");
                        i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=zaka.com.amperemeter");

                        //i.putExtra(Intent.EXTRA_TEXT   , "body ");
                        try {
                            startActivity(Intent.createChooser(i, "Share"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.action_rate:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zaka.com.amperemeter")));
                        return true;
                    case R.id.about_us:
                        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_alert_setting:

                        startActivity(new Intent(MainActivity.this , AlertSetting.class));
                        overridePendingTransition(R.anim.rotate_in,R.anim.rotate_out);
                        return true;
                    case R.id.action_pro_ico:
                        AlertDialog alertDialog;
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Amprer Meter");
                        alertDialogBuilder.setMessage("Would you like to disable ads?");
                        alertDialog = alertDialogBuilder.create();

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MainActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                                myBilling.purchaseRemoveAds();
                                startPurchase();
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });

                        alertDialogBuilder.show();
                        return true;
//                case R.id.action_more:
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=BheraApps")));
                    default:
                        return true;
                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        //Setting tabs from adpater
        // tabLayout.setTabsFromPagerAdapter(adapter);
    }
    public void set_voltage(Context context)
    {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        voltageCheckValue = sp.getInt("voltage_key", 5);
        double mile_voltage = bundle.getInt("voltage");
        double voltage = mile_voltage / 1000;

        // Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
        if (voltageCheckValue == 1) {
            tv_voltage_detaile.setText(mile_voltage + " mV");


        } else {

            tv_voltage_detaile.setText(voltage + " V");
        }
    }
    public void set_temperature(Context context)
    {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        int tempC = bundle.getInt("temperature") / 10;
        double tempF = tempC * 1.8 + 32;
        temperatureCheckValue = sp.getInt("temperature_key", 5);
        // Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
        if (temperatureCheckValue == 1) {
            tv_temperature_detaile.setText(((int) tempF) + "\u00b0" + "F");


        } else {

            tv_temperature_detaile.setText(bundle.getInt("temperature") / 10 + " \u2103");
        }
    }
    public  boolean check_chip(Context context){
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        // Toast.makeText(getApplicationContext(),""+bundle.getInt("current_now"),Toast.LENGTH_SHORT).show();
        if (bundle.getInt("current_now") <= 290) {
            editor = sp.edit();
            editor.putBoolean("check_chip", false);
            editor.apply();
            Log.d("batterycapacity", "Start sheadule work"+bundle.getInt("current_now") );


            return  false;

        }
        else{
            editor = sp.edit();
            editor.putBoolean("check_chip", true);
            editor.apply();
            Log.d("batterycapacity", "Start sheadule"+bundle.getInt("current_now") );

            return true;
        }

    }
    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public void set_alart_box() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);


        TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
        TextView crossDialogTextView = (TextView) dialog.findViewById(R.id.cross_text_view);
        crossDialogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_dialog.setText(hour + " " + getString(R.string.hour) + " " + mint + " " + getString(R.string.mint));


        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

//
        Button declineButton = (Button) dialog.findViewById(R.id.bt_dialog);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }

    public double getBatteryCapacitynew() {
        Object newInstance;
        double doubleValue;
        try {
            newInstance = Class.forName("com.android.internal.os.PowerProfile").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this});
        } catch (Exception e) {
            e.printStackTrace();
            newInstance = null;
        }
        try {
            doubleValue = ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getAveragePower", new Class[]{String.class}).invoke(newInstance, new Object[]{"battery.capacity"})).doubleValue();
        } catch (Exception e2) {
           // e2.printStackTrace();
            doubleValue = 0.0;
        }
        return  doubleValue;
    }


    public double getBatteryCapacity() {
        Object mPowerProfile_ = null;
        double batteryCapacity = 0;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity;
    }

    //    @Override
//    public void onBackPressed() {
//        unregisterReceiver(BatteryInfo);
//
////if(check_admob) {
//      // mAdView.pause();
//
////tt.cancel();
//      //  t.cancel();
////}
//        finish();
//       // super.onBackPressed();
//    }
//
//    @Override
//    protected void onStop() {
////        if (check_admob) {
////
////            tt.cancel();
////            t.cancel();
////            if (interstitialAd != null) {
////                interstitialAd.destroy();
////            }
////        }
//           // Toast.makeText(getApplicationContext(),"onStop",Toast.LENGTH_SHORT).show();
//
////            mAdView.pause();
////        }
//        //  tt.cancel();
//        // t.cancel();
//        //finish();
//       //  Toast.makeText(getApplicationContext(),"onStop",Toast.LENGTH_SHORT).show();
//        //Log.e("on stop","on stop");
//        super.onStop();
////put your logic here
//    }


    @Override
    protected void onResume() {
        super.onResume();
     //   mAdView.resume();


    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }



    public void get_charging_time(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent.getExtras();
        if (bundle.getInt("current_now") <= 0) {
            if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_AC)
                current_now = 800;
            else if (bundle.getInt("plugged") == BatteryManager.BATTERY_PLUGGED_USB) {
                current_now = 400;
            }
        } else {
            current_now = bundle.getInt("current_now");


        }
//          Toast.makeText(MainActivity.this, current_now + " mah",
//                Toast.LENGTH_LONG).show();
        if (getBatteryCapacitynew() > 0)
            get_battery_capicity = getBatteryCapacitynew();
      else if(getBatteryCapacity()>0)
        {
            get_battery_capicity = getBatteryCapacity();
        }
        else {
            get_battery_capicity = 1860.0;
        }

        //Toast.makeText(MainActivity.this, get_battery_capicity + "  " + current_now + " mah",
        //      Toast.LENGTH_LONG).show();
        get_charging_time = get_battery_capicity / current_now;
        Double convert_hour_to_mint = get_charging_time * 60;
        Double find_charging_time_per_scale = convert_hour_to_mint / bundle.getInt("scale");
        Double find_charging_time_of_chrged_leavel = find_charging_time_per_scale * bundle.getInt("level");
        Double remaining_mint = convert_hour_to_mint - find_charging_time_of_chrged_leavel;
        if (remaining_mint < 60) {
            mint = remaining_mint.intValue();
            // Toast.makeText(MainActivity.this, remaining_mint + "  1" + hour + " " + mint + " mah",
            //       Toast.LENGTH_LONG).show();
        } else {
            // String [] string_for_deviding_value=String.valueOf(convert_mint_to_hour).split(".");

            hour = (int) (remaining_mint / 60);
            mint = (int) (remaining_mint % 60);
            //Toast.makeText(MainActivity.this, remaining_mint + "  2" + hour + " " + mint + " mah",
            //      Toast.LENGTH_LONG).show();
        }
        // Double convert_mint_to_hour = remaining_mint / 60;
        // String string_for_con_value=String.valueOf(convert_mint_to_hour);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timerStop1 = sdf.format(cal.getTime());
        String[] timef = timerStop1.split(":");

        int current_hour = Integer.parseInt(timef[0]) + hour;
        int current_minute = Integer.parseInt(timef[1]) + mint;
        DecimalFormat formatter = new DecimalFormat("00");
        String mint_Formatted = formatter.format(current_minute);
        String hour_aFormatted = formatter.format(current_hour);
        //Log.e("current_hour",timerStop1+current_hour+current_minute+"");

        if (current_minute < 60) {

            if (hour == 0) {
                // tv_end_remaining_time.setText("(" + mint + " minutes)");
                //tv_end_time.setText(hour_aFormatted + ":" + mint_Formatted);
            } else {
                remaning_mint_main = mint;
                remaninh_hour_main = hour;
                // tv_end_remaining_time.setText("(" + hour + "hours and " + mint + " minutes)");
                // tv_end_time.setText(hour_aFormatted + ":" + mint_Formatted);
            }

        } else {
            current_hour = current_hour + 01;
            current_minute = current_minute - 60;
            mint_Formatted = formatter.format(current_minute);
            hour_aFormatted = formatter.format(current_hour);
            remaning_mint_main = mint;
            remaninh_hour_main = hour;

        }
        // Toast.makeText(getApplicationContext(),""+remaninh_hour_main+remaning_mint_main,Toast.LENGTH_SHORT).show();
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.action_setting:
//                if (mInterstitialAd.isLoaded()&&check_admob) {
//                    mInterstitialAd.show();
//
//                }else {
//                    startActivity(new Intent(MainActivity.this, setting.class));
//                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                } mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    startActivity(new Intent(MainActivity.this, setting.class));
//                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                    load_InterstitialAd();
//                    super.onAdClosed();
//                }
//            });
                startActivity(new Intent(MainActivity.this, setting.class));
                overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
                return true;
//            case R.id.action_pro_ico:
//
//                return true;
            case R.id.action_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Ampere meter");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=zaka.com.amperemeter");

                //i.putExtra(Intent.EXTRA_TEXT   , "body ");
                try {
                    startActivity(Intent.createChooser(i, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zaka.com.amperemeter")));
                return true;
            case R.id.action_pro_ico:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Amprer Meter");
                alertDialogBuilder.setMessage("Would you like to disable ads?");
                AlertDialog   alertDialog = alertDialogBuilder.create();

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                        myBilling.purchaseRemoveAds();

                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });

                alertDialogBuilder.show();
                return true;
            case R.id.about_us:
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            case R.id.PrivacyPolicy:
               // startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/amperemeter/priacypolicy")));
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    //code of inpurchase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        myBilling.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void onDestroy() {
         Toast.makeText(getApplication(), "destroy", Toast.LENGTH_SHORT).show();

        unregisterReceiver(BatteryInfo);
myBilling.onDestroy();
//        alertDialog.cancel();
        finish();

        super.onDestroy();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // menu.findItem(R.id.action_pro_ico).setEnabled(false);

        if (check_admob) {
            // Toast.makeText(getApplication(), "yes", Toast.LENGTH_SHORT).show();

        } else {
            // Toast.makeText(getApplication(), "not", Toast.LENGTH_SHORT).show();

            MenuItem menu_item=  menu.findItem(R.id.action_pro_ico);

            menu_item.setEnabled(false);
            menu_item.setIcon(R.drawable.action_pro_block);
            // You can also use something like:
            // menu.findItem(R.id.example_foobar).setEnabled(false);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            MenuItem menu_item=  menu.findItem(R.id.action_alert_setting);
          //  menu_item.setVisible(false);
        }
        return true;
    }




//    public void facebookadshow(){
//
//        if(interstitialAd.isAdLoaded()) {                         // startActivity(intent);
//            interstitialAd.show();
//
//            interstitialAd.setAdListener(new InterstitialAdListener() {
//                @Override
//                public void onInterstitialDisplayed(Ad ad) {
//                    // Interstitial ad displayed callback
//                    //   startActivity(intent);
//
//                    Log.e(TAG, "Interstitial ad displayed.");
//                }
//
//                @Override
//                public void onInterstitialDismissed(Ad ad) {
//                    // Interstitial dismissed callback
//                    Log.e(TAG, "Interstitial ad dismissed.");
//
//                    try {          // }
//                        startActivity(new Intent(MainActivity.this, setting.class));
//                        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                    } catch (ActivityNotFoundException e) {
//
//                    } catch (NullPointerException e) {
//
//                    }
//
//
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    // Ad error callback
//                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//
//                    Intent intent = new Intent(MainActivity.this,setting.class);
//                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        startActivity(intent);
//
//                    } catch (ActivityNotFoundException e) {
//
//                    } catch (NullPointerException e) {
//
//                    }
//
//                }
//
//
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//
//                    // Interstitial ad is loaded and ready to be displayed
//                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                    // Show the ad
//                    // interstitialAd.show();
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    //  startActivity(intent);
//
//                    // Ad clicked callback
//                    Log.d(TAG, "Interstitial ad clicked!");
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    // Ad impression logged callback
//                    // startActivity(intent);
//
//                    Log.d(TAG, "Interstitial ad impression logged!");
//                }
//
//            });
//        }else {
//            Intent intent = new Intent(MainActivity.this,setting.class);
//            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            try {
//                startActivity(intent);
//
//            } catch (ActivityNotFoundException e) {
//
//            } catch (NullPointerException e) {
//
//            }
//        }
//        load_InterstitialAd();
//    }
//    public void facebookadshow_more_tips(){
//
//
//        if(moreTipsinterstitialAd.isAdLoaded()) {                         // startActivity(intent);
//            moreTipsinterstitialAd.show();
//
//            moreTipsinterstitialAd.setAdListener(new InterstitialAdListener() {
//                @Override
//                public void onInterstitialDisplayed(Ad ad) {
//                    // Interstitial ad displayed callback
//                    //   startActivity(intent);
//
//                    Log.e(TAG, "Interstitial ad displayed.");
//                }
//
//                @Override
//                public void onInterstitialDismissed(Ad ad) {
//                    // Interstitial dismissed callback
//                    Log.e(TAG, "Interstitial ad dismissed.");
//
//                    try {          // }
//                        startActivity(new Intent(MainActivity.this, AlertSetting.class));
//                        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);                    } catch (ActivityNotFoundException e) {
//
//                    }
//
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    // Ad error callback
//                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//                    Intent intent = new Intent(MainActivity.this, AlertSetting.class);
//                    overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        startActivity(intent);
//
//                    }catch(ActivityNotFoundException e){
//
//                    }
//
//                }
//
//
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//
//                    // Interstitial ad is loaded and ready to be displayed
//                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                    // Show the ad
//                    // interstitialAd.show();
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    //  startActivity(intent);
//
//                    // Ad clicked callback
//                    Log.d(TAG, "Interstitial ad clicked!");
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    // Ad impression logged callback
//                    // startActivity(intent);
//
//                    Log.d(TAG, "Interstitial ad impression logged!");
//                }
//
//            });
//        }else {
//            Intent intent = new Intent(MainActivity.this, AlertSetting.class);
//            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            try {
//                startActivity(intent);
//
//            } catch (ActivityNotFoundException e) {
//
//            }
//        }
//        load_alert_InterstitialAd();
//    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.action_alert_setting).setVisible(false);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction(Battery_Power_Connected_Intent);
        intentFilter.addAction(Battery_Full_Intent);
        intentFilter.addAction(Battery_Power_Disconnected_Intent);
        intentFilter.addAction(Battery_High_Temperature_Intent);
        intentFilter.addAction(Battery_Low_Intent);
        registerReceiver(alertServiceReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(alertServiceReceiver);
    }
//public void get_InterstitialAd() {
//
//    final Handler handler = new Handler();
//
//    try {
//        tt = new TimerTask() {
//
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        //AdRequest adRequest1 = new AdRequest.Builder().build();
//                        try {
//                            interstitial_Ad.setOnAdLoadedCallback(new OnAdLoaded() {
//                                @Override
//                                public void adLoaded(String s) {
//                                    interstitial_Ad.showAd();
//                                    interstitial_Ad.loadAd();
//                                }
//
//                            });
//
//                            //ad_view();
//                        } catch (NullPointerException e) {
//
//                        }
//                    }
//                });
//
//            }
//        };
//
//        t = new Timer();
//        t.scheduleAtFixedRate(tt, 9000, 30000);
//    } catch (NullPointerException e) {
//
//    }
//

//}



    NativeAd mNativeAd;
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder exitBuilderDialog = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.exit_layout, null);

        Button yesBtm = dialogView.findViewById(R.id.id_exit_pos_btm);
        Button noBtm= dialogView.findViewById(R.id.id_exit_neg_btm);
        TextView txtTitle = dialogView.findViewById(R.id.id_exit_title);
        TextView txtMessage = dialogView.findViewById(R.id.id_exit_message);
        TemplateView template = dialogView.findViewById(R.id.my_template);

        template.setVisibility(View.GONE);

        if(mNativeAd!=null) {
            NativeTemplateStyle styles = new
                    NativeTemplateStyle.Builder().build();


            template.setStyles(styles);
            template.setVisibility(View.VISIBLE);
            template.setNativeAd(mNativeAd);
        }



        txtTitle.setText("Exit");
        txtMessage.setText("Do you want to exit?");

        exitBuilderDialog.setView(dialogView);
        final AlertDialog alertDialog = exitBuilderDialog.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

        yesBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentExit = new Intent(Intent.ACTION_MAIN);
                intentExit.addCategory(Intent.CATEGORY_HOME);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentExit);
                sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                editor = sp.edit();

                editor.putInt("resume_check", 0);

                editor.apply();
                finish();
                System.exit(0);
            }
        });

        noBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNativeAd!=null){
                    mNativeAd.destroy();
                }
                if(check_admob){
                    loadNativeAd();
                }

                alertDialog.cancel();
            }
        });

    }

    private void loadNativeAd(){
        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, getString(R.string.admobe_native_back))

                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        Log.d(TAG, "Native Ad Loaded");


                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.d(TAG, "Native Ad Destroyed");
                            return;
                        }

                        mNativeAd=nativeAd;

                    }
                })

                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d(TAG, "Native Ad Failed To Load");

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

}




