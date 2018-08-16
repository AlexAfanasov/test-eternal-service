package oak.shef.ac.uk.testrunningservicesbackgroundrelaunched;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by fabio on 24/01/2016.
 */
public class TimerRestarterBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("servicePrefs", Context.MODE_PRIVATE);
        boolean serviceActivated = sharedPreferences.getBoolean("serviceActivated", false);

        if (serviceActivated) {
            // Start service here
        }


        String action = intent.getAction();

        String message = "BootDeviceReceiver onReceive, action is " + action;

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_BROADCAST_RECEIVER, action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            context.startService(new Intent(context, TimerService.class));
        }
        Log.i(TimerRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
    }

}
