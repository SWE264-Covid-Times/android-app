package com.example.covidtimes.ui.about;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.covidtimes.R;
import com.example.covidtimes.StatsActivity;
import com.example.covidtimes.locationHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.covidtimes.locationHelper.PERMISSION_REQUEST_CODE;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    private View rootView;
    private TextView jhuLink;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.about_fragment, container, false);
        jhuLink = rootView.findViewById(R.id.tvInfo_JHULink);
        jhuLink.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
        fetchUserLocation();
    }

    private void fetchUserLocation(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            TextView title = (TextView) rootView.findViewById(R.id.btnInfo_Exp);
            title.setText("Permission denied");
        }
        else{
            Context context = getActivity();
            LocationManager locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            locManager.requestLocationUpdates(locManager.getBestProvider(new Criteria(), true),
                    0,0,
                    new LocationListener(){
                        @Override
                        public void onLocationChanged(Location location){
                            Location lastKnown = location;
                            locManager.removeUpdates(this);
                            Geocoder g = new Geocoder(context);
                            List<Address> result = new ArrayList<>();
                            try{
                                result = g.getFromLocation(lastKnown.getLatitude(), lastKnown.getLongitude(), 1);
                            } catch (IOException e){
                                Log.v("locationHelper", "IOException from getFomLocation");
                            }
                            if (result.size() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                if (getView() != null){
                                    updateTextViews(locationHelper.getCurrentState(result.get(0)));
                                }
                            }
                        }
                        @Override public void onStatusChanged(String provider, int status, Bundle extras){}
                        @Override public void onProviderEnabled(String provider){}
                        @Override public void onProviderDisabled(String provider){}
                    }, null);
        }
    }

    private void updateTextViews(String user_loc){
        mViewModel.getStateStatInfo(user_loc).observe(getViewLifecycleOwner(), stateStatInfo -> {
            TextView title = (TextView) rootView.findViewById(R.id.btnInfo_Exp);
            TextView detail1 = (TextView) rootView.findViewById(R.id.stateStatdetail1);
            TextView detail2 = (TextView) rootView.findViewById(R.id.stateStatdetail2);
            TextView detail3 = (TextView) rootView.findViewById(R.id.stateStatdetail3);
            TextView detail4 = (TextView) rootView.findViewById(R.id.stateStatdetail4);
            title.setText(getString(R.string.detected_loc) +" " +user_loc);
            detail1.setText(getString(R.string.total_cases)+" " + stateStatInfo.getTotCases());
            detail2.setText(getString(R.string.new_cases)+" " + (stateStatInfo.getNewCase() + stateStatInfo.getPNewCase()));
            detail3.setText(getString(R.string.total_deaths)+" " + stateStatInfo.getTotDeath());
            detail4.setText(getString(R.string.new_deaths)+" " + (stateStatInfo.getNewDeath() + stateStatInfo.getPNewDeath()));
        });
    }

}