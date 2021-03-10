package com.example.covidtimes;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    //Call<ResponseBody> createUser(@Body RequestBody params);

    @POST("historyadd")
    Call<historyStats> createHistory(@Body historyStats hStats);

    @GET("history/{name}")
    Call<List<historyStats>> getHistory(@Path("name") String name);

    @GET("vaccinations/{county}")
    Call<List<VaccinationStats>> getVaccinations(@Path(value = "county", encoded = true)String county);

    @GET("vaccinations/irvine/{zipcode}")
    Call<List<VacProviderInfo>> getVacProvider(@Path("zipcode") int zipcode);

}
