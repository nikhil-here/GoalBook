package com.application.goalbook.ViewGoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.HomeScreen.Adapter_Goals;
import com.application.goalbook.HomeScreen.MainActivity;
import com.application.goalbook.HomeScreen.Pojo_Goal;
import com.application.goalbook.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener, Observer<List<Goal>> {

    private View decorview;
    private ExtendedFloatingActionButton efabAddGoal;
    private RecyclerView rvGoals;
    private Adapter_Goals adapterGoals;

    //Viewmodel
    private GoalViewModel goalViewModel;
    public static final String TAG = "ViewGoalsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);
        if (savedInstanceState == null)
        {
            //initializing goalviewmodel
            goalViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
            //setting Observer for goals list
            goalViewModel.getAllGoals().observe(this,this);

            initViews();
            initListeners();
            setGoals();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_view_goals_efab_add_goals:
                Intent addGoalIntent = new Intent(ViewGoalsActivity.this, AddGoalActivity.class);
                startActivity(addGoalIntent);
                break;
        }
    }


    @Override
    public void onChanged(List<Goal> goals) {
        adapterGoals.submitList(goals);
    }


    private void setGoals() {
        adapterGoals = new Adapter_Goals(ViewGoalsActivity.this);
        rvGoals.setLayoutManager(new LinearLayoutManager(ViewGoalsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvGoals.hasFixedSize();
        rvGoals.setAdapter(adapterGoals);
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        rvGoals = findViewById(R.id.activity_view_goals_rv);
        efabAddGoal = findViewById(R.id.activity_view_goals_efab_add_goals);
    }

    private void initListeners() {
        efabAddGoal.setOnClickListener(this);
    }


    //removing navigation buttons (on-screen buttons)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorview.setSystemUiVisibility(hideSystemBars());
        }
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == 0) {
            decorview.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        int i = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        return i;
    }

}