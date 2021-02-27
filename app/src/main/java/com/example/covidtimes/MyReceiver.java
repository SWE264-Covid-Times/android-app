package com.example.covidtimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyReceiver extends BroadcastReceiver {
    private static Retrofit retrofit = null;
    private Context context;


    @Override
    public void onReceive(Context c, Intent i){
        //Toast.makeText(c, "New cases: " , Toast.LENGTH_LONG).show();
        context = c;
        connect();
    }
    private void connect(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(StatsAPIServiceStates.BASE_STATE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StatsAPIServiceStates apiService = retrofit.create(StatsAPIServiceStates.class);
        Call<List<StateStatsInfo>> call = apiService.getStateCasesByDate(getDate(), "CA");
        call.enqueue(new Callback<List<StateStatsInfo>>(){
            @Override
            public void onResponse(Call<List<StateStatsInfo>> call, Response<List<StateStatsInfo>> response){
                List<StateStatsInfo> stateInfo = response.body();
                if (stateInfo.size() > 0){
                    StateStatsInfo stat = stateInfo.get(0);
                    Toast.makeText(context, "New cases: "  + stat.getNewCase(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "No updates found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<StateStatsInfo>> call, Throwable throwable){
                Log.e("Broadcast Receiver", throwable.toString());
            }
        });
    }
    private String getDate(){
        return dateStringHelper.getCurrentStateQueryableDate();
    }
}
