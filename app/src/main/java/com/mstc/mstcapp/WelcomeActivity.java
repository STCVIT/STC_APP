package com.mstc.mstcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mstc.mstcapp.util.Constants;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    SharedPreferences.Editor editor;
    private final Context context = this;
    private final int NoOfSlides = 3;
    private int position = 0;
    private LinearLayout dotsLayout;
    private ImageView imageView;
    private TextView textView;

    private final int[] images = {R.drawable.ic_onboarding_1, R.drawable.ic_onboarding_2, R.drawable.ic_onboarding_3};
    private final int[] texts = {R.string.onboarding1, R.string.onboarding2, R.string.onboarding3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dotsLayout = findViewById(R.id.layoutDots);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        Animation slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);


        addBottomDots(position);
        findViewById(R.id.next).setOnClickListener(v -> {
            position++;
            if (position < NoOfSlides) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, images[position]));
                imageView.startAnimation(slide_in_right);
                textView.setText(getString(texts[position]));
                textView.startAnimation(slide_in_right);
                addBottomDots(position);
            } else {
                editor.putBoolean("isFirstLaunch", false);
                editor.apply();
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[NoOfSlides];
        int colorsActive = ContextCompat.getColor(context, R.color.colorPrimary);
        int colorsInactive = ContextCompat.getColor(context, R.color.gray);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&nbsp;&#8226;&nbsp"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(colorsInactive);
            dots[i].setPadding(15, 15, 15, 15);
            dotsLayout.addView(dots[i]);
        }
        dots[currentPage].setTextColor(colorsActive);
        dots[currentPage].setTextSize(40);
    }

    @Override
    public void onBackPressed() {
        if (position > 0) {
            position--;
            Animation slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            imageView.setImageDrawable(ContextCompat.getDrawable(context, images[position]));
            imageView.startAnimation(slide_in_left);
            textView.setText(getString(texts[position]));
            textView.startAnimation(slide_in_left);
            addBottomDots(position);
        } else {
            super.onBackPressed();
        }
    }
}