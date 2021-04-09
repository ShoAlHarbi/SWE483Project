package com.example.swe483project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    String userEmail;
    String userSIM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        userEmail = getIntent().getStringExtra("userEmail");
        userSIM = getIntent().getStringExtra("userSIM");
    }
}
