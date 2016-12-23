package com.fan.player.ui.music;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fan.player.MusicPlayerApplication;
import com.fan.player.utils.AlbumUtils;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MusicBgAnimation extends AlphaAnimation implements Animation.AnimationListener {

    private RelativeLayout containLayout;

    private Bitmap bitmap;

    public MusicBgAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicBgAnimation(float fromAlpha, float toAlpha) {
        super(fromAlpha, toAlpha);
    }

    public static MusicBgAnimation newInstance(RelativeLayout linearLayout, Bitmap bitmap){
        MusicBgAnimation instance = new MusicBgAnimation(0,1f);
        instance.containLayout = linearLayout;
        instance.bitmap = bitmap;
        instance.setRepeatCount(0);
        instance.setFillAfter(true);
        instance.setDuration(3000);
        instance.setAnimationListener(instance);
        return instance;
    }

    @Override
    public boolean willChangeBounds() {
        return super.willChangeBounds();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d("qifan","onAnimationStart"+MusicPlayerApplication.getInstance().getWidth());
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d("qifan","onAnimationEnd"+MusicPlayerApplication.getInstance().getWidth());
//        containLayout.setBackground(new BitmapDrawable(bitmap));
        containLayout.setBackground(new BitmapDrawable(AlbumUtils.blur(AlbumUtils.getRectBitmap(bitmap,MusicPlayerApplication.getInstance().getWidth(),MusicPlayerApplication.getInstance().getHeight()),25f)));
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
