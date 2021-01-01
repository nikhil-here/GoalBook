package com.application.goalbook.ViewGoals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.ImageSaver;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ViewGoalActivity extends AppCompatActivity implements Observer<Goal> {


    private ImageView ivCover;
    private TextView tvStartEndDate;
    private ChipGroup cgTags, cgReminders;
    private EditText etTitle, etDescription;
    private View vTitle, vTags, vTimeline, vReminderFrequency;

    private Goal goal;
    private ArrayList<String> tags;
    private Long startDate, endDate;
    private int gid, status, reminderFrequency;
    private String title, description, coverImage, color;

    //Viewmodel
    private GoalViewModel goalViewModel;
    public static final String TAG = "ViewGoalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);
        if (savedInstanceState == null) {
            initViews();
            initActionBar();
            getBundle();
            initObserver();
        }
    }

    @Override
    public void onChanged(Goal goal) {
        if (goal!= null) {
            this.goal = goal;
            getValues(goal);
            updateUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_goal_edit:
                Toasty.info(ViewGoalActivity.this, "Edit Goal Work In Progress").show();
                break;
            case R.id.menu_view_goal_delete:
                deleteGoal();
                break;
        }
        return true;
    }

    private void deleteGoal() {
        MaterialAlertDialogBuilder deleteAert = new MaterialAlertDialogBuilder(ViewGoalActivity.this)
                .setTitle(getResources().getString(R.string.alert_delete_goal_title))
                .setMessage(getResources().getString(R.string.alert_delete_goal_message))
                .setNegativeButton(getResources().getString(R.string.decline), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete Cancel

                    }
                })
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goalViewModel.delete(goal);
                        Toasty.warning(ViewGoalActivity.this,"Goal Removed").show();
                        finish();
                    }
                });

        deleteAert.show();
    }


    private void updateUI() {
        etTitle.setText(title);
        etDescription.setText(description);

        //loading cover image
        Bitmap bitmap = new ImageSaver(ViewGoalActivity.this).
                setFileName(coverImage).
                setDirectoryName(Constants.DIRECTORY_NAME).
                load();
        ivCover.setImageBitmap(bitmap);
        //setting view strip colors
        vTitle.setBackgroundColor(Color.parseColor(color));
        vTags.setBackgroundColor(Color.parseColor(color));
        vTimeline.setBackgroundColor(Color.parseColor(color));
        vReminderFrequency.setBackgroundColor(Color.parseColor(color));

        //setting start and end date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
        tvStartEndDate.setText(dateFormat.format(startDate) + " To " + dateFormat.format(endDate));

        //adding tags
        cgTags.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            Chip chip = new Chip(ViewGoalActivity.this);
            chip.setText(tags.get(i));
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
            cgTags.addView(chip);
        }

        //adding reminder frequency
        cgReminders.removeAllViews();
        Chip chip = new Chip(ViewGoalActivity.this);
        switch (reminderFrequency) {
            case Constants.REMINDER_NONE:
                chip.setText("None");
            case Constants.REMINDER_DAILY:
                chip.setText("Daily");
            case Constants.REMINDER_WEEKLY:
                chip.setText("Weekly");
            case Constants.REMINDER_MONTHLY:
                chip.setText("Monthly");
            case Constants.REMINDER_YEARLY:
                chip.setText("Yearly");
        }
        cgReminders.addView(chip);


    }

    private void getValues(Goal goal) {

            tags = goal.getTags();
            color = goal.getColor();
            title = goal.getTitle();
            status = goal.getStatus();
            endDate = goal.getEndDate();
            startDate = goal.getStartDate();
            coverImage = goal.getCoverImage();
            description = goal.getDescription();
            reminderFrequency = goal.getReminderFrequency();


    }


    private void getBundle() {
        gid = getIntent().getIntExtra("id", -1);
    }

    private void initObserver() {
        if (gid != -1) {
            //initializing goalviewmodel
            goalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
            //setting Observer for goals list
            goalViewModel.getGoalById(gid).observe(this, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_goal, menu);
        return true;
    }

    private void initActionBar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle("View Goal");
    }

    private void initViews() {
        vTitle = findViewById(R.id.activity_view_goal_view_title);
        vTags = findViewById(R.id.activity_view_goal_view_add_tag);
        vTimeline = findViewById(R.id.activity_view_goal_view_timeline);
        vReminderFrequency = findViewById(R.id.activity_view_goal_view_reminder_frequencey);
        ivCover = findViewById(R.id.activity_view_goal_iv_goal_cover);
        etTitle = findViewById(R.id.activity_view_goal_et_goal_title);
        etDescription = findViewById(R.id.activity_view_goal_et_goal_description);
        cgTags = findViewById(R.id.activity_view_goal_cg_tags);
        cgReminders = findViewById(R.id.activity_view_goal_cg_reminders);
        tvStartEndDate = findViewById(R.id.activity_view_goal_tv_goal_Start_end_date);
    }


}