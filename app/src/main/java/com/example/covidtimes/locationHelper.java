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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class locationHelper{

    public locationHelper(Context c, BiConsumer<Address, Context> consumer){
        locManager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
        context = c;
        this.consumer = consumer;
        //handlePermissions();
    }

    public static String getCurrentCountry(Address a){
        return a.getCountryCode();
    }
    public static String getCurrentState(Address a){
        return a.getAdminArea();
    }
    public static String getCurrentCounty(Address a){return a.getSubAdminArea();}



    private LocationManager locManager;
    private Context context;
    public static final int PERMISSION_REQUEST_CODE = 698;
    private Location lastKnown;
    private BiConsumer<Address, Context> consumer;

    /**
     * Checks if location permissions are enabled. If not, returns false and requests
     * permissions.
     * Overload following method to handle:
     * public void onRequestPermissionsResult(int, String[], int[])
     * @return true if location permissions are enabled
     */
    public void handlePermissions(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
        else{
            locManager.requestLocationUpdates(locManager.getBestProvider(new Criteria(), true),
                    0,0,
                    new LocationListener(){
                @Override
                public void onLocationChanged(Location location){
                    lastKnown = location;
                    locManager.removeUpdates(this);
                    Geocoder g = new Geocoder(context);
                    List<Address> result = new ArrayList<>();
                    try{
                        result = g.getFromLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), 1);
                    } catch (IOException e){
                        Log.v("locationHelper", "IOException from getFomLocation");
                    }
                    if (result.size() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        consumer.accept(result.get(0), context);
                    }
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras){}
                @Override public void onProviderEnabled(String provider){}
                @Override public void onProviderDisabled(String provider){}
            }, null);
        }
    }
/*
    private List<Address> getAddress(){
        List<Address> result = new ArrayList<>();
        //@SuppressLint("MissingPermission")
        //Location lastKnown = locManager.getLastKnownLocation(bestProvider);
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions((Activity)context,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    PERMISSION_REQUEST_CODE);
//            return result;
//        }
        Geocoder g = new Geocoder(context);
        if (lastKnown != null){
            try{
                result = g.getFromLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), 1);
            } catch (IOException e){
                Log.v("locationHelper", "IOException from getFomLocation");
            }
        }
        return result;
    }*/


}
