package com.application.goalbook.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.ColumnInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.application.goalbook.AddGoal.AddGoalActivity;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.Database.Profile;
import com.application.goalbook.Database.ProfileViewModel;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.ImageSaver;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener, Observer<Profile> {

    private View decorview;
    private TextView tvLastUpdatedOn, tvUsername;
    private CircleImageView civProfile;
    private View vPurpose, vVision, vMission;
    private EditText etPurpose, etVision, etMission;

    //For Color Picker Sheet
    private RadioGroup rgColors;
    private View vColorPicker;
    private String[] colorPalette;
    private View colorPickerSheetLayout;
    private BottomSheetDialog colorPickerSheetDialog;

    private Profile profile;
    private int pid;
    private String name;
    private String purpose;
    private String mission;
    private String vision;
    private String profileImage;
    private String colorPreference;
    private Boolean showNotification;
    private Long lastUpdatedOn;

    private ProfileViewModel profileViewModel;
    public static final int PICK_IMAGE_GALLERY = 121;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing goalviewmodel
        profileViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ProfileViewModel.class);
        //setting Observer for goals list
        profileViewModel.getProfileLiveData().observe(this, this);

        initViews();
        initListeners();
        initColorPickerDialog();
        createColorPalette();
    }


    @Override
    public void onChanged(Profile profile) {
        getProfile(profile);
        setProfile();
        Log.i(TAG, "onChanged: profile "+profile.toString());
    }

    private void setProfile() {
        tvUsername.setText(name);
        etPurpose.setText(purpose);
        etMission.setText(mission);
        etVision.setText(vision);

        //last updated time
        if (lastUpdatedOn != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            tvLastUpdatedOn.setVisibility(View.VISIBLE);
            tvLastUpdatedOn.setText("Last Updated On "+dateFormat.format(lastUpdatedOn));
        } else {
            tvLastUpdatedOn.setVisibility(View.GONE);
        }

        //profile image
        if (profileImage != null)
        {
            Bitmap bitmap = new ImageSaver(ProfileActivity.this).
                    setFileName(profileImage).
                    setDirectoryName(Constants.DIRECTORY_NAME).
                    load();
            civProfile.setImageBitmap(bitmap);
        }else{
            civProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }

        //view strips
        setViewStripColors(colorPreference);
    }


    private void getProfile(Profile profile) {
        this.profile = profile;
        pid = profile.getPid();
        name = profile.getName();
        purpose = profile.getPurpose();
        mission = profile.getMission();
        vision = profile.getVision();
        profileImage = profile.getProfileImage();
        colorPreference = profile.getColorPreference();
        showNotification = profile.getShowNotification();
        lastUpdatedOn = profile.getLastUpdatedOn();
    }

    private  void saveProfile()
    {
        Profile updatedProfile = new Profile(name,purpose,mission,vision,profileImage,colorPreference,showNotification,System.currentTimeMillis());
        updatedProfile.setPid(pid);
        profileViewModel.update(updatedProfile);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        //get id of checked button
        int selectedButtonId = radioGroup.getCheckedRadioButtonId();
        //id indicate position of color in array, getting selected color hash value
        colorPreference = colorPalette[i];
        setViewStripColors(colorPreference);
        saveProfile();
    }

    private void setViewStripColors(String selectedColor) {
        vPurpose.setBackgroundColor(Color.parseColor(selectedColor));
        vVision.setBackgroundColor(Color.parseColor(selectedColor));
        vMission.setBackgroundColor(Color.parseColor(selectedColor));
        vColorPicker.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(selectedColor)));
    }

    private void initColorPickerDialog() {
        colorPickerSheetDialog = new BottomSheetDialog(ProfileActivity.this);
        colorPickerSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.component_color_picker_dialog, (ViewGroup) findViewById(R.id.component_color_picker_dialog_ll_container));
        rgColors = colorPickerSheetLayout.findViewById(R.id.component_color_picker_dialog_rg_colors);
        rgColors.setOnCheckedChangeListener(this);
        colorPickerSheetDialog.setContentView(colorPickerSheetLayout);
    }

    private void createColorPalette() {
        colorPalette = getResources().getStringArray(R.array.color_palette);

        for (int i = 0; i < colorPalette.length; i++) {
            //create radio button by inflating radio button layout
            LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
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

    private void initViews() {
        decorview = getWindow().getDecorView();
        tvUsername = findViewById(R.id.activity_profile_tv_username);
        civProfile = findViewById(R.id.activity_profile_civ_profile);
        tvLastUpdatedOn = findViewById(R.id.activity_profile_tv_last_updated_on);
        vPurpose = findViewById(R.id.activity_profile_view_purpose);
        vVision = findViewById(R.id.activity_profile_view_vision);
        vMission = findViewById(R.id.activity_profile_view_mission);
        vColorPicker = findViewById(R.id.activity_profile_view_color_picker);
        etPurpose = findViewById(R.id.activity_profile_et_purpose);
        etVision = findViewById(R.id.activity_profile_et_vision);
        etMission = findViewById(R.id.activity_profile_et_mission);
    }

    private void initListeners() {
        vColorPicker.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_profile_view_color_picker:
                colorPickerSheetDialog.show();
                break;
            case R.id.activity_profile_civ_profile:
                openPhotoPicker();
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && data != null) {
            String filename = UUID.randomUUID().toString().replace("-", "");
            String fileExtension = getfileExtension(data.getData());
            profileImage = filename + "." + fileExtension;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                civProfile.setImageBitmap(bitmap);
                //saving image
                new ImageSaver(ProfileActivity.this).
                        setFileName(profileImage).
                        setDirectoryName(Constants.DIRECTORY_NAME).
                        save(bitmap);

                //saving profile
                saveProfile();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_GALLERY) {
            openPhotoPicker();
        }
    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(ProfileActivity.this.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAndRequestStorageCameraPermissions() {
        int writeStorage = ContextCompat.checkSelfPermission(ProfileActivity.this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(ProfileActivity.this.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ProfileActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PICK_IMAGE_GALLERY);
            return false;
        }
        return true;
    }



}