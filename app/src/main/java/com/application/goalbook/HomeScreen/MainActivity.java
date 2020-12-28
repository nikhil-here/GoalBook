package com.application.goalbook.HomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.R;
import com.application.goalbook.ViewGoals.ViewGoalsActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener {

    private View decorview;
    private TextView tvAllGoals;
    private ExtendedFloatingActionButton efabAddGoal;

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
    private ArrayList<Pojo_Goal> pojoGoalArrayList;

    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initViews();
            initListeners();
            getAds();
            setAds();
            getGoals();
            setGoals();
            initAdSlider(0);
            initGoalSlider(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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


    private void setGoals() {
        adapterGoals = new Adapter_Goals(MainActivity.this, pojoGoalArrayList);
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

    private void getGoals() {
        pojoGoalArrayList = new ArrayList<>();
        pojoGoalArrayList.add(new Pojo_Goal("https://eco-business.imgix.net/ebmedia/fileuploads/Feature_RightsofNature_inline2.jpg?fit=crop&h=960&ixlib=django-1.2.0&w=1440", "Kalsubai Trek", "Traveling", "02 Months Remainig", R.color.lightRed));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.thestatesman.com/wp-content/uploads/2020/02/shaw-575x321.jpg", "Playing Cricket", "Sports", "05 Months Remainig", R.color.lightBlue));
        pojoGoalArrayList.add(new Pojo_Goal("https://i.ytimg.com/vi/o8lABLhyI-A/maxresdefault.jpg", "Solo Dance", "Dance", "01 Year Remainig", R.color.lightGreen));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.greengeeks.com/blog/wp-content/uploads/2016/10/website-money.jpg", "Make Money ", "Finance", "3 Years from now", R.color.lightViolet));
        pojoGoalArrayList.add(new Pojo_Goal("https://www.invespcro.com/blog/images/blog-images/main.png", "Plan Year", "Note", "5 Years from now", R.color.lightYellow));
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
        rvAds = findViewById(R.id.activity_main_rv_ads);
        rvGoals = findViewById(R.id.activity_main_rv_goals);
        efabAddGoal = findViewById(R.id.activity_main_efab_add_goals);
        llAdsSlider = findViewById(R.id.activity_main_ll_ads_slider);
        llGoalsSlider = findViewById(R.id.activity_main_ll_goals_slider);
        tvAllGoals = findViewById(R.id.activity_main_tv_all_goals);
    }

    private void initListeners() {
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