package com.example.swe483project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class VerificationActivity extends AppCompatActivity {

    EditText psc1, psc2, psc3, psc4;   //passcode text fields
    String passcode;
    TextView timerView;
    Intent timerService;
    Button submitButton;
    String TAG = "com.example.VerificationActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;
    String currentLocation;
    boolean isRegistered = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);
        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        //setting the timer
        timerService = new Intent(this, TimerService.class);
        timerService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(timerService);
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //update ui
            updateTimer(intent);
        }

    };

    @Override
    protected void onStop() {
        if (isRegistered) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
    }


    void textFieldsDynamicNavigation(final EditText first, final EditText second) {
        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (first.getText().toString().length() == 1) {
                    second.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void init() {
        //connect text fields
        psc1 = findViewById(R.id.editTextNumber1);
        psc2 = findViewById(R.id.editTextNumber2);
        psc3 = findViewById(R.id.editTextNumber3);
        psc4 = findViewById(R.id.editTextNumber4);
        textFieldsDynamicNavigation(psc1, psc2);
        textFieldsDynamicNavigation(psc2, psc3);
        textFieldsDynamicNavigation(psc3, psc4);

        //timer view
        timerView = findViewById(R.id.timerTextView);

        //SET THE LOCATION SERVICE
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //submit button
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredPasscode = psc1.getText().toString() + psc2.getText().toString() +
                        psc3.getText().toString() + psc4.getText().toString();
                if (enteredPasscode.equals("")) {
                    Toast.makeText(VerificationActivity.this, "Please provide a complete passcode of 4 numbers", Toast.LENGTH_LONG).show();
                    return;
                }

                //stop the timer
                if (isRegistered) {
                    timerService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                    startService(timerService);
                    unregisterReceiver(broadcastReceiver);
                    isRegistered = false;
                }

                //compare the passcode
                DatabaseHelper db = new DatabaseHelper(VerificationActivity.this);
                String currentPasscode = db.getUserData(Constants.DATABASE_COLUMN.PASSCODE);
                if (enteredPasscode.equals(currentPasscode)) {  //if correct
                    Toast.makeText(VerificationActivity.this, "Correct passcode !", Toast.LENGTH_LONG).show();
                    db.updateUserData(Constants.DATABASE_COLUMN.SIM, getSIM());
                    db.updateUserData(Constants.DATABASE_COLUMN.STATUS, Constants.STATUS.SAFE);
                }else { //if wrong
                    Toast.makeText(VerificationActivity.this, "Wrong passcode !", Toast.LENGTH_LONG).show();
                    db.updateUserData(Constants.DATABASE_COLUMN.STATUS, Constants.STATUS.IN_DANGER);
                    String email = db.getUserData(Constants.DATABASE_COLUMN.EMAIL);
                    notifyUser(email);
                }//end else
                navigateToHome();
            }
        });

    }//end init

    void updateTimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 60000);
            String timeLift = Long.toString(millisUntilFinished / 1000) + "s";
            timerView.setText(timeLift);


            if (intent.hasExtra("finish")) {
                //time is up GET CURRENT LOCATION AND SEND EMAIL
                DatabaseHelper db = new DatabaseHelper(VerificationActivity.this);
                Toast.makeText(VerificationActivity.this, "Your time is up !", Toast.LENGTH_LONG).show();
                db.updateUserData(Constants.DATABASE_COLUMN.STATUS, Constants.STATUS.IN_DANGER);
                String email = db.getUserData(Constants.DATABASE_COLUMN.EMAIL);
                notifyUser(email);
                navigateToHome();
            }

        }
    }//end updateTimer

    public void notifyUser(final String userEmail) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentLocation = "Location could not be identified due to rejected access to current device location";
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Log.i(TAG, "location is not null");
                    Geocoder geocoder = new Geocoder(VerificationActivity.this, Locale.getDefault());
                    try {
                        Log.i(TAG, "inside try");
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currentLocation = "Your phone is located at" + addressList.get(0).getLatitude() + " , " +
                                addressList.get(0).getLongitude() + "\nCountry: " + addressList.get(0).getCountryName() +
                                ", City: " + addressList.get(0).getLocality() + "\nAddress: " + addressList.get(0).getAddressLine(0);
                        Log.i(TAG, "current location is : " + currentLocation);
                        Toast.makeText(VerificationActivity.this, "the location" + currentLocation, Toast.LENGTH_LONG).show();
                        sendEmail(userEmail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void sendEmail(String userEmail) {
        String email = userEmail;      //user email
        String subject = "BLUE SHIELD - Important security alert";      //email subject
        String message = "This is a security alert from BLUE SHIELD to tell you that your phone has been stolen.\n" + currentLocation;    //message
        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();
    }

    public String getSIM() {
        String SIM = "";
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            SIM = manager.getSimSerialNumber();
        } catch (Exception ex) {
            Toast.makeText(this,"Error: " + ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

        return SIM;
    }

    private void navigateToHome(){
        Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}