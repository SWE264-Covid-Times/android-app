package com.example.covidtimes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class locationHelper{

    public locationHelper(Context c){
        locManager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
        context = c;
        bestProvider = locManager.getBestProvider(new Criteria(), true);
    }

    public String getCurrentCountry(){
        List<Address> geoResult = getAddress();
        String result = "US";
        if (geoResult.size() > 0){
            result = geoResult.get(0).getCountryCode();
        }
        return result;
    }
    public String getCurrentState(){
        List<Address> geoResult = getAddress();
        String result = "CA"; //default
        if (geoResult.size() > 0){
            result = geoResult.get(0).getAdminArea();
        }
        return result;
    }


    private LocationManager locManager;
    private String bestProvider;
    private Context context;
    public static final int PERMISSION_REQUEST_CODE = 698;

    /**
     * Checks if location permissions are enabled. If not, returns false and requests
     * permissions.
     * Overload following method to handle:
     * public void onRequestPermissionsResult(int, String[], int[])
     * @return true if location permissions are enabled
     */
    private boolean handlePermissions(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        else{
            return true;
        }
    }

    private List<Address> getAddress(){
        List<Address> result = new ArrayList<>();
        if (handlePermissions()){
            @SuppressLint("MissingPermission")
            Location lastKnown = locManager.getLastKnownLocation(bestProvider);
            Geocoder g = new Geocoder(context);
            try{
                result = g.getFromLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), 1);
            } catch (IOException e){
                Log.v("locationHelper", "IOException from getFomLocaiton");
            }
        }
        else{
            Log.v("locationHelper", "handle no permissions");
        }
        return result;
    }


}
