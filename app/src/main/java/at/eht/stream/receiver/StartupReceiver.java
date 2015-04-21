package at.eht.stream.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import at.eht.stream.service.AccelerationDataReceiverService;

/**
 * @author Markus Deutsch
 */
public class StartupReceiver extends BroadcastReceiver {
    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent pebbleServiceIntent = new Intent(context, AccelerationDataReceiverService.class);
        context.startService(pebbleServiceIntent);
    }
}