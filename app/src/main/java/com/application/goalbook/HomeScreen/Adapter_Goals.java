package com.application.goalbook.HomeScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.goalbook.R;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class Adapter_Goals extends RecyclerView.Adapter<Adapter_Goals.ViewHolder> {

    private ArrayList<Pojo_Goal> pojoGoalArrayList;
    private Context context;

    public Adapter_Goals(Context context,ArrayList<Pojo_Goal> pojoGoalArrayList) {
        this.context = context;
        this.pojoGoalArrayList = pojoGoalArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_goal,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(pojoGoalArrayList.get(position).getTitle());
        holder.tvTitle.setText(pojoGoalArrayList.get(position).getTitle());
        holder.tvRemainingTime.setText(pojoGoalArrayList.get(position).getRemainingTime());
        holder.viewColor.setBackgroundResource(pojoGoalArrayList.get(position).getColorId());
        holder.chipGoalTag.setChipBackgroundColorResource(pojoGoalArrayList.get(position).getColorId());
        holder.chipGoalTag.setText(pojoGoalArrayList.get(position).getTag());
        Glide.with(context)
                .load(pojoGoalArrayList.get(position).getCoverUrl())
                .centerCrop()
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return pojoGoalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvTitle,tvRemainingTime;
        private View viewColor;
        private Chip chipGoalTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.component_goal_iv_cover);
            tvTitle = itemView.findViewById(R.id.component_goal_tv_goal_title);
            tvRemainingTime = itemView.findViewById(R.id.component_goal_tv_remaining_time);
            viewColor = itemView.findViewById(R.id.component_goal_view_goal_color);
            chipGoalTag = itemView.findViewById(R.id.component_goal_chip_goal_tag);
        }
    }
}
