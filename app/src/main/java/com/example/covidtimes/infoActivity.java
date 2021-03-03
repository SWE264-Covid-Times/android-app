// this about page is shifted to new template, no longer using this

package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class infoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView tv = findViewById(R.id.tvInfo_JHULink);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

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