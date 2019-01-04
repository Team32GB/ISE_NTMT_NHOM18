package com.example.team32gb.jobit.View.RecentSearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.team32gb.jobit.R;
import com.example.team32gb.jobit.View.ListJobSearch.ListJobSearchActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecenteSearchLayoutAdapter extends RecyclerView.Adapter<RecenteSearchLayoutAdapter.MyViewHolder> {

    Context context;
    List<Search> searches;

    public RecenteSearchLayoutAdapter(Context context, List<Search> searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recent_search_layout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StringBuilder strSearch = new StringBuilder();
        String timkiem = searches.get(position).getTimkiem();
        String diadiem = searches.get(position).getDiaDiem();

        strSearch.append(timkiem);
        if(!diadiem.isEmpty()){
            strSearch.append(" - ").append(diadiem);
        }
        holder.txtSearch.setText(strSearch);
        final Bundle bundle = new Bundle();
        bundle.putString("timKiem",timkiem);
        bundle.putString("diaDiem",diadiem);

        holder.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ListJobSearchActivity.class);
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSearch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSearch = itemView.findViewById(R.id.tvSearch);
        }
    }
}
