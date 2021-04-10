package com.example.swe483project;

//Reference: https://o7planning.org/12697/get-phone-number-in-android-using-telephonymanager
//https://medium.com/@prakharsrivastava_219/keep-the-user-logged-in-android-app-5fb6ce29ed65
//https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RegisterActivity extends AppCompatActivity  {

    private static final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE = 1;
    private static final int MY_PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION = 2;

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

    SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("register",MODE_PRIVATE);
        if(sp.getBoolean("registered",false)){
            goToHome();
        }

        setContentView(R.layout.register_layout);

        DB = new DatabaseHelper(this);

        email = findViewById(R.id.EmailTF);
        passcode1 = findViewById(R.id.Passcode1TF);
        passcode2 = findViewById(R.id.Passcode2TF);
        passcode3 = findViewById(R.id.Passcode3TF);
        passcode4 = findViewById(R.id.Passcode4TF);
        registerButton = findViewById(R.id.RegisterButton);

        textFieldsDynamicNavigation(passcode1,passcode2);
        textFieldsDynamicNavigation(passcode2,passcode3);
        textFieldsDynamicNavigation(passcode3,passcode4);

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
                //to check if email exists
                Boolean isEmailExisit = DB.checkExistEmail(selectedEmail);

                //validation
                if ( selectedEmail.equals("") || pass1.equals("") || pass2.equals("") || pass3.equals("") || pass4.equals(""))
                    Toast.makeText(RegisterActivity.this, "All Fields Mandatory", Toast.LENGTH_SHORT).show();
                else if (isValidEmail(selectedEmail) == false){
                    Toast.makeText(RegisterActivity.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
                }
                else if (isEmailExisit == true)
                    Toast.makeText(RegisterActivity.this, "This Email is Existing", Toast.LENGTH_SHORT).show();
                else if (SIM == null)
                    Toast.makeText(RegisterActivity.this, "Insert a SIM Card Please", Toast.LENGTH_SHORT).show();
                else {
                    DB.insertUser(selectedEmail, Passcode, SIM, "Safe");
                    goToHome();
                    sp.edit().putBoolean("registered", true).apply();
                    sp.edit().putString("email", selectedEmail).apply();
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void goToHome(){
        Intent i = new Intent(this,HomeActivity.class);
        i.putExtra("userEmail", selectedEmail);
        startActivity(i);
    }

    private void askPermissionAndGetSIM() {
        // With Android Level >= 23, you have to ask the user
        // for permission to get Phone Number.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {// 23

        // Check if we have READ_PHONE_STATE permission
        int readPhoneStatePermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int accessFineLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION
            );
        }
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
            case MY_PERMISSION_REQUEST_CODE_ACCESS_FINE_LOCATION:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

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
}
