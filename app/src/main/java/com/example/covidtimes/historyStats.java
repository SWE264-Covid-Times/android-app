package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class historyStats {
    @SerializedName("name")
    private String name;
    @SerializedName("searchterm")
    private String searchterm;
    @SerializedName("fromdate")
    private String fromdate;
    @SerializedName("todate")
    private String todate;
    @SerializedName("casecount")
    private int casecount;

    public historyStats(String userName, String userSearchTerm, String userFromDate, String userToDate, int userCaseCount){
        name = userName;
        searchterm = userSearchTerm;
        fromdate = userFromDate;
        todate = userToDate;
        casecount = userCaseCount;
    }
    public String getName(){return name;}
    public String getSearchTerm(){return searchterm;}
    public String getFromDate(){return fromdate;}
    public String getToDate(){return todate;}
    public int getCaseCount(){return casecount;}

}
