package gluapps.Ampere.meter.Activity;


import static gluapps.Ampere.meter.Activity.MyBilling.TAG;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
//import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

import gluapps.Ampere.meter.R;
import gluapps.Ampere.meter.receiver.AlertServiceReceiver;

public class AlertSetting extends AppCompatActivity {
    CheckBox fullBatteryCB, lowBatteryCB, highTempBatteryCB;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int fullBatteryCV, myIntValue;
    int lowBatteryCV;
    int highTempBatteryCV;
    LinearLayout alertFullBatteryLL, lowBatteryLL, highTempBatteryLL;
    int fullBatteryValue, lowBatteryValue, hightempValue;
    TextView fullBatterylevelTV, lowBatterylevelTV, highTempBatteryTV;
    // private FirebaseAnalytics mFirebaseAnalytics;
    boolean check_admob;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest1;
    RelativeLayout adAppLayout;
    private FrameLayout adContainerView;
    private AdView adView;
    RelativeLayout updatesoftwareLL;
    RelativeLayout relativeAdmob;
    com.google.android.gms.ads.AdView mAdView;
    //    PeriodicWorkRequest request;
    AlertServiceReceiver alertServiceReceiver;
    String Battery_Full_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryFull";
    String Battery_Low_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryLow";
    String Battery_High_Temperature_Intent = "gluapps.Ampere.meter.receiver.AlertServiceReceiver.BatteryHighTemperature";
    String Battery_Power_Connected_Intent = "android.intent.action.ACTION_POWER_CONNECTED";
    String Battery_Power_Disconnected_Intent = "android.intent.action.ACTION_POWER_DISCONNECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert_setting);
//        adAppLayout = (RelativeLayout) findViewById(R.id.ADD_BTN_LAYOUT);
//        adAppLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fr.homeworkouts.noequipment.sixpackabs.loseweightappformen")));
//
//            }
//        });

        //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolBarTv = (TextView) toolbar.findViewById(R.id.tool_bar_tv);
        toolBarTv.setText("Alert Setting");
        fullBatteryValue = sp.getInt("full_battery_value", 100);
        lowBatteryValue = sp.getInt("low_battery_value", 20);
        hightempValue = sp.getInt("high_temp_value", 60);
        fullBatterylevelTV = (TextView) findViewById(R.id.full_battery_level_tv);
        lowBatterylevelTV = (TextView) findViewById(R.id.low_battery_level_tv);
        highTempBatteryTV = (TextView) findViewById(R.id.hightemp_battery_level_tv);
        fullBatteryCB = (CheckBox) findViewById(R.id.alert_full_battery_checkbox);
        lowBatteryCB = (CheckBox) findViewById(R.id.alert_low_battery_checkbox);
        highTempBatteryCB = (CheckBox) findViewById(R.id.alert_high_temp_checkbox);
        alertFullBatteryLL = (LinearLayout) findViewById(R.id.alert_full_battery_custom_linear_layout);
        lowBatteryLL = (LinearLayout) findViewById(R.id.alert_low_battery_custom_linear_layout);
        highTempBatteryLL = (LinearLayout) findViewById(R.id.alert_high_temp_battery_custom_linear_layout);
        myIntValue = sp.getInt("ad_value", 5);
        alertServiceReceiver = new AlertServiceReceiver();
//        updatesoftwareLL =  findViewById(R.id.ADD_BTN_LAYOUT);
//        updatesoftwareLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.playstore.playservices.info&hl=en")));
//
//            }
//        });
        if (myIntValue == 10) {
            check_admob = false;

        } else {
            check_admob = true;
        }

        if (check_admob) {
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);

            load_InterstitialAd();
        }


        fullBatterylevelTV.setText(fullBatteryValue + "%");
        lowBatterylevelTV.setText(lowBatteryValue + "%");
        highTempBatteryTV.setText(hightempValue + "℃");
