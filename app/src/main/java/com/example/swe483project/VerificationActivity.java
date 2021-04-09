package com.example.swe483project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    EditText psc1, psc2, psc3, psc4 ;   //passcode text fields
    String passcode;
    TextView timerView;
    Intent timerService;
    Button submitButton;
    String TAG = "com.example.VerificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);

        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
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
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter(TimerService.COUNTDOWN_BR));
    }



    void textFieldsDynamicNavigation (final EditText first, final EditText second){
        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(first.getText().toString().length() == 1){
                    second.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void init(){
        //connect text fields
        psc1 = findViewById(R.id.editTextNumber1);
        psc2 = findViewById(R.id.editTextNumber2);
        psc3 = findViewById(R.id.editTextNumber3);
        psc4 = findViewById(R.id.editTextNumber4);
        textFieldsDynamicNavigation(psc1,psc2);
        textFieldsDynamicNavigation(psc2,psc3);
        textFieldsDynamicNavigation(psc3,psc4);

        //timer view
        timerView = findViewById(R.id.timerTextView);

        //submit button
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(timerService);
                unregisterReceiver(broadcastReceiver);
            }
        });

    }//end init

    void updateTimer(Intent intent){
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 60000);
            String timeLift = Long.toString(millisUntilFinished / 1000) + "s";
            timerView.setText(timeLift);


            if(intent.hasExtra("finish"))
                //Log.i(TAG, "heeeeeerre after finish");
                Toast.makeText(this, "time is up", Toast.LENGTH_LONG).show();

        }
    }
}