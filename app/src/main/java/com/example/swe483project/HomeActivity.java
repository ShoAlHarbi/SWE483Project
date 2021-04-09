package com.example.swe483project;

//https://stackoverflow.com/questions/12074156/android-storing-retrieving-strings-with-shared-preferences

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    ImageButton setting;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //String userEmail = getIntent().getStringExtra("userEmail");

        sp = getSharedPreferences("register",MODE_PRIVATE);
        //sp.edit().putString("email", userEmail).apply();

        setting = findViewById(R.id.settingButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
    }

    public void goToSettings(){
        Intent i = new Intent(this,UpdateEmailActivity.class);
        String currentEmail = sp.getString("email", "some null");;
        i.putExtra("userEmail", currentEmail);
        startActivity(i);
    }
}
