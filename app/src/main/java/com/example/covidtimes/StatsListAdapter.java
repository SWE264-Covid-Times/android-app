package com.example.covidtimes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class StatsListAdapter extends RecyclerView.Adapter<StatsListAdapter.ViewHolder> {
    private List<CountryStatsInfo> statsInfo;
    private boolean hasTwoCountries;
    private List<CountryStatsInfo> firstCountryStatsInfo;
    private List<CountryStatsInfo> secondCountryStatsInfo;


    StatsListAdapter(List<CountryStatsInfo> statsInfo) {
        this.hasTwoCountries = false;
        this.statsInfo = statsInfo;
    }
    StatsListAdapter(List<CountryStatsInfo> firstCountrysStatsInfo, List<CountryStatsInfo> secondCountrysStatsInfo) {
        this.hasTwoCountries = true;
        this.firstCountryStatsInfo = firstCountrysStatsInfo;
        this.secondCountryStatsInfo = secondCountrysStatsInfo;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountry;
        TextView tvConfirmed;
        TextView tvDeaths;
        TextView tvRecovered;
        TextView tvActive;
        TextView tvDate;


        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvDeaths = itemView.findViewById(R.id.tvDeaths);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);
            tvActive = itemView.findViewById(R.id.tvActive);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (hasTwoCountries) {
            CountryStatsInfo firstCountryStatsInfoInput = firstCountryStatsInfo.get(position);
            CountryStatsInfo secondCountryStatsInfoInput = secondCountryStatsInfo.get(position);
            holder.tvCountry.setText(firstCountryStatsInfoInput.getCountry() +  " " + secondCountryStatsInfoInput.getCountry());
            holder.tvConfirmed.setText("Confirmed: " + firstCountryStatsInfoInput.getConfirmed().toString() + " " + secondCountryStatsInfoInput.getConfirmed().toString());
            holder.tvDeaths.setText("Deaths: " + firstCountryStatsInfoInput.getDeaths().toString() + " " + secondCountryStatsInfoInput.getDeaths().toString());
            holder.tvRecovered.setText("Recovered: " + firstCountryStatsInfoInput.getRecovered().toString() + " " + secondCountryStatsInfoInput.getRecovered().toString());
            holder.tvActive.setText("Active: " + firstCountryStatsInfoInput.getActive().toString() + " " + secondCountryStatsInfoInput.getActive().toString());
            holder.tvDate.setText("Date: " + firstCountryStatsInfoInput.getDate().substring(0,10) + " " + secondCountryStatsInfoInput.getDate().substring(0,10));
        } else {
            CountryStatsInfo countryStatsInfo = statsInfo.get(position);
            holder.tvCountry.setText(countryStatsInfo.getCountry());
            holder.tvConfirmed.setText("Confirmed: " + countryStatsInfo.getConfirmed().toString());
            holder.tvDeaths.setText("Deaths: " + countryStatsInfo.getDeaths().toString());
            holder.tvRecovered.setText("Recovered: " + countryStatsInfo.getRecovered().toString());
            holder.tvActive.setText("Active: " + countryStatsInfo.getActive().toString());
            holder.tvDate.setText("Date: " + countryStatsInfo.getDate().substring(0,10));
        }
    }

    @Override
    public int getItemCount() {
        if (hasTwoCountries)
            return firstCountryStatsInfo.size();
        else
            return statsInfo.size();
    }
}
