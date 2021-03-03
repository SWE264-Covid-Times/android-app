//this is old mainActivity page, no longer using this page

package com.example.covidtimes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void clk_login(View view){
        Log.i("this", "this is a message");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button clickbtn = findViewById(R.id.about_button);
        clickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoActivityIntent = new Intent(getApplicationContext(),infoActivity.class);
                startActivity(infoActivityIntent);
            }
        });
//        final Button clkStat = findViewById(R.id.stat_button);
//        clkStat.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent statIntent = new Intent(getApplicationContext(), StatsActivity.class);
//                startActivity(statIntent);
//            }
//        });
        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                clk_login(v);
            }
        });

       final Button infoButton = findViewById(R.id.about_button);
       infoButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent infoIntent = new Intent(getApplicationContext(), infoActivity.class);
               startActivity(infoIntent);
           }
       });

        final Button statsButton = findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statsIntent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(statsIntent);
            }
        });
        final Button guideline_button = findViewById(R.id.button_guideline);
        guideline_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent guidelineIntent = new Intent(getApplicationContext(), GuidelinesActivity.class);
                startActivity(guidelineIntent);
            }
        });
        Intent intent = new Intent(this, DelayedMessageService.class);
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE,  getResources().getString(R.string.notif_text));
        //Toast.makeText(this, "toast test", Toast.LENGTH_SHORT).show();
        startService(intent);
    }
}