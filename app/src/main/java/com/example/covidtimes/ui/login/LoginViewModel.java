package com.example.covidtimes.ui.login;

import android.location.Address;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtimes.HistoryAPIService;
import com.example.covidtimes.StateStatsInfo;
import com.example.covidtimes.StatsAPIServiceStates;
import com.example.covidtimes.VacProviderInfo;
import com.example.covidtimes.VaccinationStats;
import com.example.covidtimes.dateStringHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<VaccinationStats> vacStats;
    private MutableLiveData<VacProviderInfo> vacProv;
    static Retrofit retrofit = null;
    public LiveData<VaccinationStats> getVacStats(Address uAddress){
        if (vacStats == null){
            vacStats = new MutableLiveData<>();
            loadStateInfo(uAddress);
        }
        return vacStats;
    }
    public LiveData<VacProviderInfo> getVacProvInfo(Address uAddress){
        if (vacProv == null){
            vacProv = new MutableLiveData<>();
            loadVacProv(uAddress);
        }
        return vacProv;
    }
    private void loadVacProv(Address uAddy){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(HistoryAPIService.BASE_HISTORY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        HistoryAPIService apiService = retrofit.create(HistoryAPIService.class);
        String zippy = uAddy.getPostalCode();
        Log.d("MyDebugger", zippy);
        Call<List<VacProviderInfo>> call = apiService.getVacProvider(Integer.parseInt(zippy));
        call.enqueue(new Callback<List<VacProviderInfo>>(){
            @Override
            public void onResponse(Call<List<VacProviderInfo>> call, Response<List<VacProviderInfo>> response){
                List<VacProviderInfo> statInfo = response.body();
                if (statInfo.size() > 0){
                    VacProviderInfo stat = statInfo.get(0);
                    vacProv.setValue(stat);
                }
                else{
                    Log.d("MyDebugger", "Empty onResponse VacProv");
                    vacProv.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<VacProviderInfo>> call, Throwable throwable){
                Log.e("MyDebugger", throwable.toString());
            }
        });
    }
    private void loadStateInfo(Address uAddy){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(HistoryAPIService.BASE_HISTORY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        HistoryAPIService apiService = retrofit.create(HistoryAPIService.class);
        String county = uAddy.getSubAdminArea();
        Log.d("MyDebugger", county);
        String[] temp = county.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length - 1; i++){
            sb.append(temp[i]);
            if (i < temp.length - 2){
                sb.append("%20");
            }
        }
        county = sb.toString();
        Call<List<VaccinationStats>> call = apiService.getVaccinations(county);
        call.enqueue(new Callback<List<VaccinationStats>>(){
            @Override
            public void onResponse(Call<List<VaccinationStats>> call, Response<List<VaccinationStats>> response){
                List<VaccinationStats> statInfo = response.body();
                if (statInfo.size() > 0){
                    VaccinationStats stat = statInfo.get(0);
                    vacStats.setValue(stat);
                }
                else{
                    Log.d("MyDebugger", "Empty onResponse VacStats");
                    vacStats.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<VaccinationStats>> call, Throwable throwable){
                Log.e("MyDebugger", throwable.toString());
            }
        });
    }
}