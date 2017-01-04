package com.fan.player.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/8/16
 * Time: 4:23 PM
 * Desc: ShadowImageView
 * Stole from {@link android.support.v4.widget.SwipeRefreshLayout}'s implementation to display beautiful shadow
 * for circle ImageView.
 */
public class RotateImageView extends ImageView {

    private Boolean isRotating = false;

    // Animation
    private ObjectAnimator mRotateAnimator;
    public ObjectAnimator mBackRotateAnimator;
    private long mLastAnimationValue;

    public RotateImageView(Context context) {
        this(context, null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        mRotateAnimator.setDuration(25000);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);

    }

    // Animation

    public void startRotateAnimation() {
        if(isRotating){
            resumeRotateAnimation();
        }else{
            mRotateAnimator.cancel();
            mRotateAnimator.start();
        }

        isRotating = true;
    }

    public void cancelRotateAnimation() {


        mBackRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", mLastAnimationValue, 360f);
        mBackRotateAnimator.setDuration(300);
        mBackRotateAnimator.setInterpolator(new LinearInterpolator());
        mBackRotateAnimator.start();
        mBackRotateAnimator.cancel();
        mLastAnimationValue = 0;
        mRotateAnimator.cancel();
        isRotating = false;

    }

    public void pauseRotateAnimation() {
        mLastAnimationValue = mRotateAnimator.getCurrentPlayTime();
        mRotateAnimator.cancel();
        isRotating = false;
    }

    public void resumeRotateAnimation() {
        mRotateAnimator.start();
        mRotateAnimator.setCurrentPlayTime(mLastAnimationValue);
        isRotating = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRotateAnimator != null) {
            mRotateAnimator.cancel();
            mRotateAnimator = null;
        }
    }

    /**
     * Draw oval shadow below ImageView under lollipop.
     */
    public boolean isRotating(){
        return this.isRotating;
    }
}
