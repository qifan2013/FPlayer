package com.fan.player.event;

import com.fan.player.data.model.Song;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/5/16
 * Time: 6:32 PM
 * Desc: PlaySongEvent
 */
public class PlaySongEvent {

    public Song song;

    public PlaySongEvent(Song song) {
        this.song = song;
    }
}
