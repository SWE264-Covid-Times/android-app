package com.example.covidtimes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StatsAPIService {
    String BASE_LIVE_URL = "https://api.covid19api.com/";

    //returns all cases by case type for a country in given date range
    @GET("country/{country}")
    Call<List<CountryStatsInfo>> getCasesWithTimeFrame(@Path("country") String country, @Query("from_date") String fromDate, @Query("to_date") String toDate);
}
