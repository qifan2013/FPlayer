package com.fan.player.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fan.player.utils.AlbumUtils;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/8/16
 * Time: 4:23 PM
 * Desc: ShadowImageView
 * Stole from {@link android.support.v4.widget.SwipeRefreshLayout}'s implementation to display beautiful shadow
 * for circle ImageView.
 */
public class BackgroundImageView extends ImageView {

    private static final int DEFAULT_BACKGROUND_COLOR = 0xFF3C5F78;


    // Animation
    private static ObjectAnimator mAlphaAnimator;

    public static Context mContext;

    public BackgroundImageView(Context context) {
        this(context, null);
    }

    public BackgroundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackgroundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public  static BackgroundImageView newInstance(Context context,Bitmap bitmap,int x,int y) {

        BackgroundImageView instance = new BackgroundImageView(context);
        mContext = context;
        instance.setBackgroundBitmap(bitmap,x,y);
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public BackgroundImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private static void init() {


        mAlphaAnimator = ObjectAnimator.ofFloat(mContext, "alpha", 0.5f, 1f);
        mAlphaAnimator.setDuration(10000);
        mAlphaAnimator.setInterpolator(new LinearInterpolator());
        mAlphaAnimator.setRepeatCount(0);
        mAlphaAnimator.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setBackgroundBitmap(Bitmap bitmap,int x,int y){
        setBackground(new BitmapDrawable(AlbumUtils.blur(AlbumUtils.getRectBitmap(bitmap,x,y),25f)));
        mAlphaAnimator.start();
    }
    // Animation

}
