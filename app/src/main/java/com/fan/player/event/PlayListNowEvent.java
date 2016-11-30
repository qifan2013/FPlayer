package com.fan.player.event;

import com.fan.player.data.model.PlayList;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/11/16
 * Time: 9:22 PM
 * Desc: PlayListNowEvent
 */
public class PlayListNowEvent {

    public PlayList playList;
    public int playIndex;

    public PlayListNowEvent(PlayList playList, int playIndex) {
        this.playList = playList;
        this.playIndex = playIndex;
    }
}
