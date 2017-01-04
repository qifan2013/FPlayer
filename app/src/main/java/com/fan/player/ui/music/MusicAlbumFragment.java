package com.fan.player.ui.music;

/*
 * Project: FPlayer
 * Author: fan
 * Date: 2016/12/7
 * Time: 10:08
 */

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.player.R;
import com.fan.player.data.model.Song;
import com.fan.player.ui.base.BaseFragment;
import com.fan.player.ui.widget.RotateImageView;
import com.fan.player.utils.AlbumUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicAlbumFragment extends BaseFragment {

    @BindView(R.id.image_view_album)
    RotateImageView imageViewAlbum;
    @BindView(R.id.image_view_album_around)
    RotateImageView imageViewAlbumAround;

    public Song getmSong() {
        return mSong;
    }

    public void setmSong(Song mSong) {
        this.mSong = mSong;
    }

    private Song mSong;

    public static MusicAlbumFragment newInstance(Song song) {
        MusicAlbumFragment newFragment = new MusicAlbumFragment();
        Bundle bdl = new Bundle();
        bdl.putParcelable("mSong", song);
        newFragment.setArguments(bdl);
        return newFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.viewpager_album_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSong = getArguments().getParcelable("mSong");
        Bitmap bitmap = AlbumUtils.parseAlbum(mSong);
        if (bitmap == null) {
            imageViewAlbum.setImageResource(R.drawable.default_record_album);
        } else {
            imageViewAlbum.setImageBitmap(AlbumUtils.getCroppedBitmap(bitmap));
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startRotateAnimation(){
        imageViewAlbum.startRotateAnimation();
        imageViewAlbumAround.startRotateAnimation();
    }
    public void pauseRotateAnimation(){
        imageViewAlbum.pauseRotateAnimation();
        imageViewAlbumAround.pauseRotateAnimation();
    }
    public  void resumeRotateAnimation(){
        imageViewAlbum.resumeRotateAnimation();
        imageViewAlbumAround.resumeRotateAnimation();
    }
    public void cancelRotateAnimation(){
        imageViewAlbum.cancelRotateAnimation();
        imageViewAlbumAround.cancelRotateAnimation();
    }
}
