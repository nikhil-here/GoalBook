package com.application.goalbook.HomeScreen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.ImageSaver;
import com.application.goalbook.Utility.StringFormatter;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter_Goals extends ListAdapter<Goal, Adapter_Goals.ViewHolder> {

    private Goal goal;
    private Context context;
    private ArrayList<String> tags;
    private String title, description, color, coverImage;
    private Long startDate, endDate;


    public static final String TAG = "Adapter_Goals";

    public Adapter_Goals(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Goal> DIFF_CALLBACK = new DiffUtil.ItemCallback<Goal>() {
        @Override
        public boolean areItemsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
            Boolean areItemsTheSame = oldItem.getGid() == newItem.getGid();
            return areItemsTheSame;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {

            Boolean areContentsTheSame =
                    oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getCoverImage().equals(newItem.getCoverImage()) &&
                    oldItem.getColor().equals(newItem.getColor()) &&
                    oldItem.getStartDate().equals(newItem.getStartDate()) &&
                    oldItem.getEndDate().equals(newItem.getEndDate()) &&
                    oldItem.getTags().equals(newItem.getTags()) &&
                    oldItem.getReminderFrequency() ==(newItem.getReminderFrequency()) ;
            return areContentsTheSame;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_goal,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        goal = getItem(position);
        title = goal.getTitle();
        description = goal.getDescription();
        color = goal.getColor();
        tags = goal.getTags();
        coverImage = goal.getCoverImage();
        startDate = goal.getStartDate();
        endDate = goal.getEndDate();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
        Log.i(TAG, "onBindViewHolder: enDate Date "+dateFormat.format(endDate));
        Log.i(TAG, "onBindViewHolder: endDate long "+endDate);

        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);
        holder.viewColor.setBackgroundColor(Color.parseColor(color));

        holder.cgTags.removeAllViews();
        for (int i = 0; i < tags.size(); i++)
        {
            Chip chip = new Chip(context);
            chip.setText(tags.get(i));
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
            holder.cgTags.addView(chip);
        }

        Bitmap bitmap = new ImageSaver(context).
                setFileName(coverImage).
                setDirectoryName(Constants.DIRECTORY_NAME).
                load();

        holder.ivCover.setImageBitmap(bitmap);

        holder.tvRemainingTime.setText(goal.getRemainingTime());
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvTitle,tvDescription, tvRemainingTime;
        private View viewColor;
        private ChipGroup cgTags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.component_goal_iv_cover);
            tvTitle = itemView.findViewById(R.id.component_goal_tv_goal_title);
            tvDescription = itemView.findViewById(R.id.component_goal_tv_goal_description);
            tvRemainingTime = itemView.findViewById(R.id.component_goal_tv_remaining_time);
            viewColor = itemView.findViewById(R.id.component_goal_view_goal_color);
            cgTags = itemView.findViewById(R.id.component_goal_cg_goal_tag);
        }
    }
}
