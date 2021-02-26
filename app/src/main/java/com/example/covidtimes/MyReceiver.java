package com.example.covidtimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i){
        Toast.makeText(c, "New cases: " , Toast.LENGTH_LONG).show();
    }
}
