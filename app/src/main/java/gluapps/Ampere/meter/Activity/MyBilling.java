package gluapps.Ampere.meter.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import gluapps.Ampere.meter.util.IabHelper;
import gluapps.Ampere.meter.util.IabResult;
import gluapps.Ampere.meter.util.Inventory;
import gluapps.Ampere.meter.util.Purchase;
import gluapps.Ampere.meter.Activity.Constants;

import static com.google.ads.AdRequest.LOGTAG;

/**
 * Created by zadeveloper on 4/28/2018.
 */

public class MyBilling {
    int myIntValue = 0;
    // Debug tag, for logging
        static final String TAG = "gluapps_billing";

        static final String SKU_REMOVE_ADS = "com.ampere.ad";

        // (arbitrary) request code for the purchase flow
        static final int RC_REQUEST = 10001;

        Activity activity;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

        // The helper object
        IabHelper mHelper;

        String base64EncodedPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjljzYnm95do8a7+P3TPV9D0gGL4zsTwUdTCurH5Pe6PfAlTfCCpaNHFpW9poGcpKsAajEdQ6hITYrnAmcyZLvtzb8zvqGtTMpyJT6w8Pn8eIesSr7Fue2l7fNKIhswHXp+AKJlXJQXpTsGKhCwd7Z4pW2xb/JoeRsfFbYRFiryXmaJDf8mxhrtAExepp+NYd/ey2aoMZaXxUA95zfSZ8RtIABnZSzWHbD6t5n/W1wRER9OBhsZ29vWKvx2VNr69rFRiYvjckgA4t+q5rmVJ3+hUZ3PM9nayGFQMM5QYzN4ZEh5AYpuX3E/tINgnT4ofEO8+jWUpQ9bIHEdE1opdtwwIDAQAB";
        Boolean isAdsDisabled = false;
        String payload = "ANY_PAYLOAD_STRING";

        public MyBilling(Activity launcher) {
            this.activity = launcher;
        }

        public void onCreate() {
            sp = activity.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
            // Create the helper, passing it our context and the public key to
            // verify signatures with
            Log.d(TAG, "Creating IAB helper.");
            mHelper = new IabHelper(activity, base64EncodedPublicKey);

            // enable debug logging (for a production application, you should set
            // this to false).
            mHelper.enableDebugLogging(false);

            // Start setup. This is asynchronous and the specified listener
            // will be called once setup completes.
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        // complain("Problem setting up in-app billing: " + result);
                        return;
                    }

                    // Have we been disposed off in the meantime? If so, quit.
                    if (mHelper == null)
                        return;

                    // IAB is fully set up. Now, let's get an inventory of stuff we
                    // own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // Listener that's called when we finish querying the items and
        // subscriptions we own
        IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {
                Log.d(TAG, "Query inventory finished.");

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null)
                    return;

                // Is it a failure?
                if (result.isFailure()) {
                    // complain("Failed to query inventory: " + result);
                    return;
                }

                Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

                // Do we have the premium upgrade?
                Purchase removeAdsPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
                if(removeAdsPurchase != null && verifyDeveloperPayload(removeAdsPurchase)){
               // Toast.makeText(activity,""+ Constants.isAdsDisabled, Toast.LENGTH_SHORT).show();
                    editor = sp.edit();
                    editor.putInt("ad_value", 10);
                    editor.apply();
                    Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                    //removeAds("not per");
                }


            }
        };

        // User clicked the "Remove Ads" button.
        public void purchaseRemoveAds() {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    try {
                        mHelper.launchPurchaseFlow(activity, SKU_REMOVE_ADS,
                                RC_REQUEST, mPurchaseFinishedListener, payload);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }catch(IllegalStateException e){
                        e.printStackTrace();

                    }catch(NullPointerException e){
                        e.printStackTrace();

                    }

                }
            });
        }

        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
                    + data);
            if (mHelper == null)
                return true;

            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...
                return false;
            } else {

                Log.d(TAG, "onActivityResult handled by IABUtil.");

                return true;
            }

        }

        /** Verifies the developer payload of a purchase. */
        boolean verifyDeveloperPayload(Purchase p) {
            String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct.
         * It will be the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase
         * and verifying it here might seem like a good approach, but this will
         * fail in the case where the user purchases an item on one device and
         * then uses your app on a different device, because on the other device
         * you will not have access to the random string you originally
         * generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different
         * between them, so that one user's purchase can't be replayed to
         * another user.
         *
         * 2. The payload must be such that you can verify it even when the app
         * wasn't the one who initiated the purchase flow (so that items
         * purchased by the user on one device work on other devices owned by
         * the user).
         *
         * Using your own server to store and verify developer payloads across
         * app installations is recommended.
         */
            return true;
        }

        // Callback for when a purchase is finished
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                        + purchase);

                // if we were disposed of in the meantime, quit.
                if (mHelper == null)
                    return;

                if (result.isFailure()) {
                    complain("Error purchasing: " + result);
                    return;
                }
                if (!verifyDeveloperPayload(purchase)) {
                    complain("Error purchasing. Authenticity verification failed.");
                    return;
                }

                Log.d(TAG, "Purchase successful.");

                if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
                    // bought the premium upgrade!
                    editor = sp.edit();
                    editor.putInt("ad_value", 10);
                    editor.apply();

                        Intent i = activity.getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.finish();
                        activity. startActivity(i);
//                    removeAds("permiaum");

                }
            }
        };

        private void removeAds(String check) {
                        Toast.makeText(activity,""+check, Toast.LENGTH_SHORT).show();
            isAdsDisabled = true;
            myIntValue = sp.getInt("ad_value", 5);
if(myIntValue==5){
    Intent i = activity.getBaseContext().getPackageManager()
            .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    activity.finish();
    activity. startActivity(i);
}
            editor = sp.edit();
            editor.putInt("ad_value", 10);
            editor.apply();
            if(check=="permiaum"){
                Intent i = activity.getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              activity.finish();
               activity. startActivity(i);

            }
        }

        // We're being destroyed. It's important to dispose of the helper here!

        public void onDestroy() {

            // very important:
            Log.d(TAG, "Destroying helper.");
            if (mHelper != null) {
                try {
                    mHelper.dispose();
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }catch(IllegalStateException e) {
                    Log.i(LOGTAG, "IllegalStateException on exit");
                } finally {
                }
                mHelper = null;
            }
        }

        void complain(String message) {
            Log.e(TAG, "**** TrivialDrive Error: " + message);
            alert("Error: " + message);
        }

        void alert(final String message) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    AlertDialog.Builder bld = new AlertDialog.Builder(activity);
                    bld.setMessage(message);
                    bld.setNeutralButton("OK", null);
                    Log.d(TAG, "Showing alert dialog: " + message);
                    bld.create().show();
                }
            });
        }

}
