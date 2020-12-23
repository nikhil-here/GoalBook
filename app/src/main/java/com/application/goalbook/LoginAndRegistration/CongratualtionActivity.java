package com.application.goalbook.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.goalbook.R;

public class CongratualtionActivity extends AppCompatActivity implements View.OnClickListener,View.OnSystemUiVisibilityChangeListener{

    private View decorview;
    private Button btnLetsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratualtion);
        if (savedInstanceState == null)
        {
            initViews();
            initListeners();
        }
    }

    private void initListeners() {
        btnLetsGo.setOnClickListener(this);
    }

    private void initViews() {
        btnLetsGo = findViewById(R.id.activity_congratulation_btn_letsgo);
        decorview = getWindow().getDecorView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_congratulation_btn_letsgo:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
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