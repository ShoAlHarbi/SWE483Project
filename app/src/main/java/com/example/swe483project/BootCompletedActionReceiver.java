package com.example.swe483project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompletedActionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "boote completed", Toast.LENGTH_LONG).show();
            Intent verifyPage = new Intent(context, VerificationActivity.class);
            verifyPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(verifyPage);
        } else {
            Toast.makeText(context, "sim changed", Toast.LENGTH_LONG).show();
        }

    }
}
