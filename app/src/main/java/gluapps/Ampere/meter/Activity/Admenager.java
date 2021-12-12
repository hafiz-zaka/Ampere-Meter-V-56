package gluapps.Ampere.meter.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import gluapps.Ampere.meter.R;

public class Admenager {
    private Context mContext;
    private Activity sActivity;
    public Admenager(Context context, Activity activity) {
        mContext = context;
        sActivity = activity;
    }
    private static final String TAG = "--->NativeAd";
    private TemplateView template;
    void loadAd(final String adID) {
        template = sActivity.findViewById(R.id.my_template);

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader adLoader = new AdLoader.Builder(mContext, adID).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                Log.d(TAG, "Native Ad Loaded");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (sActivity.isDestroyed()) {
                        nativeAd.destroy();
                        Log.d(TAG, "Native Ad Destroyed");
                        return;
                    }
                }

                if (nativeAd.getMediaContent().hasVideoContent()) {
                    float mediaAspectRatio = nativeAd.getMediaContent().getAspectRatio();
                    float duration = nativeAd.getMediaContent().getDuration();

                    nativeAd.getMediaContent().getVideoController().setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                        @Override
                        public void onVideoStart() {
                            super.onVideoStart();
                            Log.d(TAG, "Video Started");
                        }

                        @Override
                        public void onVideoPlay() {
                            super.onVideoPlay();
                            Log.d(TAG, "Video Played");
                        }

                        @Override
                        public void onVideoPause() {
                            super.onVideoPause();
                            Log.d(TAG, "Video Paused");
                        }

                        @Override
                        public void onVideoEnd() {
                            super.onVideoEnd();
                            Log.d(TAG, "Video Finished");
                        }

                        @Override
                        public void onVideoMute(boolean b) {
                            super.onVideoMute(b);
                            Log.d(TAG, "Video Mute : " + b);
                        }
                    });
                }

                NativeTemplateStyle styles = new
                        NativeTemplateStyle.Builder().build();


                template.setStyles(styles);
                template.setVisibility(View.VISIBLE);
                template.setNativeAd(nativeAd);

            }
        })

                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d(TAG, "Native Ad Failed To Load");
                        template.setVisibility(View.GONE);

                        new CountDownTimer(10000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d(TAG, "Sec : " + millisUntilFinished / 1000);
                            }

                            @Override
                            public void onFinish() {
                                Log.d(TAG, "Reloading Native Ad");
                                loadAd(adID);
                            }
                        }.start();

                    }
                })
                // .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }
}
