package com.example.covidtimes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private String from_date = null;
    private String to_date = null;

    // Toast reminder
    private static String reminder = null;
    private static Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        linearLayoutManager = new LinearLayoutManager(this);
        context = this;
    }

    public void onClickConfirm(View view) {
        // --- sample we have right now ----
        // valid input:
        // south-africa / australia
        // raw_from_date: 20200301
        // raw_to_date: 20200401
        //
        // invalid input:
        // united-states
        // raw_from_date: 20200301
        // raw_to_date: 20200401
        Spinner spCountry = (Spinner) findViewById(R.id.spCountry);
        country = String.valueOf(spCountry.getSelectedItem());

        EditText etFromDate = (EditText) findViewById(R.id.etFromDate);
        String raw_from_date = etFromDate.getText().toString();
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
        System.out.println("from " +from_date + " " + "to " + to_date);


//        EditText etToDate = (EditText) findViewById(R.id.etToDate);
//        String raw_to_date = etToDate.getText().toString();
//        to_date = dateStringHelper.getQueryableDate(
//                raw_to_date.substring(0, 4),
//                raw_to_date.substring(4, 6),
//                raw_to_date.substring(6, 8));
//        System.out.println("from " +from_date + " " + "to " + to_date);

        connect();
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
                    reminder = null;
                    System.out.println("Invalid Search:D\n No Info");
                }
            }

            @Override
            public void onFailure(Call<List<CountryStatsInfo>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
