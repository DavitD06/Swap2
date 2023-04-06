package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private ImageView mImageView;
    private PopupWindow mPopupWindow;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mImageView = findViewById(R.id.image);

        View mPopupView = LayoutInflater.from(this).inflate(R.layout.popup_window, null);

        mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.MATCH_PARENT,
                200, true);

        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.showAtLocation(mImageView, Gravity.BOTTOM, 0, 0);
                ObjectAnimator animator = ObjectAnimator.ofFloat(mImageView, "translationY", 0f, 100f);
                animator.setDuration(1000);
                animator.start();
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(mImageView, "translationY", mImageView.getTranslationY(), 0f);
                animator.setDuration(1000);
                animator.start();
            }
        });

        mPopupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.dismiss();
                return true;
            }
        });

        ImageView imageView = findViewById(R.id.image);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -20f, 0f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }
}



