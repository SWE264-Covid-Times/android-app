package com.example.covidtimes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StatsAPIService {
    String BASE_LIVE_URL = "https://api.covid19api.com/";

    // get all cases by case type for a country in given date range
    @GET("total/country/{country}")
    Call<List<CountryStatsInfo>> getCasesWithTimeFrame(@Path("country") String country, @Query("from") String fromDate, @Query("to") String toDate);

    // get all countries and their slugs
    @GET("countries")
    Call<List<CountrySlugInfo>> getCountrySlug();

}
