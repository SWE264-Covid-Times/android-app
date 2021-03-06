package com.example.covidtimes;

import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

// store all country slug pair
public class AllCountrySlug {
    private TreeMap<String, String> countrySlug = null;

    public AllCountrySlug(List<CountrySlugInfo> statsInfo) {
        countrySlug = new TreeMap<>();
        for (CountrySlugInfo csc : statsInfo)
            countrySlug.put(csc.getCountry(), csc.getSlug());
        System.out.println("[AllCountrySlug] finished loading");
    }

    public TreeMap<String, String> getCountrySlugPairs() {return countrySlug;}

    public String getSlug(String country) {return countrySlug.get(country);}

    public boolean isEmpty() {
        if (countrySlug == null || countrySlug.isEmpty())
            return true;
        else
            return false;
    }
}
