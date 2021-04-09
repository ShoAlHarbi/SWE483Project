package com.example.swe483project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class VerificationActivity extends AppCompatActivity {

    EditText psc1, psc2, psc3, psc4 ;   //passcode text fields
    String passcode;
    TextView timerView;
    Intent timerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);

        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //setting the timer
        timerService = new Intent(this, TimerService.class);
        startService(timerService);
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           //update ui
        }

    };

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

    }//end init

    void updateTimer(Intent intent){
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 60000);
            String timeLift = Long.toString(millisUntilFinished / 1000) + "s";
            timerView.setText(timeLift);
        }
    }
}