package com.application.goalbook.AddGoal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.HidingKeyboard;
import com.application.goalbook.Utility.ImageSaver;
import com.application.goalbook.ViewGoals.ViewGoalActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;


public class AddGoalActivity extends AppCompatActivity implements ChipGroup.OnCheckedChangeListener, TextWatcher, View.OnClickListener, RadioGroup.OnCheckedChangeListener, Observer<Goal>, TextView.OnEditorActionListener {


    private AdView adView;
    private ImageView ivCover;
    private LinearLayout llCoverPhoto;
    private AppCompatEditText etTags;
    private EditText etTitle, etDescription;
    private TextView tvStartEndDate, tvTimeline;


    //For Tags
    private ChipGroup cgTags, cgReminders;
    private Chip chipYearly, chipMonthly, chipWeekly, chipDaily, chipNone;

    //For Color Picker Sheet
    private RadioGroup rgColors;
    private View vColorPicker;
    private String[] colorPalette;
    private View colorPickerSheetLayout;
    private MaterialDatePicker datePicker;
    private BottomSheetDialog colorPickerSheetDialog;

    //For Color Strips Views
    private View vTitle, vTag, vTimeline, vReminders;
    public static final String TAG = "AddGoalActivity";
    public static final int PICK_IMAGE_GALLERY = 101;

    //Values
    private Goal goal;
    private ArrayList<String> tags = new ArrayList<>();
    private Long startDate, endDate;
    private int status = Constants.STATUS_PENDING, reminderFrequency = Constants.REMINDER_NONE, gid;
    private String coverImage, title, description, color = "#F0C1C5";

    //Viewmode
    private GoalViewModel goalViewModel;


    private Boolean isEditGoal = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        HidingKeyboard.setupUI(findViewById(R.id.activity_add_goal_cl_container), this);

