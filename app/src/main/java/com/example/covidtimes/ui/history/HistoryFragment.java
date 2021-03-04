package com.example.covidtimes.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidtimes.HistoryAPIService;
import com.example.covidtimes.R;
import com.example.covidtimes.historyListAdapter;
import com.example.covidtimes.historyStats;
import com.example.covidtimes.historyUser;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private View rootView;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.history_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        // TODO: Use the ViewModel
        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        mViewModel.getHistories(prefs.getString(getString(R.string.pref_user_name), "")).observe(getViewLifecycleOwner(), histories ->{
            RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.historyRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new historyListAdapter(histories));
        });
    }
}