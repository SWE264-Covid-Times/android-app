//this is our new main page of navigation drawer template

package com.example.covidtimes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.UUID;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class appMainPage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private locationHelper locHelper;
    private TextView cdc_link;

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

        handleSharedPrefs();
        locHelper = new locationHelper(this, appMainPage::onLocationPermissionGranted);
        locHelper.handlePermissions();
        //cdc_link = findViewById(R.id.textGrid1);
        //cdc_link.setMovementMethod(LinkMovementMethod.getInstance());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Log.d("MyDebugger", "onRequestPermissionResult: " + requestCode);
        switch(requestCode){
            case locationHelper.PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locHelper.handlePermissions();
                }
                else{ // if permissions were denied
                    Log.d("MyDebugger", "Location access denied");
                }
            }
        }
    }

    private static void onLocationPermissionGranted(Address a, Context c){
        Intent intent = new Intent(c, DelayedMessageService.class);
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE, locationHelper.getCurrentState(a));
        c.startService(intent);
    }


    private void handleSharedPrefs(){
        SharedPreferences sf = this.getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        resetPrefs(sf);
        String name = sf.getString(getString(R.string.pref_user_name), null);
        if (name == null){
            SharedPreferences.Editor editor = sf.edit();
            name = UUID.randomUUID().toString();
            editor.putString(getString(R.string.pref_user_name), name);
            editor.commit();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(HistoryAPIService.BASE_HISTORY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            HistoryAPIService historyApiService = retrofit.create(HistoryAPIService.class);
            /*
            Map<String, String> jsonParams = new ArrayMap<>();
            jsonParams.put("name", name);
            RequestBody body = RequestBody.create((new JSONObject(jsonParams)).toString(),
                    okhttp3.MediaType.parse("application/json; charset=utf-8")
                    );
            Call<ResponseBody> call = historyApiService.createUser(body);
            call.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> rawResponse){
                    Log.d("MyDebugger", rawResponse.toString());
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t){
                    Log.d("MyDebugger", t.toString());
                }
            });*/

            Call<historyUser> call = historyApiService.createUser(new historyUser(name));
            call.enqueue(new Callback<historyUser>(){
                @Override
                public void onResponse(Call<historyUser> call, Response<historyUser> response){
                    Log.d("MyDebugger", response.toString());
                }
                @Override
                public void onFailure(Call<historyUser> call, Throwable t){
                    Log.d("MyDebugger", t.toString());
                }
            });
        }
    }

    private void resetPrefs(SharedPreferences sp){
        SharedPreferences.Editor e = sp.edit();
        e.clear();
        e.commit();
    }



}