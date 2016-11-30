package com.fan.player.ui.music;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fan.player.data.model.Song;
import com.fan.player.player.PlayMode;
import com.fan.player.player.PlaybackService;
import com.fan.player.ui.base.BasePresenter;
import com.fan.player.ui.base.BaseView;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/12/16
 * Time: 8:27 AM
 * Desc: MusicPlayerContract
 */
/* package */ interface MusicPlayerContract {

    interface View extends BaseView<Presenter> {

        void handleError(Throwable error);

        void onPlaybackServiceBound(PlaybackService service);

        void onPlaybackServiceUnbound();

        void onSongSetAsFavorite(@NonNull Song song);

        void onSongUpdated(@Nullable Song song);

        void updatePlayMode(PlayMode playMode);

        void updatePlayToggle(boolean play);

        void updateFavoriteToggle(boolean favorite);
    }

    interface Presenter extends BasePresenter {

        void retrieveLastPlayMode();

        void setSongAsFavorite(Song song, boolean favorite);

        void bindPlaybackService();

        void unbindPlaybackService();
    }
}
