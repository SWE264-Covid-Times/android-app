package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.example.covidtimes.R;

public class GuidelinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

        TextView tv = findViewById(R.id.textView10);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

    }

}