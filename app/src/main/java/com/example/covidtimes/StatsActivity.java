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
    private String secondCountry = null;
    private String from_date = null;
    private String to_date = null;

    // Toast reminder
    private static String reminder = null;
    private static Context context = null;

    private AllCountrySlug allCountrySlug = null;

    private boolean loadSecondCountryLayout = false;

    private List<CountryStatsInfo> firstCountryStatsInfo = null;
    private List<CountryStatsInfo> secondCountryStatsInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null)
//            loadSecondCountryLayout = savedInstanceState.getBoolean("LOAD_SECOND_COUNTRY_LAYOUT", false);
        loadSecondCountryLayout = getIntent().getBooleanExtra("loadSecondCountryLayout", false);
        System.out.println("[StatsActivity.onCreate] " + loadSecondCountryLayout);
        if (loadSecondCountryLayout)
            setContentView(R.layout.activity_stats_two_countries);
        else
            setContentView(R.layout.activity_stats);
        linearLayoutManager = new LinearLayoutManager(this);
        context = this;

        // load all the countries from Covid-19 API
        loadCountries();
//        if (allCountrySlug == null || allCountrySlug.isEmpty())
//            loadCountries();
//        else
//            loadSpinner();
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
        call.enqueue(new Callback<List<CountrySlugInfo>>() {
            @Override
            public void onResponse(Call<List<CountrySlugInfo>> call, Response<List<CountrySlugInfo>> response) {
                List<CountrySlugInfo> statsInfo = response.body();
//                System.out.println("[StatsActivity.loadCountries] " + statsInfo + " statsInfo.isEmpty(): " + statsInfo.isEmpty());
                if (statsInfo != null) {
                    allCountrySlug = new AllCountrySlug(statsInfo);
                    System.out.println("[StatsActivity.loadCountries] " + allCountrySlug);
//                    for (String key: allCountrySlug.getCountrySlugPairs().keySet())
//                        System.out.println(key);

                    // load spinner
                    System.out.println("[StatsActivity.loadCountries] ready to load spinner" );
                    loadSpinner();
                } else {
                    reminder = "Not able to connect to server";
                    Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println(reminder);
                    reminder = null;
                }
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
        System.out.println("[StatsActivity.onClickConfirm] c1: " + raw_country + " -> " + country);
        if (loadSecondCountryLayout) {
            Spinner spSecondCountry = (Spinner) findViewById(R.id.spSecondCountry);
            String raw_second_country = String.valueOf(spSecondCountry.getSelectedItem());
            secondCountry = allCountrySlug.getSlug(raw_second_country);
            System.out.println("[StatsActivity.onClickConfirm] c2: " + raw_second_country + " -> " + secondCountry);
        }

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
            String raw_to_date = Integer.toString(Integer.parseInt(raw_from_date) + 6);
            to_date = dateStringHelper.getQueryableDate(
                    raw_to_date.substring(0, 4),
                    raw_to_date.substring(4, 6),
                    raw_to_date.substring(6, 8));
            System.out.println("[StatsActivity.onClickConfirm] from " + from_date + " " + "to " + to_date);

            // let user to enter a to_date
            //        EditText etToDate = (EditText) findViewById(R.id.etToDate);
            //        String raw_to_date = etToDate.getText().toString();
            //        to_date = dateStringHelper.getQueryableDate(
            //                raw_to_date.substring(0, 4),
            //                raw_to_date.substring(4, 6),
            //                raw_to_date.substring(6, 8));
            //        System.out.println("from " +from_date + " " + "to " + to_date);


            if (loadSecondCountryLayout)
                connectTwo();
            else
                connectOne();
        } else {
            reminder = "Make sure to follow YYYYMMDD format\nOnly number allowed";
            Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
            toast.show();
            System.out.println(reminder);
            reminder = null;
        }
    }

    // use for single country stats
    private void connectOne() {
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
//                System.out.println("[StatsActivity.connect]");
//                System.out.println("country: " + country + " from_date: " +from_date + " to_date: "+to_date+ " call: "+call);
//                System.out.println("here " + statsInfo);
//                System.out.println("here " + statsInfo.isEmpty());
                if (statsInfo != null) {
                    if (statsInfo.isEmpty()){
                        reminder = "No data found\nTry another country or data";
                        Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println(reminder);
                        reminder = null;
                    } else {
                        recyclerView = findViewById(R.id.rvStatsList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(new StatsListAdapter(statsInfo));
                    }
                } else {
                    reminder = "Invalid Search:D\nNo Info";
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

    // use for two countries comparison
    private void connectTwo() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(StatsAPIService.BASE_LIVE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StatsAPIService statsApiService = retrofit.create(StatsAPIService.class);

        // get first country stats info
        Call<List<CountryStatsInfo>> call = statsApiService.getCasesWithTimeFrame(country, from_date, to_date);
        call.enqueue(new Callback<List<CountryStatsInfo>>() {
            @Override
            public void onResponse(Call<List<CountryStatsInfo>> call, Response<List<CountryStatsInfo>> response) {

                firstCountryStatsInfo = response.body();
                System.out.println("[StatsActivity.connect] firstCountryStatsInfo: " + firstCountryStatsInfo + "\n" +firstCountryStatsInfo.isEmpty());
//                System.out.println("[StatsActivity.connectTwo]");
//                System.out.println("country: " + country + " from_date: " +from_date + " to_date: "+to_date+ " call: "+call);
//                System.out.println("here " + firstCountryStatsInfo);
//                System.out.println("here " + firstCountryStatsInfo.isEmpty());
                if (firstCountryStatsInfo != null) {
                    if (firstCountryStatsInfo.isEmpty()){
                        reminder = "No data found\nTry another country or data";
                        Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println(reminder);
                        reminder = null;
                    }
                } else {
                    reminder = "Invalid Search:D\nNo Info";
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

        // get second country stats info
        Call<List<CountryStatsInfo>> call2 = statsApiService.getCasesWithTimeFrame(secondCountry, from_date, to_date);
        call2.enqueue(new Callback<List<CountryStatsInfo>>() {
            @Override
            public void onResponse(Call<List<CountryStatsInfo>> call2, Response<List<CountryStatsInfo>> response) {

                secondCountryStatsInfo = response.body();
                System.out.println("[StatsActivity.connect] secondCountryStatsInfo: " + "\n" +secondCountryStatsInfo.isEmpty());
//                System.out.println("[StatsActivity.connectTwo]");
//                System.out.println("secondCountry: " + secondCountry + " from_date: " +from_date + " to_date: "+to_date+ " call2: "+call2);
//                System.out.println("here " + secondCountryStatsInfo);
//                System.out.println("here " + secondCountryStatsInfo.isEmpty());
                if (secondCountryStatsInfo != null) {
                    if (secondCountryStatsInfo.isEmpty()){
                        reminder = "No data found\nTry another country or data";
                        Toast toast = Toast.makeText(context, reminder, Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println(reminder);
                        reminder = null;
                    } else {
                        System.out.println("[StatsActivity.connect] start to run recycler");
                        recyclerView = findViewById(R.id.rvStatsList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(new StatsListAdapter(firstCountryStatsInfo, secondCountryStatsInfo));
                    }
                } else {
                    reminder = "Invalid Search:D\nNo Info";
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
