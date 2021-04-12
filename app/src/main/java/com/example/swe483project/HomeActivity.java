package com.example.swe483project;

//https://stackoverflow.com/questions/12074156/android-storing-retrieving-strings-with-shared-preferences

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    ImageButton setting;
    SharedPreferences sp;
    ImageView imageView;
    TextView statusTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //String userEmail = getIntent().getStringExtra("userEmail");

        sp = getSharedPreferences("register",MODE_PRIVATE);
        imageView = findViewById(R.id.statusImageView);
        statusTxt = findViewById(R.id.statusTextView);
        //imageView.setImageDrawable();
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

    public void setStatus (){
        DatabaseHelper db = new DatabaseHelper(this);
        String currentStatus = db.getUserData(Constants.DATABASE_COLUMN.STATUS);
        switch (currentStatus){
            case Constants.STATUS.SAFE:
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check));
                statusTxt.setText(R.string.state_OK);
                break;

            case Constants.STATUS.IN_DANGER:
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_wrong));
                statusTxt.setText(R.string.state_NOT_OK);
                statusTxt.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorDarkRed));
                break;
        }
    }
}
