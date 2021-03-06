package com.example.covidtimes.ui.login;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.covidtimes.R;
import com.example.covidtimes.locationHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.covidtimes.locationHelper.PERMISSION_REQUEST_CODE;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    private View rootView;
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
                                updateTextViews(result.get(0));
                            }
                        }
                        @Override public void onStatusChanged(String provider, int status, Bundle extras){}
                        @Override public void onProviderEnabled(String provider){}
                        @Override public void onProviderDisabled(String provider){}
                    }, null);
        }
    }

    private void updateTextViews(Address userLoc){
        mViewModel.getVacStats(userLoc).observe(getViewLifecycleOwner(), vacStats->{
            if (vacStats == null){
                Log.d("MyDebugger", "County not in DB");
            }
            else{
                Log.d("MyDebugger", "DosesAdmin: " + vacStats.getDoses_administered());
            }
        });
        mViewModel.getVacProvInfo(userLoc).observe(getViewLifecycleOwner(), vacProv ->{
            if (vacProv == null){ //zip not in db
                Log.d("MyDebugger", "zip not in db");
            }
            else{
                Log.d("MyDebugger", vacProv.getAddress());
            }
        });
    }

}