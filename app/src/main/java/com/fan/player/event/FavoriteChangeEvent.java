package com.fan.player.event;

import com.fan.player.data.model.Song;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/12/16
 * Time: 9:15 AM
 * Desc: FavoriteChangeEvent
 */
public class FavoriteChangeEvent {

    public Song song;

    public FavoriteChangeEvent(Song song) {
        this.song = song;
    }
}
