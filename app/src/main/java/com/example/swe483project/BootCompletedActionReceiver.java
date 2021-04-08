package com.example.swe483project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class BootCompletedActionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "boote completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "sim changed", Toast.LENGTH_LONG).show();
            //compare old with new sim card
        }

        Intent verifyPage = new Intent(context, VerificationActivity.class);
        verifyPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(verifyPage);

    }
}
