package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class AllCaseType {
    @SerializedName("ID")
    private String caseID;
    @SerializedName("Country")
    private String caseCountry;
    @SerializedName("CountryCode")
    private String caseCountryCode;
    @SerializedName("Confirmed")
    private int caseConfirmed;
    @SerializedName("Deaths")
    private int caseDeaths;
    @SerializedName("Recovered")
    private int caseRecovered;
    @SerializedName("Active")
    private int caseActive;
    @SerializedName("Date")
    private String caseDate;

    public AllCaseType(String ID, String Country, String Code, int confirmed, int deaths, int recovered, int active, String date){
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
    public int getConfirmed(){return caseConfirmed;}
    public int getDeaths(){return caseDeaths;}
    public int getRecovered(){return caseRecovered;}
    public int getActive(){return caseActive;}
    public String getDate(){return caseDate;}
}
