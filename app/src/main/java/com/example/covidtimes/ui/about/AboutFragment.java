package com.example.covidtimes.ui.about;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.covidtimes.R;
import com.example.covidtimes.StatsActivity;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v("aboutFragment", "inside AboutFragment.java");
        rootView = inflater.inflate(R.layout.about_fragment, container, false);
        Button infoExpButton = (Button)rootView.findViewById(R.id.btnInfo_Exp);
        infoExpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("MyDebugger", "inside onclick");
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
        Log.v("aboutFragment", "inside onActivityCreated");
        Button infoExpButton = (Button)rootView.findViewById(R.id.btnInfo_Exp);
        if (infoExpButton != null){
            Log.v("AboutFragment", infoExpButton.toString());
        }

    }

    public static void aboutClick(View view){
        Log.v("appMainPage", "aboutOnClick");
        //get context through view.getContext()
    }

}