package com.fan.player.ui.music;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MusicThreadFactory implements ThreadFactory {

    private String threadName;

    public MusicThreadFactory(String threadName){
        this.threadName = threadName;
    }
    @Override
    public Thread newThread(Runnable runnable) {

        return new Thread(runnable);
    }
}
