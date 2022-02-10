package gluapps.Ampere.meter.Activity;


import static gluapps.Ampere.meter.Activity.MyBilling.TAG;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

//import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

import gluapps.Ampere.meter.R;
import gluapps.Ampere.meter.receiver.AlertServiceReceiver;


/**
 * Created by zaka ullah on 3/10/2016.
 */
public class setting extends AppCompatActivity{
    LinearLayout lannguageLineatLayout,translaterLinearLayout,temperatureLinearLayout,alertSetting, aboutUsLayout;
    int myIntValue1,temperatureCheckValue,notificationCheckValue,chargingTimevalue,permanantNotificationCheckValue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    CheckBox checkBox,notificationCheckBox,chargingTimeCB,permanantNotificationCheckBox;
    String Notification_Intent = "gluapps.Ampere.meter.receiver.setting.notification";
    String Permanent_Notification_Intent = "gluapps.Ampere.meter.receiver.setting.notificationPermanent";
    TextView temperatureTV;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    AlertServiceReceiver alertServiceReceiver;
    boolean isCharging;
    boolean check_admob;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest1;
    int myIntValue;
    private InterstitialAd interstitialAd;
    Boolean check_chip;
    RelativeLayout adAppLayout;
    RelativeLayout relativeAdmob;
    com.google.android.gms.ads.AdView mAdView;
    RelativeLayout updatesoftwareLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
       // adAppLayout = (RelativeLayout) findViewById(R.id.ADD_BTN_LAYOUT);
        aboutUsLayout = findViewById(R.id.aboutus_linear_layout);
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
//        adAppLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fr.ertugruldownloader.whatsappstatussaver.videodownloader")));
//
//            }
//        });
//        updatesoftwareLL =  findViewById(R.id.ADD_BTN_LAYOUT);
//        updatesoftwareLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.check.updateandroidapp.softwareupgrade&hl=en")));
//
//            }
//        });

     //  load_InterstitialAd();
        //load_notifi_InterstitialAd();
        checkBox= (CheckBox) findViewById(R.id.open_checkbox);
        lannguageLineatLayout= (LinearLayout) findViewById(R.id.language_linear_layout);
        temperatureLinearLayout= (LinearLayout) findViewById(R.id.temperature_linear_layout);
        translaterLinearLayout=(LinearLayout) findViewById(R.id.translater_linear_layout);
        temperatureTV= (TextView) findViewById(R.id.teperature_tv_setting);
        alertSetting=(LinearLayout) findViewById(R.id.alert_bt_setting);
        notificationCheckBox= (CheckBox) findViewById(R.id.notification_checkbox_setting);
        permanantNotificationCheckBox= (CheckBox) findViewById(R.id.permanent_notification_checkbox_setting);
        chargingTimeCB= (CheckBox) findViewById(R.id.charging_time_checkbox_setting);
        mNotifyMgr = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolBarTv= (TextView)toolbar.findViewById(R.id.tool_bar_tv);
        toolBarTv.setText("Settings");
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        myIntValue1 = sp.getInt("your_int_key", 5);
        chargingTimevalue = sp.getInt("charging_time_key", 5);
        check_chip = sp.getBoolean("check_chip",false );
        alertServiceReceiver = new AlertServiceReceiver();
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
        alertSetting.setVisibility(View.VISIBLE);
//        }
       // Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
        if(myIntValue1==1) {
            checkBox.setChecked(false);

        }
        myIntValue = sp.getInt("ad_value", 5);
        if (myIntValue == 10) {
            check_admob = false;


        } else {
            check_admob = true;
            load_InterstitialAd();
        }

