package com.example.swe483project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class actionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isbooted = false;
        boolean isSIMChanged = false;
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Constants.ACTION.QUICBOOT_POWERON.equals(intent.getAction())) {
            Toast.makeText(context, "boot completed", Toast.LENGTH_LONG).show();
            isbooted = true;
        } else if (Constants.ACTION.SIM_STATE_CHANGED.equals(intent.getAction()) && intent.hasExtra("ss")){
            if (intent.getExtras().getString("ss").equals("READY")) {
                Toast.makeText(context, "sim changed", Toast.LENGTH_LONG).show();
                //compare old with new sim card
                    isSIMChanged = isChanged(context);
            }
        }else{  //in case uncaught  boot action
            isbooted = true;
        }

        if(isbooted || isSIMChanged){
            Intent verifyPage = new Intent(context, VerificationActivity.class);
            verifyPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(verifyPage);
        }

    }

    private boolean isChanged(Context context) {
        boolean isChanged;
        String currentSIM = getSIM(context);
        DatabaseHelper db = new DatabaseHelper(context);
        String oldSIM = db.getUserData(Constants.DATABASE_COLUMN.SIM);
        isChanged =  !currentSIM.equals(oldSIM);
        if(!isChanged){
            db.updateUserData(Constants.DATABASE_COLUMN.STATUS, Constants.STATUS.SAFE);
        }
        return isChanged;
    }

    public String getSIM(Context context) {
        String SIM = "";
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            SIM = manager.getSimSerialNumber();
        } catch (Exception ex) {
            Toast.makeText(context,"Error: " + ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

        return SIM;
    }


}
