package com.example.covidtimes.ui.gallery;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidtimes.AllCountrySlug;
import com.example.covidtimes.CountrySlugInfo;
import com.example.covidtimes.StatsAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<AllCountrySlug> aCS;
    static Retrofit retrofit = null;
    /*public GalleryViewModel() {
        aCS = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }*/
    public LiveData<AllCountrySlug> getCountryList(){
        if (aCS == null){
            aCS = new MutableLiveData<>();
            loadCountryList();
        }
        return aCS;
    }


    private void loadCountryList(){
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
                    AllCountrySlug allCountrySlug = new AllCountrySlug(statsInfo);
                    Log.d("MyDebugger", "loaded country list");
                    aCS.setValue(allCountrySlug);
                } else {
                    aCS.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<CountrySlugInfo>> call, Throwable throwable) {
                Log.e("MyDebugger", throwable.toString());
            }
        });
    }
}