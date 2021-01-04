package com.application.goalbook.HomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.Database.Profile;
import com.application.goalbook.Database.ProfileViewModel;
import com.application.goalbook.Profile.ProfileActivity;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.ImageSaver;
import com.application.goalbook.Utility.StringFormatter;
import com.application.goalbook.ViewGoals.ViewGoalActivity;
import com.application.goalbook.ViewGoals.ViewGoalsActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener,  Adapter_Goals.ViewGoalInterface {

    private View decorview;
    private TextView tvAllGoals, tvUsername, tvHint;
    private CircleImageView civProfile;
    private ExtendedFloatingActionButton efabAddGoal;

    //For Dashboard
    private int totalCount = 0, pendingCount = 0, completedCount = 0;
    private TextView tvTotalCount, tvCompletedCount, tvPendingCount;

    //For Ads
    private TextView[] tvAdSlider;
    private LinearLayout llAdsSlider;
    private RecyclerView rvAds;
    private Adapter_Ads adapterAds;
    private ArrayList<Integer> adList;

    //For Goals
    private TextView[] tvGoalsSlider;
    private LinearLayout llGoalsSlider;
    private RecyclerView rvGoals;
    private Adapter_Goals adapterGoals;
    private List<Goal> pojoGoalArrayList = new ArrayList<>();

    //Viewmodel
    private GoalViewModel goalViewModel;
    private ProfileViewModel profileViewModel;
    public static final String TAG = "MainActivity";

    //String Formatter
    private StringFormatter stringFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGoalObserver();
        initProfileObserver();
        initViews();
        initListeners();
        getAds();
        setAds();
        setGoals();
        initAdSlider(0);
    }

    private void initGoalObserver() {
        goalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                initGoalSlider(0);
                pojoGoalArrayList = goals;
                adapterGoals.submitList(goals);
                updateDashboard(goals);
            }
        });

    }

    private void initProfileObserver() {
        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ProfileViewModel.class);

        profileViewModel.getProfileLiveData().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                setProfile(profile);
            }
        });
    }

    private void setProfile(Profile profile) {
        if (profile == null)
        {
            return;
        }
        String username = profile.getName();
        String profileImage = profile.getProfileImage();

        tvUsername.setText(username);
        //profile image
        if (profileImage != null)
        {
            Bitmap bitmap = new ImageSaver(MainActivity.this).
                    setFileName(profileImage).
                    setDirectoryName(Constants.DIRECTORY_NAME).
                    load();
            civProfile.setImageBitmap(bitmap);
        }else{
            civProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }
    }


    private void updateDashboard(List<Goal> goals) {
        totalCount = goals.size();
        completedCount = 0;
        pendingCount = 0;
        stringFormatter = new StringFormatter(this);

        for (int i = 0; i < totalCount; i++) {
            if (goals.get(i).getStatus() == Constants.STATUS_COMPLETED) {
                completedCount = completedCount + 1;
            } else {
                pendingCount = pendingCount + 1;
            }
        }

        tvTotalCount.setText(stringFormatter.countFormatter(totalCount));
        tvCompletedCount.setText(stringFormatter.countFormatter(completedCount));
        tvPendingCount.setText(stringFormatter.countFormatter(pendingCount));
    }

    @Override
    public void onViewGoalClick(View view, int position) {
        Intent viewGoalIntent = new Intent(MainActivity.this, ViewGoalActivity.class);
        viewGoalIntent.putExtra("id", pojoGoalArrayList.get(position).getGid());

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(view.findViewById(R.id.component_goal_iv_cover), "cover");
        pairs[1] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_title), "title");
        pairs[2] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_description), "description");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(viewGoalIntent, activityOptions.toBundle());

    }

    @Override
    public void onEditGoalClick(View view, int position) {
        Intent viewGoalIntent = new Intent(MainActivity.this, AddGoalActivity.class);
        viewGoalIntent.putExtra("id", pojoGoalArrayList.get(position).getGid());

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(view.findViewById(R.id.component_goal_iv_cover), "cover");
        pairs[1] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_title), "title");
        pairs[2] = new Pair<View, String>(view.findViewById(R.id.component_goal_tv_goal_description), "description");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(viewGoalIntent, activityOptions.toBundle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_civ_profile:
                jumpToProfile();
                break;
            case R.id.activity_main_efab_add_goals:
                Intent addGoalIntent = new Intent(MainActivity.this, AddGoalActivity.class);
                startActivity(addGoalIntent);
                break;
            case R.id.activity_main_tv_all_goals:
                Intent viewGoalsIntent = new Intent(MainActivity.this, ViewGoalsActivity.class);
                startActivity(viewGoalsIntent);
                break;
        }
    }

    private void jumpToProfile() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(civProfile, "profile");
        pairs[1] = new Pair<View, String>(tvUsername, "username");
        pairs[2] = new Pair<View, String>(tvHint, "hint");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(profileIntent, activityOptions.toBundle());

    }


    private void setGoals() {
        adapterGoals = new Adapter_Goals(MainActivity.this, this);
        rvGoals.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGoals.hasFixedSize();
        rvGoals.setAdapter(adapterGoals);
        //for aligning next item to centre of horizontal rv
        final SnapHelper bpSnapHelper = new PagerSnapHelper();
        bpSnapHelper.attachToRecyclerView(rvGoals);
        rvGoals.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int scrolledPosition = ((LinearLayoutManager) rvGoals.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                    if (scrolledPosition == RecyclerView.NO_POSITION) {
                        return;
                    }
                    initGoalSlider(scrolledPosition);
                }
            }
        });
    }

    private void setAds() {
        adapterAds = new Adapter_Ads(adList);
        rvAds.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvAds.hasFixedSize();
        rvAds.setAdapter(adapterAds);
        //for aligning next item to centre of horizontal rv
        final SnapHelper bpSnapHelper = new PagerSnapHelper();
        bpSnapHelper.attachToRecyclerView(rvAds);

        rvAds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int scrolledPosition = ((LinearLayoutManager) rvAds.getLayoutManager())
                            .findFirstCompletelyVisibleItemPosition();

                    if (scrolledPosition == RecyclerView.NO_POSITION) {
                        return;
                    }
                    initAdSlider(scrolledPosition);
                }
            }
        });
    }


    private void getAds() {
        adList = new ArrayList<>();
        adList.add(R.drawable.ad1);
        adList.add(R.drawable.ad2);
        adList.add(R.drawable.ad1);
        adList.add(R.drawable.ad2);
    }

    private void initAdSlider(int position) {
        try {
            tvAdSlider = new TextView[adList.size()];
            llAdsSlider.removeAllViews();
            for (int i = 0; i < tvAdSlider.length; i++) {
                tvAdSlider[i] = new TextView(this);
                tvAdSlider[i].setText(Html.fromHtml("&#8226;"));
                tvAdSlider[i].setTextSize(44);
                tvAdSlider[i].setTextColor(getResources().getColor(R.color.lightGrey));
                llAdsSlider.addView(tvAdSlider[i]);
            }
            if (tvAdSlider.length > 0) {
                tvAdSlider[position].setTextColor(getResources().getColor(R.color.blackVariant));
            }
        } catch (Exception e) {
            Log.e(TAG, "initAdSlider: " + e.getMessage());
        }
    }

    private void initGoalSlider(int position) {

        tvGoalsSlider = new TextView[pojoGoalArrayList.size()];
        llGoalsSlider.removeAllViews();
        for (int i = 0; i < tvGoalsSlider.length; i++) {
            tvGoalsSlider[i] = new TextView(this);
            tvGoalsSlider[i].setText(Html.fromHtml("&#8226;"));
            tvGoalsSlider[i].setTextSize(44);
            tvGoalsSlider[i].setTextColor(getResources().getColor(R.color.lightGrey));
            llGoalsSlider.addView(tvGoalsSlider[i]);
        }
        if (tvGoalsSlider.length > 0) {
            tvGoalsSlider[position].setTextColor(getResources().getColor(R.color.blackVariant));
        }
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        civProfile = findViewById(R.id.activity_main_civ_profile);
        tvUsername = findViewById(R.id.activity_main_tv_username);
        tvHint = findViewById(R.id.activity_main_tv_hint);
        rvAds = findViewById(R.id.activity_main_rv_ads);
        rvGoals = findViewById(R.id.activity_main_rv_goals);
        efabAddGoal = findViewById(R.id.activity_main_efab_add_goals);
        llAdsSlider = findViewById(R.id.activity_main_ll_ads_slider);
        llGoalsSlider = findViewById(R.id.activity_main_ll_goals_slider);
        tvAllGoals = findViewById(R.id.activity_main_tv_all_goals);

        tvTotalCount = findViewById(R.id.activity_main_tv_total_count);
        tvCompletedCount = findViewById(R.id.activity_main_tv_completed_count);
        tvPendingCount = findViewById(R.id.activity_main_tv_pending_count);


    }

    private void initListeners() {
        civProfile.setOnClickListener(this);
        efabAddGoal.setOnClickListener(this);
        tvAllGoals.setOnClickListener(this);
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