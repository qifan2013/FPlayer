package com.fan.player;

import android.content.Context;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/10/16
 * Time: 4:11 PM
 * Desc: Injection
 */
public class Injection {

    public static Context provideContext() {
        return MusicPlayerApplication.getInstance();
    }
}
