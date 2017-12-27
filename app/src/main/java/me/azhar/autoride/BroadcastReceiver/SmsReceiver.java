package me.azhar.autoride.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.widget.Toast;

import me.azhar.autoride.Activity.Activity_Register;
import me.azhar.autoride.Utility.Operation;


public class SmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        String pn12 = Operation.getStringFromSharedPreference(context, "ePhoneNumbers");
        String emergencyRandom = Operation.getStringFromSharedPreference(context, "emergencyRandom");

        Bundle extras = intent.getExtras();
        if (extras == null)
            return;

        Object[] pdus = (Object[]) extras.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = SMessage.getOriginatingAddress();
            String body = SMessage.getMessageBody().toString();


            String destination = PhoneNumberUtils.compare(sender, pn12) ? pn12 : "";

            if (destination.length() > 0 && emergencyRandom.length() > 0 && String.valueOf(emergencyRandom).equals(body)) {

                Operation.SaveToSharedPreference(context, "ePhoneNumber", pn12);

                Intent intentone = new Intent(context.getApplicationContext(), Activity_Register.class);
                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentone);
                Toast.makeText(context, "Emergency number is verified", Toast.LENGTH_SHORT).show();
            }

            Intent in = new Intent("SmsMessage.intent.MAIN").putExtra("get_msg", sender + ":" + body);
            context.sendBroadcast(in);

        }


    }
}
