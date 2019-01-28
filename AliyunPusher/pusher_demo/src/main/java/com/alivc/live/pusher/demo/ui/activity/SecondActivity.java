package com.alivc.live.pusher.demo.ui.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alivc.live.pusher.demo.R;

/**
 * 直播场景列表
 *  显示 "普通推流" 和 "录屏直播" item选项
 */
public class SecondActivity extends ListActivity {


    String[] mModules = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        mModules = new String[]{
                getString(R.string.live_normal_function),
                getString(R.string.video_recording),
        };
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            mModules = null;
            mModules = new String[]{
                    getString(R.string.live_basic_function),
            };
        }
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModules));

    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, PushConfigActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, VideoRecordConfigActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