        if (savedInstanceState == null) {
            //initializing goalviewmodel
            goalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(GoalViewModel.class);
            initActionBar();
            initColorPickerDialog();
            createColorPalette();
            initViews();
            initAds();
            initListeners();
            getBundle();
        }
    }

    private void getBundle() {
        gid = getIntent().getIntExtra("id", -1);
        initObserver();
    }

    private void initObserver() {
        if (gid != -1) {
            isEditGoal = true;
            setTitle("Edit Goal");
            goalViewModel.getGoalById(gid).observe(this, this);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_goal_save:
                getValues();
                if (validate())
                {
                    if (isEditGoal)
                    {
                        updateGoal();
                    }else{
                        saveGoal();
                    }
                }
                return true;
            case R.drawable.ic_close:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(Goal goal) {
        setGoal(goal);
    }

    private void saveGoal() {
        Goal goal = new Goal(coverImage, title, description, tags, color, status, startDate, endDate, reminderFrequency);
        goalViewModel.insert(goal);
        Toasty.success(AddGoalActivity.this, "New Goal Added").show();
        finish();
    }

    private void updateGoal() {
        Goal updatedGoal = new Goal(coverImage, title, description, tags, color, status, startDate, endDate, reminderFrequency);
        updatedGoal.setGid(gid);
        goalViewModel.update(updatedGoal);
        Toasty.success(AddGoalActivity.this, "Goal Modified").show();
        Log.i(TAG, "updateGoal: updated Goal "+updatedGoal.toString());
        finish();
    }

    private void setGoal(Goal goal) {
        this.goal = goal;
        tags = goal.getTags();
        color = goal.getColor();
        title = goal.getTitle();
        status = goal.getStatus();
        endDate = goal.getEndDate();
        startDate = goal.getStartDate();
        coverImage = goal.getCoverImage();
        description = goal.getDescription();
        reminderFrequency = goal.getReminderFrequency();

        etTitle.setText(title);
        etDescription.setText(description);
        //loading cover image
        Bitmap bitmap = new ImageSaver(AddGoalActivity.this).
                setFileName(coverImage).
                setDirectoryName(Constants.DIRECTORY_NAME).
                load();
        ivCover.setImageBitmap(bitmap);
        //setting view strip colors
        vTitle.setBackgroundColor(Color.parseColor(color));
        vTag.setBackgroundColor(Color.parseColor(color));
        vTimeline.setBackgroundColor(Color.parseColor(color));
        vReminders.setBackgroundColor(Color.parseColor(color));
        vColorPicker.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));

        //setting start and end date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
        tvStartEndDate.setText(dateFormat.format(startDate) + " To " + dateFormat.format(endDate));
        //adding tags
        cgTags.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            addChip(tags.get(i));
        }

        switch (reminderFrequency) {
            case Constants.REMINDER_NONE:
                chipNone.setChecked(true);
                break;
            case Constants.REMINDER_DAILY:
                chipDaily.setChecked(true);
                break;
            case Constants.REMINDER_WEEKLY:
                chipWeekly.setChecked(true);
                break;
            case Constants.REMINDER_MONTHLY:
                chipMonthly.setChecked(true);
                break;
            case Constants.REMINDER_YEARLY:
                chipYearly.setChecked(true);
                break;
        }
    }


    private void getValues() {
        title = etTitle.getText().toString();
        description = etDescription.getText().toString();
        //get tags
        tags.clear();
        for (int i = 0; i < cgTags.getChildCount(); i++) {
            Chip chip = (Chip) cgTags.getChildAt(i);
            String tag = (String) chip.getChipText();
            tags.add(tag);
        }
        if (coverImage == null) {
            Uri defaultImageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.background)
                    + '/' + getResources().getResourceTypeName(R.drawable.background) + '/' + getResources().getResourceEntryName(R.drawable.background));
            coverImage = defaultImageUri.toString();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && data != null) {

            String filename = UUID.randomUUID().toString().replace("-", "");
            String fileExtension = getfileExtension(data.getData());
            coverImage = filename + "." + fileExtension;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ivCover.setImageBitmap(bitmap);
                //saving image
                new ImageSaver(AddGoalActivity.this).
                        setFileName(coverImage).
                        setDirectoryName(Constants.DIRECTORY_NAME).
                        save(bitmap);

            } catch (Exception e) {
                Log.i(TAG, "onActivityResult: PhotoPicker Exception " + e.getMessage());
            }

        }
    }

    private String getfileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    private void showDateRangePicker() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Timeline For Your Goal");
        datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDate = selection.first;
                endDate = selection.second;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
                tvStartEndDate.setText(dateFormat.format(startDate) + " To " + dateFormat.format(endDate));
            }
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    //For Color Picker
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        //get id of checked button
        int selectedButtonId = radioGroup.getCheckedRadioButtonId();
        //id indicate position of color in array, getting selected color hash value
        color = colorPalette[i];
        setViewStripColors(color);
    }

    //For Reminders
    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.activity_add_goal_chip_reminder_yearly:
                reminderFrequency = Constants.REMINDER_YEARLY;
                break;
            case R.id.activity_add_goal_chip_reminder_monthly:
                reminderFrequency = Constants.REMINDER_MONTHLY;
                break;
            case R.id.activity_add_goal_chip_reminder_weekly:
                reminderFrequency = Constants.REMINDER_WEEKLY;
                break;
            case R.id.activity_add_goal_chip_reminder_daily:
                reminderFrequency = Constants.REMINDER_DAILY;
                break;
            case R.id.activity_add_goal_chip_reminder_none:
                reminderFrequency = Constants.REMINDER_NONE;
                break;
        }
    }

    private void setViewStripColors(String selectedColor) {
        vTitle.setBackgroundColor(Color.parseColor(selectedColor));
        vTag.setBackgroundColor(Color.parseColor(selectedColor));
        vTimeline.setBackgroundColor(Color.parseColor(selectedColor));
        vReminders.setBackgroundColor(Color.parseColor(selectedColor));
        vColorPicker.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(selectedColor)));
        //changing chips color
        setChipsColor(selectedColor);
    }


    private void setChipsColor(String selectedColor) {
        for (int i = 0; i < cgTags.getChildCount(); i++) {
            cgTags.getChildAt(i).setBackgroundColor(Color.parseColor(selectedColor));
            Chip chip = (Chip) cgTags.getChildAt(i);
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(selectedColor)));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_GALLERY) {
            openPhotoPicker();
        }
    }

    private void openPhotoPicker() {
        if (!checkCameraPermission()) {
            checkAndRequestStorageCameraPermissions();
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
        }
    }

    private void createColorPalette() {
        colorPalette = getResources().getStringArray(R.array.color_palette);

        for (int i = 0; i < colorPalette.length; i++) {
            //create radio button by inflating radio button layout
            LayoutInflater inflater = LayoutInflater.from(AddGoalActivity.this);
            View rbView = inflater.inflate(R.layout.component_custom_radio_button, null);
            RadioButton rb = (RadioButton) rbView.getRootView();

            //set unique id
            rb.setId(i);

            //set some margin to radio buttons
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 4, 4, 4);
            rb.setLayoutParams(params);

            //set color from pallete
            rb.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorPalette[i])));

            //add view
            rgColors.addView(rb);
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
        etTags.setOnEditorActionListener(this);
    }

    //--------------------Listeners For Add Tags Start  --------------------
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String tagText = etTags.getText().toString();
            if (!tagText.isEmpty())
            {
                addChip(tagText);
                etTags.setText(null);
            }
            handled = true;
        }
        return handled;
    }

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
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
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

    private void initAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_goal, menu);
        return true;
    }

    private void initViews() {

        llCoverPhoto = findViewById(R.id.activity_add_goal_ll_add_cover);
        ivCover = findViewById(R.id.activity_add_goal_iv_goal_cover);
        etTitle = findViewById(R.id.activity_add_goal_et_goal_title);
        etDescription = findViewById(R.id.activity_add_goal_et_goal_description);
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
        adView = findViewById(R.id.activity_add_goal_adview);
    }

    private void initActionBar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Goal");
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

    private boolean validate() {
        //checking for title
        if (title.trim().isEmpty() | description.trim().isEmpty()) {
            Toasty.warning(AddGoalActivity.this, "Enter Tile & Description").show();
            return false;
        }
        if (startDate == null | endDate == null) {
            Toasty.warning(AddGoalActivity.this, "Please select Start & End Date").show();
            return false;
        }
        if (tags.size() < 1) {
            Toasty.warning(AddGoalActivity.this, "Add atleast One Tag for your goal").show();
            return false;
        }

        return true;
    }


}