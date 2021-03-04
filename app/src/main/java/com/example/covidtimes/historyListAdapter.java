package com.example.covidtimes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class historyListAdapter extends RecyclerView.Adapter<historyListAdapter.ViewHolder>{
    private List<historyStats> mData;
    public historyListAdapter(List<historyStats> data){this.mData = data;}
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView historyCaseCount;
        TextView historySearchTerm;
        TextView historyDateRange;
        ViewHolder (View itemView){
            super(itemView);
            historyCaseCount = itemView.findViewById(R.id.historyCaseCount);
            historySearchTerm = itemView.findViewById(R.id.historySearchTerm);
            historyDateRange = itemView.findViewById(R.id.historyDateRange);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        historyStats currentHistory = mData.get(position);
        holder.historyCaseCount.setText(currentHistory.getCaseCount());
        holder.historySearchTerm.setText(currentHistory.getSearchTerm());
        holder.historyDateRange.setText(currentHistory.getFromDate() + " - " + currentHistory.getToDate());
    }
    @Override
    public int getItemCount(){return mData.size();}
}
