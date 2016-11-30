package com.fan.player.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import com.fan.player.R;
import com.fan.player.ui.base.BaseActivity;
import com.fan.player.ui.base.BaseFragment;
import com.fan.player.ui.local.LocalFilesFragment;
import com.fan.player.ui.playlist.PlayListFragment;
import com.fan.player.ui.settings.SettingsFragment;

import java.util.List;

public class MainActivity extends BaseActivity {

    static final int DEFAULT_PAGE_INDEX = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindViews({R.id.radio_button_play_list, R.id.radio_button_local_files})
    List<RadioButton> radioButtons;

    String[] mTitles;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Main Controls' Titles
        mTitles = getResources().getStringArray(R.array.mp_main_titles);

        // Fragments
        BaseFragment[] fragments = new BaseFragment[mTitles.length];
        fragments[0] = new PlayListFragment();
        fragments[1] = new LocalFilesFragment();

        // Inflate ViewPager
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), mTitles, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.mp_margin_large));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Empty
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Empty
            }

            @Override
            public void onPageSelected(int position) {
                radioButtons.get(position).setChecked(true);
            }
        });

        radioButtons.get(DEFAULT_PAGE_INDEX).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnCheckedChanged({R.id.radio_button_play_list, R.id.radio_button_local_files})
    public void onRadioButtonChecked(RadioButton button, boolean isChecked) {
        if (isChecked) {
            onItemChecked(radioButtons.indexOf(button));
        }
    }

    private void onItemChecked(int position) {
        viewPager.setCurrentItem(position);
        toolbar.setTitle(mTitles[position]);
    }
}
