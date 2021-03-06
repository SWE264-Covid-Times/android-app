package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

public class VacProviderInfo {
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("provider")
    private String provider;
    @SerializedName("zipcode")
    private int zipcode;
    public VacProviderInfo(String address, String phone, String provider, int zipcode){
        this.address = address;
        this.phone = phone;
        this.provider = provider;
        this.zipcode = zipcode;
    }
    public String getAddress(){return address;}
    public String getPhone(){return phone;}
    public String getProvider(){return provider;}
    public int getZipcode(){return zipcode;}
}
