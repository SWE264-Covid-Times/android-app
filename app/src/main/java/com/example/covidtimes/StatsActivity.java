// this stat page is from old template- so we need to add this to our navigation template


package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    static final String TAG = StatsActivity.class.getSimpleName();
    static Retrofit retrofit = null;

    private String country = null;
    private String from_date = null;
    private String to_date = null;

    // Toast reminder
    private static String reminder = null;
    private static Context context = null;

    private AllCountrySlug allCountrySlug = null;

    private boolean loadSecondCountryLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null)
//            loadSecondCountryLayout = savedInstanceState.getBoolean("LOAD_SECOND_COUNTRY_LAYOUT", false);
        loadSecondCountryLayout = getIntent().getBooleanExtra("loadSecondCountryLayout", false);
        System.out.println(loadSecondCountryLayout);
        if (loadSecondCountryLayout)
            setContentView(R.layout.activity_stats_two_countries);
        else
            setContentView(R.layout.activity_stats);
        linearLayoutManager = new LinearLayoutManager(this);
        context = this;

        // load all the countries from Covid-19 API
        loadCountries();
    }

    public void loadComparisonLayout(View view) {
        loadSecondCountryLayout = true;
        finish();
        Intent intent = new Intent(getBaseContext(), StatsActivity.class);
        intent.putExtra("loadSecondCountryLayout", true);
        startActivity(intent);
    }

    public void onClickConfirm(View view) {
        // sample valid input:
        // 20200301
        // will return information of selected country from 20200301 to 20200307

        Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
        String raw_country = String.valueOf(spCountry.getSelectedItem());
        country = allCountrySlug.getSlug(raw_country);
        System.out.println(raw_country + " - " + country);

        EditText etFromDate = (EditText) findViewById(R.id.etFromDate);
        String raw_from_date = etFromDate.getText().toString();
        boolean validFromDate = false;
        if (raw_from_date.length() == 8) {
            validFromDate = dateStringHelper.isValidDate(raw_from_date.substring(0, 4),
                    raw_from_date.substring(4, 6),
                    raw_from_date.substring(6, 8));
        }
        if (validFromDate) {
            from_date = dateStringHelper.getQueryableDate(
                    raw_from_date.substring(0, 4),
                    raw_from_date.substring(4, 6),
                    raw_from_date.substring(6, 8));

            // create a new to_date with range of a week
            System.out.println("String " + raw_from_date);
            System.out.println("Integer " + Integer.parseInt(raw_from_date));
            String raw_to_date = Integer.toString(Integer.parseInt(raw_from_date) + 6);
            System.out.println(raw_to_date);
            to_date = dateStringHelper.getQueryableDate(
                    raw_to_date.substring(0, 4),
                    raw_to_date.substring(4, 6),
                    raw_to_date.substring(6, 8));
            System.out.println("from " + from_date + " " + "to " + to_date);

            // let user to enter a to_date
            //        EditText etToDate = (EditText) findViewById(R.id.etToDate);
            //        String raw_to_date = etToDate.getText().toString();
            //        to_date = dateStringHelper.getQueryableDate(
            //                raw_to_date.substring(0, 4),
            //                raw_to_date.substring(4, 6),
            //                raw_to_date.substring(6, 8));
            //        System.out.println("from " +from_date + " " + "to " + to_date);

            connect();
        } else {
            reminder = "Make sure to follow YYYYMMDD format\n Only number allowed";
            Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
            toast.show();
            System.out.println(reminder);
            reminder = null;
        }
    }


    private void loadCountries() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(StatsAPIService.BASE_LIVE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StatsAPIService statsApiService = retrofit.create(StatsAPIService.class);
        Call<List<CountrySlugInfo>> call = statsApiService.getCountrySlug();
        System.out.println("yo3");
        System.out.println(call);
        call.enqueue(new Callback<List<CountrySlugInfo>>() {
            @Override
            public void onResponse(Call<List<CountrySlugInfo>> call, Response<List<CountrySlugInfo>> response) {

                System.out.println("yo4");
                List<CountrySlugInfo> statsInfo = response.body();

//                for (CountrySlugInfo csc : statsInfo)
//                    System.out.println(csc.getCountry());
//                System.out.println("here: " + statsInfo);
                if (statsInfo != null) {
                    System.out.println("here");
                    allCountrySlug = new AllCountrySlug(statsInfo);

                    for (String key: allCountrySlug.getCountrySlugPairs().keySet())
                    System.out.println(key);
                } else {
                    reminder = "Not able to connect to server";
                    Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println(reminder);
                    reminder = null;
                }

                // load spinner
                loadSpinner();
            }

            @Override
            public void onFailure(Call<List<CountrySlugInfo>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }

    private void loadSpinner(){
        Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                allCountrySlug.getCountrySlugPairs().keySet().toArray());

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spCountry.setAdapter(ad);

        if (loadSecondCountryLayout) {
            Spinner spSecondCountry = (Spinner) findViewById(R.id.spSecondCountry);
            ArrayAdapter adSecondCountry
                    = new ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    allCountrySlug.getCountrySlugPairs().keySet().toArray());

            adSecondCountry.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);
            spSecondCountry.setAdapter(adSecondCountry);

        }
    }

    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(StatsAPIService.BASE_LIVE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StatsAPIService statsApiService = retrofit.create(StatsAPIService.class);
        Call<List<CountryStatsInfo>> call = statsApiService.getCasesWithTimeFrame(country, from_date, to_date);
        call.enqueue(new Callback<List<CountryStatsInfo>>() {
            @Override
            public void onResponse(Call<List<CountryStatsInfo>> call, Response<List<CountryStatsInfo>> response) {

                List<CountryStatsInfo> statsInfo = response.body();
//                System.out.println("country: " + country + " from_date: " +from_date + " to_date: "+to_date+ " call: "+call);
//                System.out.println("here " + statsInfo);
//                System.out.println("here " + statsInfo.get(0));
//                System.out.println("here " + statsInfo.get(0).getCountry());
                if (statsInfo != null) {
                    recyclerView = findViewById(R.id.rvStatsList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(new StatsListAdapter(statsInfo));
                } else {
                    reminder = "Invalid Search:D\n No Info";
                    Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println(reminder);
                    reminder = null;
                }
            }

            @Override
            public void onFailure(Call<List<CountryStatsInfo>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
