package com.example.covidtimes.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtimes.AllCountrySlug;
import com.example.covidtimes.CountryStatsInfo;
import com.example.covidtimes.HistoryAPIService;
import com.example.covidtimes.R;
import com.example.covidtimes.StatsAPIService;
import com.example.covidtimes.StatsListAdapter;
import com.example.covidtimes.dateStringHelper;
import com.example.covidtimes.historyStats;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryFragment extends Fragment {

    private GalleryViewModel mViewModel;
    private View rootView;
    private AllCountrySlug allCountrySlug;
    static Retrofit retrofit;
    static Retrofit historyRetrofit;
    private String country= "";
    private String secondCountry = null;
    private String from_date = "";
    private String to_date = "";
    private boolean compare_two;
    private List<CountryStatsInfo> firstCountryStats = null;
    private AtomicBoolean waitCountryStats;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        compare_two = false;
        waitCountryStats = new AtomicBoolean(false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedI){
        super.onActivityCreated(savedI);
        mViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        setUpCompareButton();
        setUpButton();
        mViewModel.getCountryList().observe(getViewLifecycleOwner(), allCS ->{
            allCountrySlug = allCS;
            if (allCS == null){
                Toast toast = Toast.makeText(getActivity(), "Not able to connect to server", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Spinner spCountry = rootView.findViewById(R.id.spCountry);
                ArrayAdapter ad
                        = new ArrayAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        allCS.getCountrySlugPairs().keySet().toArray());

                ad.setDropDownViewResource(
                        android.R.layout
                                .simple_spinner_dropdown_item);
                spCountry.setAdapter(ad);
            }
        });
    }

    private void setUpCompareButton(){
        Button compareButton = rootView.findViewById(R.id.addComparison);
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                compare_two = true;
                compareButton.setVisibility(Button.GONE);
                Spinner secondSP = rootView.findViewById(R.id.spSecondCountry);
                secondSP.setVisibility(Button.VISIBLE);
                ArrayAdapter ad
                        = new ArrayAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        allCountrySlug.getCountrySlugPairs().keySet().toArray());

                ad.setDropDownViewResource(
                        android.R.layout
                                .simple_spinner_dropdown_item);
                secondSP.setAdapter(ad);
            }
        });
    }

    private void setUpButton(){
        Button searchButton = rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Spinner spCountry = rootView.findViewById(R.id.spCountry);
                String raw_country = String.valueOf(spCountry.getSelectedItem());
                country = allCountrySlug.getSlug(raw_country);
                System.out.println("[StatsActivity.onClickConfirm] c1: " + raw_country + " -> " + country);
                if (compare_two) {
                    Spinner spSecondCountry = rootView.findViewById(R.id.spSecondCountry);
                    String raw_second_country = String.valueOf(spSecondCountry.getSelectedItem());
                    secondCountry = allCountrySlug.getSlug(raw_second_country);
                }

                EditText etFromDate = rootView.findViewById(R.id.etFromDate);
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
                    String raw_to_date = Integer.toString(Integer.parseInt(raw_from_date));
                    to_date = dateStringHelper.getQueryableDateOffset(
                            raw_to_date.substring(0, 4),
                            raw_to_date.substring(4, 6),
                            raw_to_date.substring(6, 8), 6);
                    Log.d("MyDebugger", to_date);
                    if (allCountrySlug == null){
                        Toast toast = Toast.makeText(getActivity(), "Not able to connect to server", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        getCountryStats();
                    }
                } else {
                    String reminder = "Make sure to follow YYYYMMDD format\nOnly number allowed";
                    Toast toast = Toast.makeText(getActivity(), reminder, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void getCountryStats(){
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
                if (statsInfo != null) {
                    if (statsInfo.isEmpty()){
                        String reminder = "No data found\nTry another country or data";
                        Toast toast = Toast.makeText(getActivity(), reminder, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        addToHistory(statsInfo);
                        if (!compare_two){ //if only displaying one country
                            RecyclerView recyclerView = rootView.findViewById(R.id.rvStatsList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(new StatsListAdapter(statsInfo));
                        }
                        else{ //if comparing two
                            if (waitCountryStats.get() == false){ //if we don't have a single piece of data yet
                                firstCountryStats = statsInfo;
                                waitCountryStats.set(true);
                            }
                            else{ //waitCountryStats was true, we have the other data set in memory
                                RecyclerView recyclerView = rootView.findViewById(R.id.rvStatsList);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new StatsListAdapter(statsInfo, firstCountryStats));
                            }
                        }
                    }
                } else {
                    String reminder = "Invalid Search:\nNo Info";
                    Toast toast = Toast.makeText(getActivity(), reminder, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<List<CountryStatsInfo>> call, Throwable throwable) {
                Log.e("MyDebugger", throwable.toString());
            }
        });
        if (compare_two){
            Call<List<CountryStatsInfo>> call2 = statsApiService.getCasesWithTimeFrame(secondCountry, from_date, to_date);
            call2.enqueue(new Callback<List<CountryStatsInfo>>() {
                @Override
                public void onResponse(Call<List<CountryStatsInfo>> call2, Response<List<CountryStatsInfo>> response) {
                    List<CountryStatsInfo> secondCountryStatsInfo = response.body();
                    System.out.println("[StatsActivity.connect] secondCountryStatsInfo: " + "\n" +secondCountryStatsInfo.isEmpty());
                    if (secondCountryStatsInfo != null) {
                        if (secondCountryStatsInfo.isEmpty()){
                            String reminder = "No data found\nTry another country or data";
                            Toast toast = Toast.makeText(getActivity(), reminder, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            addToHistory(secondCountryStatsInfo);
                            if (waitCountryStats.get() == false){ //if we don't have a single piece of data yet
                                firstCountryStats = secondCountryStatsInfo;
                                waitCountryStats.set(true);
                            }
                            else{ //waitCountryStats was true, we have the other data set in memory
                                RecyclerView recyclerView = rootView.findViewById(R.id.rvStatsList);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new StatsListAdapter(firstCountryStats, secondCountryStatsInfo));
                            }
                        }
                    } else {
                        String reminder = "Invalid Search:\nNo Info";
                        Toast toast = Toast.makeText(getActivity(), reminder, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<List<CountryStatsInfo>> call, Throwable throwable) {
                    Log.e("MyDebugger", throwable.toString());
                }
            });
        }
    }

    private void addToHistory(List<CountryStatsInfo> countryStats){
        SharedPreferences sf = getActivity().getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        String name = sf.getString(getString(R.string.pref_user_name), null);
        if (name != null){
            if (historyRetrofit == null){
                historyRetrofit = new Retrofit.Builder().baseUrl(HistoryAPIService.BASE_HISTORY_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            HistoryAPIService historyApiService = historyRetrofit.create(HistoryAPIService.class);
            CountryStatsInfo csi = countryStats.get(0);
            historyStats hs = new historyStats(name, csi.getCountry(),
                    dateStringHelper.getDateFromQueryDate(from_date),
                    dateStringHelper.getDateFromQueryDate(to_date), csi.getConfirmed());
            Call<historyStats> call = historyApiService.createHistory(hs);
            call.enqueue(new Callback<historyStats>(){
                @Override
                public void onResponse(Call<historyStats> call, Response<historyStats> response){
                    Log.d("MyDebugger", response.toString());
                }
                @Override
                public void onFailure(Call<historyStats> call, Throwable t){
                    Log.d("MyDebugger", t.toString());
                }
            });
        }
    }

}