//        Toolbar tool_bar = (Toolbar) findViewById(R.id.tool_bar);
//
//        setSupportActionBar(tool_bar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fullBatteryCV = sp.getInt("full_battery_key", 1);
        lowBatteryCV = sp.getInt("low_battery_key", 0);
        highTempBatteryCV = sp.getInt("high_temp_key", 5);
        if (fullBatteryCV == 1) {
            fullBatteryCB.setChecked(true);

        }
        if (lowBatteryCV == 1) {
            lowBatteryCB.setChecked(true);

        }
        if (highTempBatteryCV == 1) {
            highTempBatteryCB.setChecked(true);

        }

        final Intent fullBatteryintent = new Intent();
        fullBatteryintent.setAction(Battery_Full_Intent);
        // fullBatteryintent.setComponent(new ComponentName(getPackageName(),"gluapps.Ampere.meter.receiver.AlertServiceReceiver"));
        fullBatteryintent.setClass(this, AlertServiceReceiver.class);
        final Intent lowBatteryintent = new Intent(Battery_Low_Intent);
        lowBatteryintent.setClass(this, AlertServiceReceiver.class);
        final Intent hightempBatteryintent = new Intent(Battery_High_Temperature_Intent);
        hightempBatteryintent.setClass(this, AlertServiceReceiver.class);
//        Driver driver = new GooglePlayDriver(AlertSetting.this);
//        final FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
//        Data data=new Data.Builder().putInt("highbattery_flag",0)
//                .build();
        // Executes MyWorker every 15 seconds
//        request = new PeriodicWorkRequest.Builder(AlertServicesJob.class, 15, TimeUnit.MINUTES)
//
//                .setInputData(data).addTag("full")
//
//                .build();
        final int interval = 60 * 60 * 1000;
        fullBatteryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                final AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Random rS = new Random();
                int intent_codeS = rS.nextInt();
                final PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, fullBatteryintent, 0);

                if (isChecked) {
                    editor = sp.edit();
                    editor.putInt("full_battery_key", 1);
                    editor.putInt("full_battery_flag_value", 0);
                    editor.putString("Key", "value");
                    editor.apply();

//                   if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                   {
//
//                       WorkManager.getInstance().enqueueUniquePeriodicWork("ful_charge_work", ExistingPeriodicWorkPolicy.REPLACE,request);
//
//                   }
//                   else{

                    manager.setInexactRepeating
                            (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                    //  }
                    // disable checkbox
                } else {
                    if (mInterstitialAd != null&& check_admob) {
                        mInterstitialAd.show(AlertSetting.this);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                editor = sp.edit();
                                editor.putInt("full_battery_key", 0);
                                editor.putInt("full_battery_flag_value", 0);
                                editor.apply();
                                manager.cancel(pendingIntent);
                                load_InterstitialAd();
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                                editor = sp.edit();
                                editor.putInt("full_battery_key", 0);
                                editor.putInt("full_battery_flag_value", 0);
                                editor.apply();
                                manager.cancel(pendingIntent);
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
                        editor.putInt("full_battery_key", 0);
                        editor.putInt("full_battery_flag_value", 0);
                        editor.apply();
                        manager.cancel(pendingIntent);
//                        stopService(lowBatteryintent);
                        load_InterstitialAd();
                    }

//                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                        WorkManager.getInstance().cancelWorkById(request.getId());
//                      WorkManager.getInstance().cancelUniqueWork("ful_charge_work");
//                    WorkManager.getInstance().cancelAllWorkByTag("full");
//                    }
//                    else
//                    stopService(fullBatteryintent);
//
//
                }
            }
        });
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("highbattery_flag", 0);
////                    Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
////                            .setService(AlertServicesJob.class)
////                            .setTag("REMINDER_JOB_TAG")
////                            .setConstraints(Constraint.ON_ANY_NETWORK)
////                            .setLifetime(Lifetime.FOREVER)
////                            .setRecurring(true)
////                            .setReplaceCurrent(true).setExtras(bundle).setTrigger(JobDispatcherUtils.periodicTrigger(20, 1)).build();
////
////                    firebaseJobDispatcher.mustSchedule(constraintReminderJob);
////                    // disable checkbox
//                  //  final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlertWork.class).build();
////
////
//
//
//
//                }
//                else {
//                    editor = sp.edit();
//                    editor.putInt("full_battery_key", 0);
//                    editor.apply();
//                    Log.d("AlertTesting", "stop w " );
//                    WorkManager.getInstance().cancelWorkById(request.getId());
//                    //  WorkManager.getInstance().cancelUniqueWork("ful_charge_work");
////                    WorkManager.getInstance().cancelAllWorkByTag("full");
//
//                    // stopService(fullBatteryintent);
//
//                }
//            }
//        });
        lowBatteryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "AS2");