        if(check_admob){
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
        }



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {


                if (isChecked){


                    editor = sp.edit();
                    editor.putInt("your_int_key", 0);
                    editor.apply();
                    // disable checkbox
                }
                else {
                    if (mInterstitialAd != null&& check_admob) {
                        mInterstitialAd.show(setting.this);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                editor = sp.edit();
                                editor.putInt("your_int_key", 1);
                                editor.putInt("charging_time_key", 1);
                                chargingTimeCB.setChecked(false);
                                editor.apply();
                                load_InterstitialAd();
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                                editor = sp.edit();
                                editor.putInt("your_int_key", 1);
                                editor.putInt("charging_time_key", 1);
                                chargingTimeCB.setChecked(false);
                                editor.apply();
                                load_InterstitialAd();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }
                  else {
                        editor = sp.edit();
                        editor.putInt("your_int_key", 1);
                        editor.putInt("charging_time_key", 1);
                        chargingTimeCB.setChecked(false);
                        editor.apply();
                        load_InterstitialAd();
                    }

                    //   Toast.makeText(getApplication(), "male no", Toast.LENGTH_SHORT).show();

                }
            }
        });
        notificationCheckValue = sp.getInt("notification_key", 5);
//        if(check_chip){
//            chargingTimeCB.setChecked(true);
//            notificationCheckBox.setChecked(true);}
//            else
//        {
//            editor = sp.edit();
//            editor.putInt("notification_key", 1);
//            editor.apply();
//            notificationCheckBox.setChecked(false);
//            chargingTimeCB.setChecked(false);
//            editor = sp.edit();
//            editor.putInt("charging_time_key", 1);
//            editor.apply();
//        }
        if(notificationCheckValue==1) {

            notificationCheckBox.setChecked(true);

        }
        final Intent NotificationIntent = new Intent();
        NotificationIntent.setAction(Notification_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        NotificationIntent.setClass(this, AlertServiceReceiver.class);
        final Intent permanentNotificationInetent = new Intent();
        permanentNotificationInetent.setAction(Permanent_Notification_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        permanentNotificationInetent.setClass(this, AlertServiceReceiver.class);
        final int interval = 1000;
        notificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Random rS = new Random();
                int intent_codeS = rS.nextInt();
                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, NotificationIntent, 0);

                if (isChecked){
                    editor = sp.edit();
                    editor.putInt("notification_key", 1);
                    editor.apply();
                    Toast.makeText(getApplication(), getString(R.string.toast_hide_notificaion_message),
                            Toast.LENGTH_SHORT).show();
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis(),interval, pendingIntent);

//                    registerReceiver(BatteryInfo, new IntentFilter(
//                            Intent.ACTION_BATTERY_CHANGED));
                    // disable checkbox
                }
                else {
//                    if (mInterstitialAd.isLoaded()&&check_admob ) {
//                        mInterstitialAd.show();
//                        mInterstitialAd.setAdListener(new AdListener() {
//                            @Override
//                            public void onAdClosed() {
//                                editor = sp.edit();
//                                editor.putInt("notification_key", 0);
//                                editor.apply();
//                                mNotifyMgr.cancel(1);
//                                load_notifi_InterstitialAd();
//                                super.onAdClosed();
//                            }
//                        });
//                    }else

                        editor = sp.edit();
                        editor.putInt("notification_key", 0);
                        editor.apply();
                        manager.cancel(pendingIntent);
                        mNotifyMgr.cancel(1);
                        //load_notifi_InterstitialAd();

                }
            }
        });

        if(chargingTimevalue==1) {
            chargingTimeCB.setChecked(false);

        }
        chargingTimeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                Bundle bundle = new Bundle();


                if (isChecked){
                    editor = sp.edit();
                    editor.putInt("charging_time_key", 0);
                    editor.putInt("your_int_key", 0);
                    checkBox.setChecked(true);

                    editor.apply();
                    // disable checkbox
                }
                else {
                    editor = sp.edit();
                    editor.putInt("charging_time_key", 1);
                    editor.apply();
                    //   Toast.makeText(getApplication(), "male no", Toast.LENGTH_SHORT).show();

                }
            }
        });
        // Toast.makeText(getApplication(), "male12"+myIntValue, Toast.LENGTH_SHORT).show();
           permanantNotificationCheckValue = sp.getInt("permanant_notification_key", 5);

        if(permanantNotificationCheckValue==1) {
            permanantNotificationCheckBox.setChecked(true);

        }
        permanantNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                Bundle bundle = new Bundle();

                AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Random rS = new Random();
                int intent_codeS = rS.nextInt();
                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, permanentNotificationInetent, 0);


                if (isChecked){
                    editor = sp.edit();
                    editor.putInt("permanant_notification_key", 1);
                    editor.apply();
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),interval, pendingIntent);

