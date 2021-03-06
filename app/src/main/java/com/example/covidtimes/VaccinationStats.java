package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class VaccinationStats {
    @SerializedName("county")
    private String county;
    @SerializedName("date")
    private String date;
    @SerializedName("doses_administered")
    private int doses_administered;
    @SerializedName("new_doses_administered")
    private int new_doses_administered;

    public VaccinationStats(String county, String date, int doses_administered, int new_doses_administered){
        this.county = county;
        this.date = date;
        this.doses_administered = doses_administered;
        this.new_doses_administered = new_doses_administered;
    }
    public String getCounty(){return county;}
    public String getDate(){return date;}
    public int getDoses_administered(){return doses_administered;}
    public int getNew_doses_administered(){return new_doses_administered;}
}
