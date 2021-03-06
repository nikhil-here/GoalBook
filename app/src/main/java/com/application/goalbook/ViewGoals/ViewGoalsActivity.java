package com.application.goalbook.ViewGoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.Database.Profile;
import com.application.goalbook.Database.ProfileViewModel;
import com.application.goalbook.HomeScreen.Adapter_Goals;
import com.application.goalbook.HomeScreen.MainActivity;
import com.application.goalbook.Profile.ProfileActivity;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.HidingKeyboard;
import com.application.goalbook.Utility.ImageSaver;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener, Adapter_Goals.ViewGoalInterface, TextWatcher, ChipGroup.OnCheckedChangeListener {

    private View decorview;
    private AdView adView;
    private CircleImageView civProfile;
    private ChipGroup cgStatus;
    private EditText etSearch;
    private ExtendedFloatingActionButton efabAddGoal;
    private RecyclerView rvGoals;
    private Adapter_Goals adapterGoals;
    private List<Goal> pojoGoalArrayList;
    private List<Goal> filteredList = new ArrayList<>();

    private ProfileViewModel profileViewModel;

    //Viewmodel
    private GoalViewModel goalViewModel;
    public static final String TAG = "ViewGoalsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);
        HidingKeyboard.setupUI(findViewById(R.id.activity_view_goals_cl_container), this);

        initGoalObserver();
        initProfileObserver();
        initViews();
        initAd();
        initListeners();
        setGoals();

    }

    private void initGoalObserver() {
        //initializing goalviewmodel
        goalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
        //setting Observer for goals list
        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                pojoGoalArrayList = goals;
                adapterGoals.setCompleteGoalList(pojoGoalArrayList);
                adapterGoals.submitList(goals);
            }
        });
    }

    private void initProfileObserver() {
        //initializing goalviewmodel
        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ProfileViewModel.class);
        //setting Observer for goals list
        profileViewModel.getProfileLiveData().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                setProfile(profile);
            }
        });
    }

    private void setProfile(Profile profile) {

        String profileImage = profile.getProfileImage();

        //profile image
        if (profileImage != null)
        {
            Bitmap bitmap = new ImageSaver(ViewGoalsActivity.this).
                    setFileName(profileImage).
                    setDirectoryName(Constants.DIRECTORY_NAME).
                    load();
            civProfile.setImageBitmap(bitmap);
        }else{
            civProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_view_goals_efab_add_goals:
                Intent addGoalIntent = new Intent(ViewGoalsActivity.this, AddGoalActivity.class);
                startActivity(addGoalIntent);
                break;
            case R.id.activity_view_goals_civ_profile:
                jumpToProfile();
                break;
        }
    }

    private void jumpToProfile() {
        Intent profileIntent = new Intent(ViewGoalsActivity.this, ProfileActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(civProfile, "profile");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(profileIntent, activityOptions.toBundle());

    }

    private void setGoals() {
        adapterGoals = new Adapter_Goals(ViewGoalsActivity.this, this);
        rvGoals.setLayoutManager(new LinearLayoutManager(ViewGoalsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvGoals.hasFixedSize();
        rvGoals.setAdapter(adapterGoals);
    }

    private void initAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        civProfile = findViewById(R.id.activity_view_goals_civ_profile);
        rvGoals = findViewById(R.id.activity_view_goals_rv);
        etSearch = findViewById(R.id.activity_view_goals_et_search);
        efabAddGoal = findViewById(R.id.activity_view_goals_efab_add_goals);
        cgStatus = findViewById(R.id.activity_view_goals_cg_status);
        adView = findViewById(R.id.activity_view_goals_adview);
    }

    private void initListeners() {
        efabAddGoal.setOnClickListener(this);
        etSearch.addTextChangedListener(this);
        cgStatus.setOnCheckedChangeListener(this);
        civProfile.setOnClickListener(this);
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

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {

        List <Goal> completedGoalList = new ArrayList<>();
        List <Goal> pendingGoalList = new ArrayList<>();
        for(int i = 0; i < pojoGoalArrayList.size(); i++)
        {
            if (pojoGoalArrayList.get(i).getStatus() == Constants.STATUS_COMPLETED)
            {
                completedGoalList.add(pojoGoalArrayList.get(i));
            }else {
                pendingGoalList.add(pojoGoalArrayList.get(i));
            }
        }

        switch (group.getCheckedChipId())
        {
            case R.id.activity_view_goals_chip_status_total:
                adapterGoals.submitList(pojoGoalArrayList);
                break;
            case R.id.activity_view_goals_chip_status_completed:
                adapterGoals.submitList(completedGoalList);
                break;
            case R.id.activity_view_goals_chip_status_pending:
                adapterGoals.submitList(pendingGoalList);
                break;
        }
    }

    //--------------------For Filtering List  --------------------
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapterGoals.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    //--------------------For Filtering List  --------------------
}