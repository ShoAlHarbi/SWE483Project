package com.example.swe483project;

//Reference: https://o7planning.org/12697/get-phone-number-in-android-using-telephonymanager

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RegisterActivity extends AppCompatActivity  {

    private static final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE = 1;

    EditText email;
    EditText passcode1;
    EditText passcode2;
    EditText passcode3;
    EditText passcode4;

    Button registerButton;

    DatabaseHelper DB;
    String selectedEmail;
    String pass1;
    String pass2;
    String pass3;
    String pass4;
    String Passcode;
    String SIM;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        DB = new DatabaseHelper(this);

        email = findViewById(R.id.EmailTF);
        passcode1 = findViewById(R.id.Passcode1TF);
        passcode2 = findViewById(R.id.Passcode2TF);
        passcode3 = findViewById(R.id.Passcode3TF);
        passcode4 = findViewById(R.id.Passcode4TF);
        registerButton = findViewById(R.id.RegisterButton);

        this.askPermissionAndGetSIM();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                pass1 = passcode1.getText().toString();
                pass2 = passcode2.getText().toString();
                pass3 = passcode3.getText().toString();
                pass4 = passcode4.getText().toString();

                //input data
                selectedEmail = email.getText().toString();
                Passcode = pass1+pass2+pass3+pass4;

                if ( selectedEmail.equals("") || pass1.equals("") || pass2.equals("") || pass3.equals("") || pass4.equals(""))
                    Toast.makeText(RegisterActivity.this, "All Fields Mandatory", Toast.LENGTH_SHORT).show();
                else {
                    Boolean isEmailExisit = DB.checkExistEmail(selectedEmail);
                    if (isEmailExisit == false){
                        if( SIM.equals("") )
                            Toast.makeText(RegisterActivity.this, "Insert a SIM Card Please", Toast.LENGTH_SHORT).show();
                        else{
                            DB.insertUser(selectedEmail, Passcode, SIM,"Safe");
                            Toast.makeText(RegisterActivity.this, "card is exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "This Email is Existing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void askPermissionAndGetSIM() {
        // With Android Level >= 23, you have to ask the user
        // for permission to get Phone Number.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have READ_PHONE_STATE permission
            int readPhoneStatePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);

            if ( readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSION_REQUEST_CODE_PHONE_STATE
                );
                return;
            }
        }
        this.getSIM();
    }

    @SuppressLint("MissingPermission")
    private void getSIM() {
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            SIM = manager.getSimSerialNumber();
        } catch (Exception ex) {
            Toast.makeText(this,"Error: " + ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_PHONE_STATE: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.getSIM();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_PHONE_STATE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }



}
