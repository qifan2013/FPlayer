package com.fan.player.ui.local.all;

import android.content.Context;
import com.fan.player.R;
import com.fan.player.data.model.Song;
import com.fan.player.ui.common.AbstractSummaryAdapter;
import com.fan.player.ui.widget.RecyclerViewFastScroller;

import java.util.List;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/2/16
 * Time: 8:21 PM
 * Desc: LocalMusicAdapter
 */
public class LocalMusicAdapter extends AbstractSummaryAdapter<Song, LocalMusicItemView>
        implements RecyclerViewFastScroller.BubbleTextGetter {

    Context mContext;

    public LocalMusicAdapter(Context context, List<Song> data) {
        super(context, data);
        mContext = context;
    }

    @Override
    protected String getEndSummaryText(int dataCount) {
        return mContext.getString(R.string.mp_local_files_music_list_end_summary_formatter, dataCount);
    }

    @Override
    protected LocalMusicItemView createView(Context context) {
        return new LocalMusicItemView(context);
    }

    @Override
    public String getTextToShowInBubble(int position) {
        Song item = getItem(position);
        if (position > 0 && item == null) {
            item = getItem(position - 1);
        }
        return item.getDisplayName().substring(0, 1);
    }
}
