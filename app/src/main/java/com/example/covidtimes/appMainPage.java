//this is our new main page of navigation drawer template

package com.example.covidtimes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.example.covidtimes.ui.about.AboutFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class appMainPage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private locationHelper locHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        final Button clickbtn = findViewById(R.id.nav_about);
//        clickbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent infoActivityIntent = new Intent(getApplicationContext(),AboutFragment.class);
//                startActivity(infoActivityIntent);
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login, R.id.nav_about, R.id.nav_stat, R.id.nav_guidelines, R.id.nav_history)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
/*
        locHelper = new locationHelper(this);
        if (locHelper.askPermissions()){
            onLocationPermissionGranted();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_main_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void onClickConfirm(View view) {
        Log.v("appMainPage", "login?");
    }

    public void aboutOnClick(View view){
        AboutFragment.aboutClick(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case locationHelper.PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //onLocationPermissionGranted();
                }
                else{ // if permissions were denied
                    Log.v(this.getClass().getName(), "Location access denied");
                }
            }
        }
    }
/*
    private void onLocationPermissionGranted(){
        Log.v(this.getClass().getName(), locHelper.getCurrentCountry());
        if (locHelper.getCurrentCountry().equals("US")){
            Log.v(this.getClass().getName(), locHelper.getCurrentState());
        }
        Intent intent = new Intent(this, DelayedMessageService.class);
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE, locHelper.getCurrentState());
        //Toast.makeText(this, "toast test", Toast.LENGTH_SHORT).show();
        startService(intent);
    }*/
}