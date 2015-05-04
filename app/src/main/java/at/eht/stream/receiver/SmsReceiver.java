package at.eht.stream.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.getpebble.android.kit.PebbleKit;

import at.eht.stream.Config;
import at.eht.stream.service.UploadService;

/**
 * @author Markus Deutsch
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        StringBuilder messageBody = new StringBuilder();

        if(intent != null && intent.getAction() != null && intent.getAction().equalsIgnoreCase(ACTION)){

            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];

            for(int i = 0; i < pduArray.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                messageBody.append(messages[i].getMessageBody());
            }

            handleSms(messageBody.toString());
        }
    }

    private void handleSms(String input){
        if(input == null) return;

        if(input.equalsIgnoreCase("PBL-UPLOAD")){
            // Start Upload
            context.startService(new Intent(context, UploadService.class));
        } else if(input.equalsIgnoreCase("PBL-GO")){
            // Start Watchapp
            PebbleKit.startAppOnPebble(context, Config.PEBBLE_UUID);
        }
    }
}
