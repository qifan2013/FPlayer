package com.fan.player.ui.music;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fan.player.R;
import com.fan.player.RxBus;
import com.fan.player.data.model.PlayList;
import com.fan.player.data.model.Song;
import com.fan.player.data.source.AppRepository;
import com.fan.player.data.source.PreferenceManager;
import com.fan.player.event.PlayListNowEvent;
import com.fan.player.event.PlaySongEvent;
import com.fan.player.player.IPlayback;
import com.fan.player.player.PlayMode;
import com.fan.player.player.PlaybackService;
import com.fan.player.ui.base.BaseActivity;
import com.fan.player.ui.base.BaseFragment;
import com.fan.player.ui.widget.ShadowImageView;
import com.fan.player.utils.AlbumUtils;
import com.fan.player.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/1/16
 * Time: 9:58 PM
 * Desc: MusicPlayerFragment
 */

public class MusicPlayerActivity extends BaseActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
    }}

}
