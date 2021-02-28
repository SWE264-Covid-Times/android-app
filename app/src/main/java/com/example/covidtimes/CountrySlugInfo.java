package com.example.covidtimes;

import com.google.gson.annotations.SerializedName;

// store Country info
public class CountrySlugInfo {
    @SerializedName("Country")
    private String country;
    @SerializedName("Slug")
    private String slug;
    @SerializedName("IS02")
    private String is02;

    public CountrySlugInfo(String country, String slug, String is02){
        this.country = country;
        this.slug = slug;
        this.is02 = is02;
    }
    public String getCountry(){return this.country;}
    public String getSlug(){return this.slug;}
    public String getIs02(){return this.is02;}

}
