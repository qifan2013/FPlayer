package com.fan.player.ui.details;

import com.fan.player.data.model.PlayList;
import com.fan.player.data.model.Song;
import com.fan.player.ui.base.BasePresenter;
import com.fan.player.ui.base.BaseView;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/14/16
 * Time: 2:32 AM
 * Desc: PlayListDetailsContract
 */
public interface PlayListDetailsContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable e);

        void onSongDeleted(Song song);
    }

    interface Presenter extends BasePresenter {

        void addSongToPlayList(Song song, PlayList playList);

        void delete(Song song, PlayList playList);
    }
}
