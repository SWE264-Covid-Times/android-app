package com.example.covidtimes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// store all country slug pair
public  class AllCountrySlug {
    private HashMap<String, String> countrySlug = null;

    public AllCountrySlug(List<CountrySlugInfo> statsInfo) {
        countrySlug = new HashMap<>();
        for (CountrySlugInfo csc : statsInfo)
            countrySlug.put(csc.getCountry(), csc.getSlug());
        System.out.println("finished loading");
//        sort();
    }

    private void sort() {
        List<String> key = new ArrayList<>(countrySlug.keySet());
        Collections.sort(key);
    }
    public HashMap<String, String> getCountrySlugPairs() {return countrySlug;}

    public String getSlug(String country) {return countrySlug.get(country);}


}
