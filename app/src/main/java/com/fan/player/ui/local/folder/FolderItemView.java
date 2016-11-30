package com.fan.player.ui.local.folder;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fan.player.R;
import com.fan.player.data.model.Folder;
import com.fan.player.ui.base.adapter.IAdapterView;

/**
 * Project: FPlayer
 * User: fan
 * Date: 9/3/16
 * Time: 7:22 PM
 * Desc: FolderItemView
 */
public class FolderItemView extends RelativeLayout implements IAdapterView<Folder> {

    @BindView(R.id.text_view_name)
    TextView textViewName;
    @BindView(R.id.text_view_info)
    TextView textViewInfo;
    @BindView(R.id.layout_action)
    View buttonAction;

    public FolderItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_added_folder, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bind(Folder folder, int position) {
        textViewName.setText(folder.getName());
        textViewInfo.setText(getContext().getString(
                R.string.mp_local_files_folder_list_item_info_formatter,
                folder.getNumOfSongs(),
                folder.getPath()
        ));
    }
}
