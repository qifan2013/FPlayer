package com.fan.player.event;

import com.fan.player.data.model.PlayList;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/14/16
 * Time: 1:08 AM
 * Desc: PlayListUpdatedEvent
 */
public class PlayListUpdatedEvent {

    PlayList playList;

    public PlayListUpdatedEvent(PlayList playList) {
        this.playList = playList;
    }
}
