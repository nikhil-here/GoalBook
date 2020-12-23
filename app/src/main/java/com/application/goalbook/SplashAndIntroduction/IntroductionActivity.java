package com.application.goalbook.SplashAndIntroduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.goalbook.LoginAndRegistration.LoginActivity;
import com.application.goalbook.R;

public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,View.OnSystemUiVisibilityChangeListener{

    private ViewPager vpSlider;
    private LinearLayout llDotLayout;
    private Adapter_Intro adapterIntro;
    private TextView[] mDots;
    private Button btnSkip;
    private View decorView;
    private int mCurrentPage;
    //for view pager
    private int[] layouts = {R.layout.layout_intro_one,R.layout.layout_intro_two,R.layout.layout_intro_three};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        if (savedInstanceState == null)
        {
            initViews();
            initAdapter();
            initListeners();
            addDotsIndicator(0);
        }
    }

    //for skip and next button

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.activity_slider_btn_skip:
                Intent loginIntent = new Intent(IntroductionActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    private void initViews() {
        vpSlider = findViewById(R.id.activity_intro_slider_vp);
        llDotLayout = findViewById(R.id.activity_intro_slider_ll);
        btnSkip = findViewById(R.id.activity_slider_btn_skip);
        decorView = getWindow().getDecorView();
    }

    private void initAdapter() {
        adapterIntro = new Adapter_Intro(layouts,this);
        vpSlider.setAdapter(adapterIntro);
    }

    private void initListeners() {
        btnSkip.setOnClickListener(this);
        vpSlider.setOnPageChangeListener(this);
    }

    //for handling dot and skip/finish button text
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addDotsIndicator(position);
        mCurrentPage = position;
        if (position == 2)
        {
            btnSkip.setText("FINISH");
        }else{
            btnSkip.setText("SKIP");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        llDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(54 );
            mDots[i].setTextColor(getResources().getColor(R.color.blackVariant));
            llDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.lightGrey));
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
