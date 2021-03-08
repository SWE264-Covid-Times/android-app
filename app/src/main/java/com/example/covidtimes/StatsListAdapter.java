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

        TextView tvCountry2;
        TextView tvConfirmed2;
        TextView tvDeaths2;
        TextView tvRecovered2;
        TextView tvActive2;
        TextView tvDate2;


        ViewHolder(View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvDeaths = itemView.findViewById(R.id.tvDeaths);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);
            tvActive = itemView.findViewById(R.id.tvActive);
            tvDate = itemView.findViewById(R.id.tvDate);


            tvCountry2 = itemView.findViewById(R.id.tvCountry2);
            tvConfirmed2 = itemView.findViewById(R.id.tvConfirmed2);
            tvDeaths2 = itemView.findViewById(R.id.tvDeaths2);
            tvRecovered2 = itemView.findViewById(R.id.tvRecovered2);
            tvActive2 = itemView.findViewById(R.id.tvActive2);
            tvDate2 = itemView.findViewById(R.id.tvDate2);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_list_adapter2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (hasTwoCountries) {
            if (position < firstCountryStatsInfo.size()) {
                CountryStatsInfo firstCountryStatsInfoInput = firstCountryStatsInfo.get(position);
                holder.tvCountry.setText(firstCountryStatsInfoInput.getCountry());
                holder.tvConfirmed.setText("Confirmed: " + firstCountryStatsInfoInput.getConfirmed().toString());
                holder.tvDeaths.setText("Deaths: " + firstCountryStatsInfoInput.getDeaths().toString());
                holder.tvRecovered.setText("Recovered: " + firstCountryStatsInfoInput.getRecovered().toString());
                holder.tvActive.setText("Active: " + firstCountryStatsInfoInput.getActive().toString());
                holder.tvDate.setText("Date: " + firstCountryStatsInfoInput.getDate().substring(0, 10));
            } else {
                holder.tvCountry.setText("");
                holder.tvConfirmed.setText("");
                holder.tvDeaths.setText("");
                holder.tvRecovered.setText("");
                holder.tvActive.setText("");
                holder.tvDate.setText("");
            }

            if (position < secondCountryStatsInfo.size()) {
                CountryStatsInfo secondCountryStatsInfoInput = secondCountryStatsInfo.get(position);
                holder.tvCountry2.setText(secondCountryStatsInfoInput.getCountry());
                holder.tvConfirmed2.setText("Confirmed: " + secondCountryStatsInfoInput.getConfirmed().toString());
                holder.tvDeaths2.setText("Deaths: " + secondCountryStatsInfoInput.getDeaths().toString());
                holder.tvRecovered2.setText("Recovered: " + secondCountryStatsInfoInput.getRecovered().toString());
                holder.tvActive2.setText("Active: " + secondCountryStatsInfoInput.getActive().toString());
                holder.tvDate2.setText("Date: " + secondCountryStatsInfoInput.getDate().substring(0, 10));
            } else {
                holder.tvCountry.setText("");
                holder.tvConfirmed.setText("");
                holder.tvDeaths.setText("");
                holder.tvRecovered.setText("");
                holder.tvActive.setText("");
                holder.tvDate.setText("");
            }

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
        int itemCount;
        if (hasTwoCountries)
            if (firstCountryStatsInfo.size() >= secondCountryStatsInfo.size())
                itemCount = firstCountryStatsInfo.size();
            else
                itemCount = secondCountryStatsInfo.size();
        else
            itemCount = statsInfo.size();
        return itemCount;
    }
}