//                    registerReceiver(BatteryInfo, new IntentFilter(
//                            Intent.ACTION_BATTERY_CHANGED));
                }
                else {
                    editor = sp.edit();
                    editor.putInt("permanant_notification_key", 0);
                    editor.apply();
                    manager.cancel(pendingIntent);
                    mNotifyMgr.cancel(2);

                }
            }
        });
        temperatureLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();


                temperatureCheckValue = sp.getInt("temperature_key", 5);

                if(temperatureCheckValue==1) {
                    temperatureTV.setText(getString(R.string.temperature_heading_setting)+" "+"\u00b0"+"C");
                    editor = sp.edit();
                    editor.putInt("temperature_key", 5);
                    editor.apply();
                }
                else {
                    editor = sp.edit();
                    editor.putInt("temperature_key", 1);
                    editor.apply();
                    temperatureTV.setText(getString(R.string.temperature_heading_setting)+" "+"\u00b0"+"F");

                }

            }
        });
        temperatureCheckValue = sp.getInt("temperature_key", 5);

        if(temperatureCheckValue==1) {

            temperatureTV.setText(getString(R.string.temperature_heading_setting)+" "+"\u00b0"+"F");
        }
        else {
            temperatureTV.setText(getString(R.string.temperature_heading_setting)+" "+"\u00b0"+"C");
        }
lannguageLineatLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        startActivity(new Intent(setting.this,LanguageActivity.class));
        overridePendingTransition(R.anim.rotate_in,R.anim.rotate_out);
        finish();
    }
});
        translaterLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();


                startActivity(new Intent(setting.this,TranslateFragment.class));
                overridePendingTransition(R.anim.rotate_in,R.anim.rotate_out);
                finish();
            }
        });
    alertSetting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(setting.this,AlertSetting.class));
            overridePendingTransition(R.anim.rotate_in,R.anim.rotate_out);
        }
    });
    }
    @Override
    public void onBackPressed() {
       // startActivity(new Intent(setting.this,MainActivity.class));

        super.onBackPressed();
        //unregisterReceiver(BatteryInfo);
        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
        //finish();
    }
    private BroadcastReceiver BatteryInfo = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

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
                }
                else {
                    Toast.makeText(getApplication(), getString(R.string.toast_hide_notificaion_message), Toast.LENGTH_SHORT).show();

                }
            }

        }

    };

    public void mainNoification(Context context) {
        permanantNotificationCheckValue = sp.getInt("permanant_notification_key", 5);
        Intent intent1 = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Bundle bundle = intent1.getExtras();
        int level = intent1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        boolean isPresent = intent1.getBooleanExtra("present", false);

        if (isPresent) {
            int tempC = bundle.getInt("temperature") / 10;
            double tempF = tempC * 1.8 + 32;
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
         isCharging = bundle.getInt("status") == BatteryManager.BATTERY_STATUS_CHARGING;
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
        }
        try{
        unregisterReceiver(BatteryInfo);
    }  catch (Exception e) {
        e.printStackTrace();
    }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void load_InterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.admobe_intertesial_setting), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle  the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Notification_Intent);
        intentFilter.addAction(Permanent_Notification_Intent);
        registerReceiver(alertServiceReceiver, intentFilter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(alertServiceReceiver);
    }
}
