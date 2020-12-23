package com.application.goalbook.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.goalbook.R;
import com.application.goalbook.Utility.HidingKeyboard;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,View.OnSystemUiVisibilityChangeListener{


    private View decorView;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HidingKeyboard.setupUI(findViewById(R.id.activity_login_ll_container), this);

        if (savedInstanceState == null) {
            initViews();
            initListeners();
        }


    }

    private void initListeners() {
        tvRegister.setOnClickListener(this);
    }

    private void initViews() {
        tvRegister = findViewById(R.id.activity_login_tv_register);
        decorView = getWindow().getDecorView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_login_tv_register:
                Intent registerIntent = new Intent(this,RegistrationActivity.class);
                startActivity(registerIntent);
        }
    }


    //removing navigation buttons (on-screen buttons)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == 0) {
            decorView.setSystemUiVisibility(hideSystemBars());
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