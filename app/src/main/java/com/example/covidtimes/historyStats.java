package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class historyStats {
    @SerializedName("name")
    private String uName;
    @SerializedName("searchterm")
    private String uSearchTerm;
    @SerializedName("fromdate")
    private String uFromDate;
    @SerializedName("todate")
    private String uToDate;
    @SerializedName("casecount")
    private int uCaseCount;

    public historyStats(String userName, String userSearchTerm, String userFromDate, String userToDate, int userCaseCount){
        uName = userName;
        uSearchTerm = userSearchTerm;
        uFromDate = userFromDate;
        uToDate = userToDate;
        uCaseCount = userCaseCount;
    }
    public String getName(){return uName;}
    public String getSearchTerm(){return uSearchTerm;}
    public String getFromDate(){return uFromDate;}
    public String getToDate(){return uToDate;}
    public int getCaseCount(){return uCaseCount;}

}
