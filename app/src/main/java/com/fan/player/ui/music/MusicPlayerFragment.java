package com.fan.player.ui.music;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.fan.player.ui.base.BaseFragment;
import com.fan.player.ui.widget.BackgroundImageView;
import com.fan.player.utils.AlbumUtils;
import com.fan.player.utils.TimeUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class MusicPlayerFragment extends BaseFragment implements MusicPlayerContract.View, IPlayback.Callback {

    // private static final String TAG = "MusicPlayerFragment";

    // Update seek bar every second
    private static final long UPDATE_PROGRESS_INTERVAL = 1000;
    @BindView(R.id.layout_contain_bg)
    FrameLayout containLayoutbg;
    @BindView(R.id.layout_contain)
    RelativeLayout containLayout;
    @BindView(R.id.view_pager_album)
    ViewPager viewPagerAlbum;
    @BindView(R.id.text_view_name)
    TextView textViewName;
    @BindView(R.id.text_view_artist)
    TextView textViewArtist;
    @BindView(R.id.text_view_progress)
    TextView textViewProgress;
    @BindView(R.id.text_view_duration)
    TextView textViewDuration;
    @BindView(R.id.seek_bar)
    SeekBar seekBarProgress;
    @BindView(R.id.button_play_mode_toggle)
    ImageView buttonPlayModeToggle;
    @BindView(R.id.button_play_toggle)
    ImageView buttonPlayToggle;
    @BindView(R.id.button_favorite_toggle)
    ImageView buttonFavoriteToggle;

    private IPlayback mPlayer;
    private AlbumFragmentAdapter mAlbumFragmentAdapter;
    private ArrayList<MusicAlbumFragment> albumFragments = new ArrayList<>();

    private ArrayList<View> albumPagerList = new ArrayList<>();

    private FrameLayout.LayoutParams tparams;
    private FrameLayout.LayoutParams params;
    private MusicPlayerContract.Presenter mPresenter;
    private AlphaAnimation alphaAnimation;
    private int currentIndex = 0;

    ExecutorService changeSongPool = Executors.newCachedThreadPool(new MusicThreadFactory("changeSong"));
    ExecutorService changeBgPool = Executors.newCachedThreadPool(new MusicThreadFactory("changeBg"));

    private int x ,y;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
            switch (msg.what){
                case 1:
                    playSong(mPlayer.getPlayList(),currentIndex );
                    break;
                case 2:
                    containLayoutbg.addView(BackgroundImageView.newInstance(getActivity(),(Bitmap) msg.obj,x,y),tparams);
                    containLayoutbg.startAnimation(MusicBgAnimation.newInstance(containLayout,(Bitmap) msg.obj));
//                    containLayout.setBackground(new BitmapDrawable(AlbumUtils.blur(AlbumUtils.getRectBitmap((Bitmap) msg.obj,x,y),25f)));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Runnable mProgressCallback = new Runnable() {
        @Override
        public void run() {
            if (isDetached()) return;

            if (mPlayer.isPlaying()) {

                int progress = (int) (seekBarProgress.getMax()
                        * ((float) mPlayer.getProgress() / (float) getCurrentSongDuration()));
                updateProgressTextWithDuration(mPlayer.getProgress());
                if (progress >= 0 && progress <= seekBarProgress.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBarProgress.setProgress(progress, true);
                    } else {
                        seekBarProgress.setProgress(progress);
                    }
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mProgressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (mPlayer.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback);
                    mHandler.post(mProgressCallback);

                }
            }
        });


        new MusicPlayerPresenter(getActivity(), AppRepository.getInstance(), this).subscribe();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mProgressCallback);
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    // Click Events

    @OnClick(R.id.button_play_toggle)
    public void onPlayToggleAction(View view) {
        if (mPlayer == null) return;

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            AlbumAnimation("pause",currentIndex);
        } else {
            mPlayer.play();
            AlbumAnimation("resume",currentIndex);
        }
    }

    @OnClick(R.id.button_play_mode_toggle)
    public void onPlayModeToggleAction(View view) {
        if (mPlayer == null) return;

        PlayMode current = PreferenceManager.lastPlayMode(getActivity());
        PlayMode newMode = PlayMode.switchNextMode(current);
        PreferenceManager.setPlayMode(getActivity(), newMode);
        mPlayer.setPlayMode(newMode);
        updatePlayMode(newMode);
    }

    @OnClick(R.id.button_play_last)
    public void onPlayLastAction(View view) {
        if (mPlayer == null) return;

        mPlayer.playLast();
    }

    @OnClick(R.id.button_play_next)
    public void onPlayNextAction(View view) {
        if (mPlayer == null) return;

        mPlayer.playNext();
    }

    @OnClick(R.id.button_favorite_toggle)
    public void onFavoriteToggleAction(View view) {
        if (mPlayer == null) return;

        Song currentSong = mPlayer.getPlayingSong();
        if (currentSong != null) {
            view.setEnabled(false);
            mPresenter.setSongAsFavorite(currentSong, !currentSong.isFavorite());
        }
    }

    // RXBus Events

    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlaySongEvent) {
                            onPlaySongEvent((PlaySongEvent) o);
                        } else if (o instanceof PlayListNowEvent) {
                            onPlayListNowEvent((PlayListNowEvent) o);
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }

    private void onPlaySongEvent(PlaySongEvent event) {
        Log.d("qifan","PlaySongEvent");
        Song song = event.song;
        playSong(song);
    }

    private void onPlayListNowEvent(PlayListNowEvent event) {
        Log.d("qifan","onPlayListNowEvent");
        PlayList playList = event.playList;
        int playIndex = event.playIndex;
        currentIndex = playIndex;
        playSong(playList, playIndex);
    }

    // Music Controls

    private void playSong(Song song) {
        PlayList playList = new PlayList(song);
        playSong(playList, 0);
        Log.d("qifan","onPlayListNowEvent");

    }

    private void playSong(PlayList playList, int playIndex) {
        if (playList == null) return;
        Log.d("qifan","onPlayListNowEvent");
        playList.setPlayMode(PreferenceManager.lastPlayMode(getActivity()));
        mPlayer.play(playList, playIndex);

        Song song = playList.getCurrentSong();
        onSongUpdated(song);

    }

    private void updateProgressTextWithProgress(int progress) {
        int targetDuration = getDuration(progress);
        textViewProgress.setText(TimeUtils.formatDuration(targetDuration));
    }

    private void updateProgressTextWithDuration(int duration) {
        textViewProgress.setText(TimeUtils.formatDuration(duration));
    }

    private void seekTo(int duration) {
        mPlayer.seekTo(duration);
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBarProgress.getMax()));
    }

    private int getCurrentSongDuration() {
        Song currentSong = mPlayer.getPlayingSong();
        int duration = 0;
        if (currentSong != null) {
            duration = currentSong.getDuration();

        }
        return duration;
    }

    // Player Callbacks

    @Override
    public void onSwitchLast(Song last) {
        onSongUpdated(last);
    }

    @Override
    public void onSwitchNext(Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onComplete(Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        updatePlayToggle(isPlaying);
        if (isPlaying) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        } else {
            mHandler.removeCallbacks(mProgressCallback);
        }
    }

    // MVP View

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaybackServiceBound(PlaybackService service) {
        mPlayer = service;
        mPlayer.registerCallback(this);


        initAlbum();
    }

    @Override
    public void onPlaybackServiceUnbound() {
        mPlayer.unregisterCallback(this);
        mPlayer = null;
    }

    @Override
    public void onSongSetAsFavorite(@NonNull Song song) {
        buttonFavoriteToggle.setEnabled(true);
        updateFavoriteToggle(song.isFavorite());
    }

    public void onSongUpdated(@Nullable Song song) {
        if (song == null) {
            buttonPlayToggle.setImageResource(R.drawable.ic_play);
            seekBarProgress.setProgress(0);
            updateProgressTextWithProgress(0);
            seekTo(0);
            mHandler.removeCallbacks(mProgressCallback);
            return;
        }
        // Step 1: Song name and artist
        textViewName.setText(song.getDisplayName());
        textViewArtist.setText(song.getArtist());
        // Step 2: favorite
        buttonFavoriteToggle.setImageResource(song.isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
        // Step 3: Duration
        textViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));

        mHandler.removeCallbacks(mProgressCallback);
        if (mPlayer.isPlaying()) {
            mHandler.post(mProgressCallback);
            viewPagerAlbum.setCurrentItem(mPlayer.getPlayList().getSongs().indexOf(song));
            buttonPlayToggle.setImageResource(R.drawable.ic_pause);
            AlbumAnimation("start",currentIndex);
            if(currentIndex != 0 && mAlbumFragmentAdapter.getFragment(currentIndex-1) != null){
                AlbumAnimation("cancel",currentIndex-1);
            }
            if(currentIndex != mPlayer.getPlayList().getSongs().size() && mAlbumFragmentAdapter.getFragment(currentIndex+1) != null){
                AlbumAnimation("cancel",currentIndex+1);
            }
        }
    }

    @Override
    public void updatePlayMode(PlayMode playMode) {
        if (playMode == null) {
            playMode = PlayMode.getDefault();
        }
        switch (playMode) {
            case LIST:
                buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case LOOP:
                buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case SHUFFLE:
                buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            case SINGLE:
                buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_single);
                break;
        }
    }

    @Override
    public void updatePlayToggle(boolean play) {
        buttonPlayToggle.setImageResource(play ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void updateFavoriteToggle(boolean favorite) {
        buttonFavoriteToggle.setImageResource(favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    }

    @Override
    public void setPresenter(MusicPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void initAlbum(){
        x = containLayout.getWidth();
        y = containLayout.getHeight();
        params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);//定义框架布局器参数
        tparams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);//定义显示组件参数
        Bitmap bitmap = AlbumUtils.parseAlbum(mPlayer.getPlayingSong());
        if (bitmap == null) {
        } else {
            containLayoutbg.addView(BackgroundImageView.newInstance(getActivity(),bitmap,x,y),tparams);
        }
        for(int i=0;i<mPlayer.getPlayList().getSongs().size();i++){
            albumFragments.add(MusicAlbumFragment.newInstance(mPlayer.getPlayList().getSongs().get(i)));
        }
        mAlbumFragmentAdapter = new AlbumFragmentAdapter(getChildFragmentManager(),albumFragments);
        viewPagerAlbum.setAdapter(mAlbumFragmentAdapter);
        currentIndex = mPlayer.getPlayList().getSongs().indexOf(mPlayer.getPlayingSong());
        viewPagerAlbum.setCurrentItem(currentIndex);
        viewPagerAlbum.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.d("qifan","positionoffset "+positionOffset+" position "+position+" positionOffsetPixels "+positionOffsetPixels +" currentindex "+currentIndex);
            }

            @Override
            public void onPageSelected(int position) {
                changeSongPool.execute(new MusicSelectedJugeThread(position));
                changeBgPool.execute(new MusicChangeBgThread(position));
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class AlbumFragmentAdapter extends FragmentStatePagerAdapter{

        private ArrayList<MusicAlbumFragment> mFragments = new ArrayList<>();

        public  MusicAlbumFragment currentFragment;

        public AlbumFragmentAdapter(FragmentManager fm, ArrayList fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            if (mFragments == null) return 0;
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            currentFragment = (MusicAlbumFragment) object;
            super.setPrimaryItem(container, position, object);
        }
        public MusicAlbumFragment getFragment(int position) {
            return mFragments.get(position);
        }
    }
    class MusicSelectedJugeThread implements  Runnable{

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private int index;

        public MusicSelectedJugeThread(int index){
            this.index = index;
        }

        @Override
        public void run() {
            try {
                Thread.currentThread().sleep(1000);
                Message msg = new Message();
                if(index == currentIndex){
                    msg.what = 1;
                }
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    class MusicChangeBgThread implements  Runnable{

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private int index;

        public MusicChangeBgThread(int index){
            this.index = index;
        }

        @Override
        public void run() {
            Bitmap bitmap = AlbumUtils.parseAlbum(mPlayer.getPlayList().getSongs().get(index));
            if (bitmap != null) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = bitmap;
                mHandler.sendMessage(msg);
              }
        }
    }
    public void AlbumAnimation(String type,int index){
        switch (type){
            case "start":
                //start
                mAlbumFragmentAdapter.getFragment(index).startRotateAnimation();
                break;
            case "pause":
                //pause
                mAlbumFragmentAdapter.getFragment(index).pauseRotateAnimation();
                break;
            case "resume":
                //resume
                mAlbumFragmentAdapter.getFragment(index).resumeRotateAnimation();
                break;
            case "cancel":
                //cancel
                mAlbumFragmentAdapter.getFragment(index).cancelRotateAnimation();
                break;
            default:
                break;
        }
    }
}
