package com.application.goalbook.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

public class ProfileActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private View decorview;

    private View vPurpose, vVision, vMission;

    //For Color Strips Views
    private int selectedColorId = R.color.lightRed;

    //For Color Picker Sheet
    private RadioGroup rgColors;
    private View vColorPicker;
    private View colorPickerSheetLayout;
    private MaterialDatePicker datePicker;
    private BottomSheetDialog colorPickerSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (savedInstanceState == null)
        {
            initViews();
            initListeners();
            //initColorPickerDialog();
        }
    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
    }

    private void setViewStripColors(int colorId) {
        vPurpose.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, colorId));
        vVision.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, colorId));
        vMission.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, colorId));
        vColorPicker.setBackgroundTintList(getResources().getColorStateList(colorId));

    }

    private void initColorPickerDialog() {
        colorPickerSheetDialog = new BottomSheetDialog(ProfileActivity.this);
        colorPickerSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.component_color_picker_dialog, (ViewGroup) findViewById(R.id.component_color_picker_dialog_ll_container));
        rgColors = colorPickerSheetLayout.findViewById(R.id.component_color_picker_dialog_rg_colors);
        rgColors.setOnCheckedChangeListener(this);
        colorPickerSheetDialog.setContentView(colorPickerSheetLayout);
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        vPurpose = findViewById(R.id.activity_profile_view_purpose);
        vVision = findViewById(R.id.activity_profile_view_vision);
        vMission = findViewById(R.id.activity_profile_view_mission);
        vColorPicker = findViewById(R.id.activity_profile_view_color_picker);
    }

    private void initListeners() {
        vColorPicker.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_profile_view_color_picker:
                colorPickerSheetDialog.show();
                break;
        }
    }
}