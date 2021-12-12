package gluapps.Ampere.meter.Activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import android.util.Log;

//import static com.servicsbhera.moslem.namaztimer.AlarmBroadcastReceiver.prayerName;

public class SimpleJobIntentService extends JobIntentService {

    static final int JOB_ID = 1001;

    static void enqueueWork(Context context, Intent work){
        enqueueWork(context,SimpleJobIntentService.class,JOB_ID,work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    @Override
    public boolean onStopCurrentWork() {
        Log.i("SimpleJobIntentService", "Job Stopped");
        return super.onStopCurrentWork();
    }
}
