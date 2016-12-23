package com.fan.player;

import android.app.Application;
import android.view.Display;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Project: FPlayer
 * User: fan
 * Date: 8/31/16
 * Time: 9:32 PM
 * Desc: MusicPlayerApplication
 */
public class MusicPlayerApplication extends Application {

    private static MusicPlayerApplication sInstance;

    private int height,width;

    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager manager = (WindowManager) this
                .getSystemService(this.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        sInstance = this;

        // Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Monospace-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