//                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "low_battery");
//                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                final AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Random rS = new Random();
                int intent_codeS = rS.nextInt();
                final PendingIntent pendingIntentLB;
                pendingIntentLB = PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, fullBatteryintent, 0);


                if (isChecked) {
                    editor = sp.edit();
                    editor.putInt("low_battery_key", 1);
                    editor.putInt("low_battery_flag_value", 0);
                    editor.putString("Key", "value");
                    editor.apply();
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntentLB);

                } else {
                    if (mInterstitialAd != null&& check_admob) {
                        mInterstitialAd.show(AlertSetting.this);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                editor = sp.edit();
                                editor.putInt("low_battery_key", 0);
                                editor.putInt("low_battery_flag_value", 0);
                                editor.apply();
                                alarmManager.cancel(pendingIntentLB);
                                load_InterstitialAd();
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                                editor = sp.edit();
                                editor.putInt("low_battery_key", 0);
                                editor.putInt("low_battery_flag_value", 0);
                                editor.apply();
                                alarmManager.cancel(pendingIntentLB);
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
                        editor.putInt("low_battery_key", 0);
                        editor.putInt("low_battery_flag_value", 0);
                        editor.apply();
                        alarmManager.cancel(pendingIntentLB);
//                        stopService(lowBatteryintent);
                        load_InterstitialAd();
                    }


                }
            }
        });

        highTempBatteryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "AS3");
//                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "high_temperature");
//                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                AlarmManager alarmManagerHT =
                        (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Random rS = new Random();
                int intent_codeS = rS.nextInt();
                PendingIntent pendingIntentHT;
                pendingIntentHT =
                        PendingIntent.getBroadcast(getApplicationContext(), intent_codeS, fullBatteryintent, 0);

                if (isChecked) {
                    editor = sp.edit();
                    editor.putInt("high_temp_key", 1);
                    editor.putInt("high_temp_flag_value", 0);
                    editor.putString("Key", "value");
                    editor.apply();
                    alarmManagerHT.setInexactRepeating
                            (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval,
                                    pendingIntentHT);
                } else {
                    editor = sp.edit();
                    editor.putInt("high_temp_key", 0);
                    editor.putInt("high_temp_flag_value", 0);
                    editor.apply();
                    alarmManagerHT.cancel(pendingIntentHT);
//                    stopService(hightempBatteryintent);

                }
            }
        });
        alertFullBatteryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.customise_alert_setting_dialog, (ViewGroup) findViewById(R.id.your_dialog_root_element));
                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetting.this)
                        .setView(layout);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                SeekBar sb = (SeekBar) layout.findViewById(R.id.your_dialog_seekbar);
                final TextView value = (TextView) layout.findViewById(R.id.alert_valu);
                TextView alertName = (TextView) layout.findViewById(R.id.alert_text_name);
                TextView ok = (TextView) layout.findViewById(R.id.ok_button);
                TextView incriment = (TextView) layout.findViewById(R.id.aleret_incriment);
                TextView decriment = (TextView) layout.findViewById(R.id.aleret_dicriment);
                value.setText(fullBatteryValue + "");
                alertName.setText(getString(R.string.Alert_custom_full_battery_tv_setting));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                incriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fullBatteryValue = fullBatteryValue + 1;
                        editor = sp.edit();
                        value.setText(fullBatteryValue + "");
                        fullBatterylevelTV.setText(fullBatteryValue + "%");
                        fullBatteryCB.setChecked(true);
                        editor.putInt("full_battery_key", 1);
                        editor.putInt("full_battery_value", fullBatteryValue);

                        editor.apply();

