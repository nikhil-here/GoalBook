package com.application.goalbook.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.goalbook.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,View.OnSystemUiVisibilityChangeListener {
    private View decorview;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (savedInstanceState == null)
        {
            initViews();
            initListeners();
        }
    }

    private void initListeners() {
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initViews() {
        decorview = getWindow().getDecorView();
        btnRegister = findViewById(R.id.activity_registration_btn_register);
        tvLogin = findViewById(R.id.activity_registration_tv_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_registration_btn_register:
                Intent otpIntent = new Intent(this,OTPActivity.class);
                startActivity(otpIntent);
                break;

            case R.id.activity_registration_tv_login:
                super.onBackPressed();
                break;

        }
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