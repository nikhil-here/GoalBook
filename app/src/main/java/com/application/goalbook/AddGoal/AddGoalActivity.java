package com.application.goalbook.AddGoal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.application.goalbook.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddGoalActivity extends AppCompatActivity implements ChipGroup.OnCheckedChangeListener, TextWatcher, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View decorview;

    private  ImageView ivCover;
    private LinearLayout llCoverPhoto;
    private AppCompatEditText etTags;
    private TextView tvStartEndDate, tvTimeline;


    //For Tags
    private ChipGroup cgTags, cgReminders;
    private Chip chipYearly, chipMonthly, chipWeekly, chipDaily, chipNone;

    //For Color Picker Sheet
    private RadioGroup rgColors;
    private View vColorPicker;
    private View colorPickerSheetLayout;
    private MaterialDatePicker datePicker;
    private BottomSheetDialog colorPickerSheetDialog;

    //For Color Strips Views
    private int selectedColorId = R.color.lightRed;
    private View vTitle, vTag, vTimeline, vReminders;

    public static final String TAG = "AddGoalActivity";
    public static final int PICK_IMAGE_GALLERY = 101;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_goal, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        if (savedInstanceState == null) {
            initActionBar();
            initColorPickerDialog();
            initViews();
            initListeners();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_add_goal_tv_timeline:
                showDateRangePicker();
                break;
            case R.id.activity_add_goal_view_color_picker:
                colorPickerSheetDialog.show();
                break;
            case R.id.activity_add_goal_ll_add_cover:
                openPhotoPicker();
                break;
        }
    }

    private void openPhotoPicker() {
        if (!checkCameraPermission()) {
            checkAndRequestStorageCameraPermissions();
        }
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && data != null)
        {
            ivCover.setImageURI(data.getData());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_goal_save:
                Toast.makeText(AddGoalActivity.this, "Goal Saved", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void showDateRangePicker() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Timeline For Your Goal");
        datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Long startDate = selection.first;
                Long endDate = selection.second;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
                tvStartEndDate.setText(dateFormat.format(startDate) + " To " + dateFormat.format(endDate));
            }
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    //For Color Picker
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.component_color_picker_dialog_rb_red:
                selectedColorId = R.color.lightRed;
                setViewStripColors(R.color.lightRed);
                break;
            case R.id.component_color_picker_dialog_rb_yellow:
                selectedColorId = R.color.lightYellow;
                setViewStripColors(R.color.lightYellow);
                break;
            case R.id.component_color_picker_dialog_rb_green:
                selectedColorId = R.color.lightGreen;
                setViewStripColors(R.color.lightGreen);
                break;
            case R.id.component_color_picker_dialog_rb_blue:
                selectedColorId = R.color.lightBlue;
                setViewStripColors(R.color.lightBlue);
                break;
            case R.id.component_color_picker_dialog_rb_violet:
                selectedColorId = R.color.lightViolet;
                setViewStripColors(R.color.lightViolet);
                break;
            case R.id.component_color_picker_dialog_rb_grey:
                selectedColorId = R.color.lightGrey;
                setViewStripColors(R.color.lightGrey);
                break;
            case R.id.component_color_picker_dialog_rb_black:
                selectedColorId = R.color.lightBlack;
                setViewStripColors(R.color.lightBlack);
                break;
        }
    }

    //For Reminders
    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {

        /*switch (checkedId)
        {
            case R.id.activity_add_goal_chip_reminder_yearly:
                Toast.makeText(this,"Reminder is Set to : Yearly",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_goal_chip_reminder_monthly:
                Toast.makeText(this,"Reminder is Set to : Monthly",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_goal_chip_reminder_weekly:
                Toast.makeText(this,"Reminder is Set to : Weekly",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_goal_chip_reminder_daily:
                Toast.makeText(this,"Reminder is Set to : Daily",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_goal_chip_reminder_none:
                Toast.makeText(this,"Reminder is Set to : None",Toast.LENGTH_SHORT).show();
                break;
        }*/
    }

    private void setViewStripColors(int colorId) {
        vTitle.setBackgroundColor(ContextCompat.getColor(AddGoalActivity.this, colorId));
        vTag.setBackgroundColor(ContextCompat.getColor(AddGoalActivity.this, colorId));
        vTimeline.setBackgroundColor(ContextCompat.getColor(AddGoalActivity.this, colorId));
        vReminders.setBackgroundColor(ContextCompat.getColor(AddGoalActivity.this, colorId));
        //vColorPicker.setBackgroundColor(ContextCompat.getColor(AddGoalActivity.this,colorId));
        vColorPicker.setBackgroundTintList(getResources().getColorStateList(colorId));
        //changing chips color
        setChipsColor(colorId);
    }

    private void setChipsColor(int colorId) {
        for (int i = 0; i < cgTags.getChildCount(); i++) {
            cgTags.getChildAt(i).setBackgroundColor(colorId);
            Chip chip = (Chip) cgTags.getChildAt(i);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(colorId)));
        }
    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(AddGoalActivity.this.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAndRequestStorageCameraPermissions() {
        int writeStorage = ContextCompat.checkSelfPermission(AddGoalActivity.this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(AddGoalActivity.this.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddGoalActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PICK_IMAGE_GALLERY);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_GALLERY) {
            openPhotoPicker();
        }
    }

    private void initColorPickerDialog() {
        colorPickerSheetDialog = new BottomSheetDialog(AddGoalActivity.this);
        colorPickerSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.component_color_picker_dialog, (ViewGroup) findViewById(R.id.component_color_picker_dialog_ll_container));
        rgColors = colorPickerSheetLayout.findViewById(R.id.component_color_picker_dialog_rg_colors);
        rgColors.setOnCheckedChangeListener(this);
        colorPickerSheetDialog.setContentView(colorPickerSheetLayout);
    }

    private void initListeners() {
        cgReminders.setOnCheckedChangeListener(this);
        etTags.addTextChangedListener(this);
        tvTimeline.setOnClickListener(this);
        llCoverPhoto.setOnClickListener(this);
        vColorPicker.setOnClickListener(this);
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        llCoverPhoto = findViewById(R.id.activity_add_goal_ll_add_cover);
        ivCover = findViewById(R.id.activity_add_goal_iv_goal_cover);
        vColorPicker = findViewById(R.id.activity_add_goal_view_color_picker);
        etTags = findViewById(R.id.activity_add_goal_et_tags);
        cgTags = findViewById(R.id.activity_add_goal_cg_tags);
        cgReminders = findViewById(R.id.activity_add_goal_cg_reminders);
        chipYearly = findViewById(R.id.activity_add_goal_chip_reminder_yearly);
        chipMonthly = findViewById(R.id.activity_add_goal_chip_reminder_monthly);
        chipWeekly = findViewById(R.id.activity_add_goal_chip_reminder_weekly);
        chipDaily = findViewById(R.id.activity_add_goal_chip_reminder_daily);
        chipNone = findViewById(R.id.activity_add_goal_chip_reminder_none);
        tvTimeline = findViewById(R.id.activity_add_goal_tv_timeline);
        tvStartEndDate = findViewById(R.id.activity_add_goal_tv_goal_Start_end_date);
        vTitle = findViewById(R.id.activity_add_goal_view_title);
        vTag = findViewById(R.id.activity_add_goal_view_add_tag);
        vTimeline = findViewById(R.id.activity_add_goal_view_start_end_date);
        vReminders = findViewById(R.id.activity_add_goal_view_reminder_frequencey);
    }


    private void initActionBar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Goal");
    }


    //--------------------Listeners For Add Tags Start  --------------------
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.toString().contains(" ")) {
            addChip(String.valueOf(charSequence));
            etTags.setText(null);
        }
    }

    private void addChip(String text) {
        Chip chip = new Chip(AddGoalActivity.this);
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setCheckable(true);
        chip.setCloseIcon(getResources().getDrawable(R.drawable.ic_close));
        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(selectedColorId)));
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cgTags.removeView(view);
            }
        });
        cgTags.addView(chip);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i(TAG, "afterTextChanged: toString" + editable.toString());
    }


    //--------------------Listeners For Add Tags End  --------------------

}