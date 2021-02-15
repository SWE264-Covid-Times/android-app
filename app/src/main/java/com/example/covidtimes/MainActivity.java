package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final Button infoButton = findViewById(R.id.btnInfo);
       infoButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent infoIntent = new Intent(getApplicationContext(), infoActivity.class);
               startActivity(infoIntent);
           }
       });

    }
}