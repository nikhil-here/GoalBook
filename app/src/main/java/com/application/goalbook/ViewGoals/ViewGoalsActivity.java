package com.application.goalbook.ViewGoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.HomeScreen.Adapter_Goals;
import com.application.goalbook.HomeScreen.MainActivity;
import com.application.goalbook.HomeScreen.Pojo_Goal;
import com.application.goalbook.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener {

    private View decorview;
    private ExtendedFloatingActionButton efabAddGoal;
    private RecyclerView rvGoals;
    private Adapter_Goals adapterGoals;
    private ArrayList<Pojo_Goal> pojoGoalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);
        if (savedInstanceState == null)
        {
            initViews();
            initListeners();
            getGoals();
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




    private void setGoals() {
        adapterGoals = new Adapter_Goals(ViewGoalsActivity.this, pojoGoalArrayList);
        rvGoals.setLayoutManager(new LinearLayoutManager(ViewGoalsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvGoals.hasFixedSize();
        rvGoals.setAdapter(adapterGoals);
    }

    private void getGoals() {
        pojoGoalArrayList = new ArrayList<>();
        pojoGoalArrayList.add(new Pojo_Goal("https://eco-business.imgix.net/ebmedia/fileuploads/Feature_RightsofNature_inline2.jpg?fit=crop&h=960&ixlib=django-1.2.0&w=1440", "Kalsubai Trek", "Traveling", "02 Months Remainig", R.color.lightRed));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.thestatesman.com/wp-content/uploads/2020/02/shaw-575x321.jpg", "Playing Cricket", "Sports", "05 Months Remainig", R.color.lightBlue));
        pojoGoalArrayList.add(new Pojo_Goal("https://i.ytimg.com/vi/o8lABLhyI-A/maxresdefault.jpg", "Solo Dance", "Dance", "01 Year Remainig", R.color.lightGreen));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.greengeeks.com/blog/wp-content/uploads/2016/10/website-money.jpg", "Make Money ", "Finance", "3 Years from now", R.color.lightViolet));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.invespcro.com/blog/images/blog-images/main.png", "Plan Year", "Note", "5 Years from now", R.color.lightYellow));
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