package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class StateStatsInfo {
    @SerializedName("tot_cases")
    private int totCases;
    @SerializedName("new_case")
    private int newCase;
    @SerializedName("pnew_case")//new probable cases
    private int pnewCase;
    @SerializedName("tot_death")
    private int totDeath;
    @SerializedName("new_death")
    private int newDeath;
    @SerializedName("pnew_death")//new probable deaths
    private int pnewDeath;

    public StateStatsInfo(String tCase, String nCase, String pnCase, String tDeath, String nDeath, String pnDeath){
        totCases = getInt(tCase);
        newCase = getInt(nCase);
        pnewCase = getInt(pnCase);
        totDeath = getInt(tDeath);
        newDeath = getInt(nDeath);
        pnewDeath = getInt(pnDeath);
    }
    private int getInt(String num){
        return (int) Float.parseFloat(num);
    }
    public int getTotCases(){return totCases;}
    public int getNewCase(){return newCase;}
    public int getPNewCase(){return pnewCase;}
    public int getTotDeath(){return totDeath;}
    public int getNewDeath(){return newDeath;}
    public int getPNewDeath(){return pnewDeath;}
}