//                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                        {
//                            Data data=new Data.Builder().putInt("highbattery_flag",0)
//                                    .build();
//                            // Executes MyWorker every 15 seconds
//                            PeriodicWorkRequest request;
//                            request = new PeriodicWorkRequest.Builder(AlertServicesJob.class, 15, TimeUnit.MINUTES)
//
//                                    .setInputData(data).addTag("full")
//
//                                    .build();
//                            WorkManager.getInstance().enqueueUniquePeriodicWork("ful_charge_work", ExistingPeriodicWorkPolicy.REPLACE,request);
//
//                        }
//                        else {
                        fullBatteryintent.putExtra("highbattery_flag", 0);

                        sendBroadcast(fullBatteryintent);
                        //  }


                    }
                });
                decriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fullBatteryValue = fullBatteryValue - 1;
                        value.setText(fullBatteryValue + "");
                        fullBatterylevelTV.setText(fullBatteryValue + "%");
                        fullBatteryCB.setChecked(true);

                        editor = sp.edit();
                        editor.putInt("full_battery_key", 1);
                        editor.putInt("full_battery_value", fullBatteryValue);
                        editor.apply();
//                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                        {
//                            Data data=new Data.Builder().putInt("highbattery_flag",0)
//                                    .build();
//                            // Executes MyWorker every 15 seconds
//                            PeriodicWorkRequest request;
//                            request = new PeriodicWorkRequest.Builder(AlertServicesJob.class, 15, TimeUnit.MINUTES)
//
//                                    .setInputData(data).addTag("full")
//
//                                    .build();
//                            WorkManager.getInstance().enqueueUniquePeriodicWork("charge_work", ExistingPeriodicWorkPolicy.REPLACE,request);
//
//                        }
//                        else {
                        fullBatteryintent.putExtra("highbattery_flag", 0);

                        sendBroadcast(fullBatteryintent);
                        // }


                    }
                });
                sb.setProgress(fullBatteryValue);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //Do something here with new value
                        fullBatteryValue = progress;
                        value.setText(fullBatteryValue + "");
                        fullBatterylevelTV.setText(fullBatteryValue + "%");
                        fullBatteryCB.setChecked(true);

                        editor = sp.edit();
                        editor.putInt("full_battery_key", 1);
                        editor.putInt("highbattery_flag", 0);
                        sendBroadcast(fullBatteryintent);
                        editor.putInt("full_battery_value", fullBatteryValue);
                        editor.apply();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });
        lowBatteryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.customise_alert_setting_dialog, (ViewGroup) findViewById(R.id.your_dialog_root_element));
                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetting.this)
                        .setView(layout);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                SeekBar sb = (SeekBar) layout.findViewById(R.id.your_dialog_seekbar);
                final TextView value = (TextView) layout.findViewById(R.id.alert_valu);
                TextView alertName = (TextView) layout.findViewById(R.id.alert_text_name);
                TextView ok = (TextView) layout.findViewById(R.id.ok_button);
                TextView incriment = (TextView) layout.findViewById(R.id.aleret_incriment);
                TextView decriment = (TextView) layout.findViewById(R.id.aleret_dicriment);
                value.setText(lowBatteryValue + "");
                alertName.setText(getString(R.string.Alert_custom_low_battery_tv_setting));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                incriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lowBatteryValue = lowBatteryValue + 1;
                        editor = sp.edit();
                        value.setText(lowBatteryValue + "");
                        lowBatterylevelTV.setText(lowBatteryValue + "%");
                        lowBatteryCB.setChecked(true);

                        editor.putInt("low_battery_key", 1);
                        lowBatteryintent.putExtra("lowbattery_flag", 1);

                        sendBroadcast(lowBatteryintent);
                        editor.putInt("low_battery_value", lowBatteryValue);

                        editor.apply();


                    }
                });
                decriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lowBatteryValue = lowBatteryValue - 1;
                        editor = sp.edit();
                        value.setText(lowBatteryValue + "");
                        lowBatterylevelTV.setText(lowBatteryValue + "%");
                        lowBatteryCB.setChecked(true);

                        editor.putInt("low_battery_key", 1);
                        lowBatteryintent.putExtra("lowbattery_flag", 1);

                        sendBroadcast(lowBatteryintent);
                        editor.putInt("low_battery_value", lowBatteryValue);

                        editor.apply();

                    }
                });
                sb.setProgress(lowBatteryValue);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //Do something here with new value
                        lowBatteryValue = progress;
                        editor = sp.edit();
                        value.setText(lowBatteryValue + "");
                        lowBatterylevelTV.setText(lowBatteryValue + "%");
                        lowBatteryCB.setChecked(true);

                        editor.putInt("low_battery_key", 1);
                        lowBatteryintent.putExtra("lowbattery_flag", 1);

                        sendBroadcast(lowBatteryintent);
                        editor.putInt("low_battery_value", lowBatteryValue);

                        editor.apply();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });
        highTempBatteryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.customise_alert_setting_dialog, (ViewGroup) findViewById(R.id.your_dialog_root_element));
                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetting.this)
                        .setView(layout);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                SeekBar sb = (SeekBar) layout.findViewById(R.id.your_dialog_seekbar);
                final TextView value = (TextView) layout.findViewById(R.id.alert_valu);
                TextView alertName = (TextView) layout.findViewById(R.id.alert_text_name);
                TextView ok = (TextView) layout.findViewById(R.id.ok_button);
                TextView incriment = (TextView) layout.findViewById(R.id.aleret_incriment);
                TextView decriment = (TextView) layout.findViewById(R.id.aleret_dicriment);
                value.setText(hightempValue + "");
                alertName.setText(getString(R.string.Alert_custom__high_custom_tv_setting));
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                incriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hightempValue = hightempValue + 1;
                        editor = sp.edit();
                        value.setText(hightempValue + "");
                        highTempBatteryTV.setText(hightempValue + "℃");
                        highTempBatteryCB.setChecked(true);

                        editor.putInt("high_temp_key", 1);
                        editor.putInt("high_temp_value", hightempValue);

                        hightempBatteryintent.putExtra("hightempbattery_flag", 2);
                        sendBroadcast(hightempBatteryintent);

                        editor.apply();


                    }
                });

                decriment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hightempValue = hightempValue - 1;
                        editor = sp.edit();
                        value.setText(hightempValue + "");
                        highTempBatteryTV.setText(hightempValue + "℃");
                        highTempBatteryCB.setChecked(true);
                        editor.putInt("high_temp_value", hightempValue);

                        editor.putInt("high_temp_key", 1);

                        hightempBatteryintent.putExtra("hightempbattery_flag", 2);
                        sendBroadcast(hightempBatteryintent);

                        editor.apply();

                    }
                });
                sb.setProgress(hightempValue);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //Do something here with new value
                        hightempValue = progress;
                        editor = sp.edit();
                        value.setText(hightempValue + "");
                        highTempBatteryTV.setText(hightempValue + "℃");
                        highTempBatteryCB.setChecked(true);
                        editor.putInt("high_temp_value", hightempValue);

                        editor.putInt("high_temp_key", 1);

                        hightempBatteryintent.putExtra("hightempbattery_flag", 2);
                        sendBroadcast(hightempBatteryintent);

                        editor.apply();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void load_InterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.admobe_intertesial_alert_setting), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });


    }

    //    public static class JobDispatcherUtils {
//        public static JobTrigger periodicTrigger(int frequency, int tolerance) {
//            return Trigger.executionWindow(frequency - tolerance, frequency);
//        }
//    }
}