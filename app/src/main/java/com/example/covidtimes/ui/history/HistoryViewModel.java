package com.example.covidtimes.ui.history;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.covidtimes.HistoryAPIService;
import com.example.covidtimes.historyStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<historyStats>> histories;
    static Retrofit retrofit = null;
    public LiveData<List<historyStats>> getHistories(String name){
        if (histories == null){
            histories = new MutableLiveData<List<historyStats>>();
            loadHistories(name);
        }
        return histories;
    }
    private void loadHistories(String name){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(HistoryAPIService.BASE_HISTORY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d("MyDebugger", "UUID: "+name);
        HistoryAPIService historyApiService = retrofit.create(HistoryAPIService.class);
        Call<List<historyStats>> call = historyApiService.getHistory(name);
        call.enqueue(new Callback<List<historyStats>>(){
            @Override
            public void onResponse(Call<List<historyStats>> call, Response<List<historyStats>> response){
                Log.d("MyDebugger", response.toString());
                if (response.body() == null){
                    List<historyStats> nullStats = new ArrayList<>();
                    nullStats.add(new historyStats("ERROR", "Something went wrong", "Response message",
                            response.message(), response.code()));
                    histories.setValue(nullStats);
                }
                else{
                    histories.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<historyStats>> call, Throwable t){
                Log.d("MyDebugger", t.toString());
            }
        });
    }
}