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
        boolean isbooted = false;
        boolean isSIMChanged = false;
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Constants.ACTION.QUICBOOT_POWERON.equals(intent.getAction())) {
            Toast.makeText(context, "boote completed", Toast.LENGTH_LONG).show();
            isbooted = true;
        } else if (Constants.ACTION.SIM_STATE_CHANGED.equals(intent.getAction())){
            Toast.makeText(context, "sim changed", Toast.LENGTH_LONG).show();
            //compare old with new sim card
            VerificationActivity verificationActivity = new VerificationActivity();
            String currentSIM = verificationActivity.getSIM();
            DatabaseHelper db = new DatabaseHelper(verificationActivity);
            String oldSIM = db.getUserData(Constants.DATABASE_COLUMN.SIM);
            if(currentSIM.equals(oldSIM))
                isSIMChanged = true;
        }

        if(isbooted || isSIMChanged){
            Intent verifyPage = new Intent(context, VerificationActivity.class);
            verifyPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(verifyPage);
        }

    }


}
