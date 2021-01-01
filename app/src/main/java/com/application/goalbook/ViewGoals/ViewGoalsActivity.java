package com.application.goalbook.ViewGoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.HomeScreen.Adapter_Goals;
import com.application.goalbook.HomeScreen.MainActivity;
import com.application.goalbook.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener, Observer<List<Goal>>, Adapter_Goals.ViewGoalInterface {

    private View decorview;
    private ExtendedFloatingActionButton efabAddGoal;
    private RecyclerView rvGoals;
    private Adapter_Goals adapterGoals;
    private List<Goal> pojoGoalArrayList;

    //Viewmodel
    private GoalViewModel goalViewModel;
    public static final String TAG = "ViewGoalsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);

        //initializing goalviewmodel
        goalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
        //setting Observer for goals list
        goalViewModel.getAllGoals().observe(this, this);

        initViews();
        initListeners();
        setGoals();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_view_goals_efab_add_goals:
                Intent addGoalIntent = new Intent(ViewGoalsActivity.this, AddGoalActivity.class);
                startActivity(addGoalIntent);
                break;
        }
    }


    @Override
    public void onChanged(List<Goal> goals) {
        pojoGoalArrayList = goals;
        adapterGoals.submitList(goals);
    }


    private void setGoals() {
        adapterGoals = new Adapter_Goals(ViewGoalsActivity.this, this);
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

    @Override
    public void onViewGoalClick(View view, int position) {
        Intent viewGoalIntent = new Intent(ViewGoalsActivity.this, ViewGoalActivity.class);
        viewGoalIntent.putExtra("id", pojoGoalArrayList.get(position).getGid());
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(view.findViewById(R.id.component_goal_iv_cover), "cover");
        pairs[1] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_title), "title");
        pairs[2] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_description), "description");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(viewGoalIntent, activityOptions.toBundle());
    }

    @Override
    public void onEditGoalClick(View view,int position) {
        Intent viewGoalIntent = new Intent(ViewGoalsActivity.this, AddGoalActivity.class);
        viewGoalIntent.putExtra("id", pojoGoalArrayList.get(position).getGid());

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(view.findViewById(R.id.component_goal_iv_cover), "cover");
        pairs[1] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_title), "title");
        pairs[2] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_description), "description");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(viewGoalIntent, activityOptions.toBundle());
    }
}