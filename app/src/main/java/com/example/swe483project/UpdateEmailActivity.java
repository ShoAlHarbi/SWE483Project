package com.example.swe483project;

//https://stackoverflow.com/questions/12298835/how-to-retrieve-data-from-sqlite-database-in-android-and-display-it-in-textview
//https://stackoverflow.com/questions/9798473/sqlite-in-android-how-to-update-a-specific-row


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateEmailActivity extends AppCompatActivity {

    String userEmail;

    EditText EmailTF;
    EditText passcode1;
    EditText passcode2;
    EditText passcode3;
    EditText passcode4;

    String newEmail;
    String pass1;
    String pass2;
    String pass3;
    String pass4;

    Button update;
    Button backToHome;


    String passcode;

    DatabaseHelper DB;
    SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateemail_layout);

        DB = new DatabaseHelper(this);

        EmailTF = findViewById(R.id.UEmailTF);
        passcode1 = findViewById(R.id.UPasscode1TF);
        passcode2 = findViewById(R.id.UPasscode2TF);
        passcode3 = findViewById(R.id.UPasscode3TF);
        passcode4 = findViewById(R.id.UPasscode4TF);
        update = findViewById(R.id.UpdateButton);
        backToHome = findViewById(R.id.BackButton);

        textFieldsDynamicNavigation(passcode1,passcode2);
        textFieldsDynamicNavigation(passcode2,passcode3);
        textFieldsDynamicNavigation(passcode3,passcode4);

        String currentEmail = getIntent().getStringExtra("userEmail");
        EmailTF.setHint(currentEmail);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });

        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                pass1 = passcode1.getText().toString();
                pass2 = passcode2.getText().toString();
                pass3 = passcode3.getText().toString();
                pass4 = passcode4.getText().toString();

                //used data
                userEmail = EmailTF.getText().toString();
                passcode = pass1+pass2+pass3+pass4;

                Boolean isEmailExisit = DB.checkExistEmail(userEmail);
                //validation
                if ( userEmail.equals("") ) {
                    Toast.makeText(UpdateEmailActivity.this, "Enter a new email", Toast.LENGTH_SHORT).show();
                }
                else if ( passcode.equals("")) {
                    Toast.makeText(UpdateEmailActivity.this, "Enter your passcode", Toast.LENGTH_SHORT).show();
                }
                else if (isEmailExisit == true) {
                    Toast.makeText(UpdateEmailActivity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                } else if (isValidEmail(userEmail) == false) {
                    Toast.makeText(UpdateEmailActivity.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    String currentE = getIntent().getStringExtra("userEmail");
                    String userPasscode = "";
                    String query = "SELECT passcode FROM USERS WHERE email = \'" + currentE + "\'";
                    Cursor cursor = DB.getCustomQuery(query);
                    if (cursor.moveToFirst()){
                        userPasscode = cursor.getString(0);
                    }
                    if ( userPasscode.trim().equals(passcode.trim()) ) {
                        String currentforUpdate = getIntent().getStringExtra("userEmail");
                        String UpdateQuery = "UPDATE USERS SET email = \'" + userEmail + "\' WHERE "
                                + "email" + " = \'" + currentforUpdate + "\'";
                        DB.updateEmail(UpdateQuery);
                        sp = getSharedPreferences("register",MODE_PRIVATE);
                        sp.edit().putString("email", userEmail).apply();
                        Toast.makeText(UpdateEmailActivity.this, "UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateEmailActivity.this, "WRONG PASSCODE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void goToHome(){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
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
