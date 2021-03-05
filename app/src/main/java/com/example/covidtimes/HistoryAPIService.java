package com.example.covidtimes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HistoryAPIService {
    String BASE_HISTORY_URL = "http://covidtimes-env.eba-yjpbhcys.us-east-2.elasticbeanstalk.com/covidapi/resources/";

    @POST("useradd")
    Call<historyUser> createUser(@Body historyUser hUser);

    @POST("historyadd")
    Call<historyStats> createHistory(@Body historyStats hStats);

    @GET("history/{name}")
    Call<List<historyStats>> getHistory(@Path("name") String name);
}