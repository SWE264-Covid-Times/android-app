package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class historyUser {
    @SerializedName("name")
    private String uName;

    public historyUser(String n){
        uName = n;
    }
    public String getName(){return uName;}
}
