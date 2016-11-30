package com.fan.player.event;

import com.fan.player.data.model.PlayList;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/10/16
 * Time: 10:36 PM
 * Desc: PlayListCreatedEvent
 */
public class PlayListCreatedEvent {

    public PlayList playList;

    public PlayListCreatedEvent(PlayList playList) {
        this.playList = playList;
    }
}
