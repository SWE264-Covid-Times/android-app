package com.example.covidtimes;

import java.util.HashMap;
import java.util.List;

// store country slug pair
public  class AllCountrySlug {
    private HashMap<String, String> countrySlug = null;

    public AllCountrySlug(List<CountrySlugInfo> statsInfo) {
        countrySlug = new HashMap<>();
        for (CountrySlugInfo csc : statsInfo)
            countrySlug.put(csc.getCountry(), csc.getSlug());
        System.out.println("finished loading");
    }

    public HashMap<String, String> getCountrySlugPairs() {return countrySlug;}

    public String getSlug(String country) {return countrySlug.get(country);}


}
