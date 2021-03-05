package com.example.covidtimes.ui.about;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtimes.StateStatsInfo;
import com.example.covidtimes.StatsAPIServiceStates;
import com.example.covidtimes.dateStringHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboutViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<StateStatsInfo> stateStatInfo;
    static Retrofit retrofit = null;
    private int offset = 1;
    public LiveData<StateStatsInfo> getStateStatInfo(String state){
        if (stateStatInfo == null){
            stateStatInfo = new MutableLiveData<>();
            loadStateInfo(state);
        }
        return stateStatInfo;
    }
    private void loadStateInfo(String state){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(StatsAPIServiceStates.BASE_STATE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StatsAPIServiceStates apiService = retrofit.create(StatsAPIServiceStates.class);
        String stateAbbrev = dateStringHelper.stateToAbbrev(state);
        Call<List<StateStatsInfo>> call = apiService.getStateCasesByDate(dateStringHelper.getCurrentStateQueryableDate(offset), stateAbbrev);
        call.enqueue(new Callback<List<StateStatsInfo>>(){
            @Override
            public void onResponse(Call<List<StateStatsInfo>> call, Response<List<StateStatsInfo>> response){
                List<StateStatsInfo> stateInfo = response.body();
                if (stateInfo.size() > 0){
                    StateStatsInfo stat = stateInfo.get(0);
                    stateStatInfo.setValue(stat);
                }
                else{
                    offset++;
                    loadStateInfo(stateAbbrev);
                }
            }
            @Override
            public void onFailure(Call<List<StateStatsInfo>> call, Throwable throwable){
                Log.e("MyDebugger", throwable.toString());
            }
        });
    }
}