package com.fan.player.ui.local.all;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import com.fan.player.data.model.Song;
import com.fan.player.ui.base.BasePresenter;
import com.fan.player.ui.base.BaseView;

import java.util.List;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/13/16
 * Time: 8:32 PM
 * Desc: LocalMusicContract
 */
/* package */ interface LocalMusicContract {

    interface View extends BaseView<Presenter> {

        LoaderManager getLoaderManager();

        Context getContext();

        void showProgress();

        void hideProgress();

        void emptyView(boolean visible);

        void handleError(Throwable error);

        void onLocalMusicLoaded(List<Song> songs);
    }

    interface Presenter extends BasePresenter {

        void loadLocalMusic();
    }
}
