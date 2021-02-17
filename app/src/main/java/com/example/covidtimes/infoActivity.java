package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class infoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final Button infoExpButton = findViewById(R.id.btnInfo_Exp);
        infoExpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statIntent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(statIntent);
            }
        });
    }
}