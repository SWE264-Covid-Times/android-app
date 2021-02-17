package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class CountryStatsInfo {
    @SerializedName("ID")
    private String caseID;
    @SerializedName("Country")
    private String caseCountry;
    @SerializedName("CountryCode")
    private String caseCountryCode;
    @SerializedName("Confirmed")
    private Integer caseConfirmed;
    @SerializedName("Deaths")
    private Integer caseDeaths;
    @SerializedName("Recovered")
    private Integer caseRecovered;
    @SerializedName("Active")
    private Integer caseActive;
    @SerializedName("Date")
    private String caseDate;

    public CountryStatsInfo(String ID, String Country, String Code, int confirmed, int deaths, int recovered, int active, String date){
        caseID = ID;
        caseCountry = Country;
        caseCountryCode = Code;
        caseConfirmed = confirmed;
        caseDeaths = deaths;
        caseRecovered = recovered;
        caseActive = active;
        caseDate = date;
    }
    public String getID(){return caseID;}
    public String getCountry(){return caseCountry;}
    public String getCountryCode(){return caseCountryCode;}
    public Integer getConfirmed(){return caseConfirmed;}
    public Integer getDeaths(){return caseDeaths;}
    public Integer getRecovered(){return caseRecovered;}
    public Integer getActive(){return caseActive;}
    public String getDate(){return caseDate;}
}
