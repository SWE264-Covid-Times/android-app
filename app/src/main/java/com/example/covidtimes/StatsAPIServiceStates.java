package com.example.covidtimes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StatsAPIServiceStates {
    String BASE_STATE_URL = "https://data.cdc.gov/resource/";
    //docs: https://dev.socrata.com/foundry/data.cdc.gov/9mfq-cb36

    //ex: https://data.cdc.gov/resource/9mfq-cb36.json?submission_date=2020-03-28T00:00:00.000&state=CA
    @GET("9mfq-cb36.json")
    Call<List<StateStatsInfo>> getStateCasesByDate(@Query("submission_date") String subDate, @Query("state") String state);

}
