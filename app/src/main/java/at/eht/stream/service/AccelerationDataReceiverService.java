package at.eht.stream.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import at.eht.stream.Config;
import at.eht.stream.database.SampleBatchDAO;
import at.eht.stream.model.Sample;
import at.eht.stream.model.SampleBatch;

/**
 * @author Markus Deutsch
 */
public class AccelerationDataReceiverService extends Service {

    PebbleKit.PebbleDataReceiver accelerationReceiver;
    String LOG_TAG = "AccelerationDataReceiverService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SampleBatchDAO.init(this);
        setupAccelerationReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(accelerationReceiver);
    }

    private void setupAccelerationReceiver(){
        accelerationReceiver = new PebbleKit.PebbleDataReceiver(Config.PEBBLE_UUID) {
            @Override
            public void receiveData(Context context, int transactionId, PebbleDictionary pebbleTuples) {
                if(!pebbleTuples.contains(Config.KEY_COMMAND) || pebbleTuples.getInteger(Config.KEY_COMMAND) != Config.COMMAND_DATA){
                    Log.i(LOG_TAG, "Message with invalid/no command received.");
                    return;
                }

                PebbleKit.sendAckToPebble(context, transactionId);

                Sample[] samples = new Sample[Config.NUMBER_SAMPLES];

                for(int i = 0; i < Config.NUMBER_SAMPLES; i++){
                    Sample sample = new Sample();
                    sample.setX(pebbleTuples.getInteger(i * Config.NUMBER_PARAMETERS + 0 + 1).intValue());
                    sample.setY(pebbleTuples.getInteger(i * Config.NUMBER_PARAMETERS + 1 + 1).intValue());
                    sample.setZ(pebbleTuples.getInteger(i * Config.NUMBER_PARAMETERS + 2 + 1).intValue());
                    samples[i] = sample;
                }

                SampleBatch batch = new SampleBatch(samples);
                SampleBatchDAO.insert(batch);
            }
        };
    }
}
