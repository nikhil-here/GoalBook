package com.application.goalbook.HomeScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.goalbook.R;

import java.util.ArrayList;

public class Adapter_Ads extends RecyclerView.Adapter<Adapter_Ads.ViewHolder> {

    private ArrayList<Integer> adList ;

    public Adapter_Ads(ArrayList<Integer> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_ad,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivAd.setImageResource(adList.get(position));
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAd = itemView.findViewById(R.id.component_ad_iv);
        }
    }
}
