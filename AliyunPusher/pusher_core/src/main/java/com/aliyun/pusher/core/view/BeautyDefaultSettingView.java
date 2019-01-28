package com.aliyun.pusher.core.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.aliyun.pusher.core.R;
import com.aliyun.pusher.core.listener.OnItemSeletedListener;
import com.aliyun.pusher.core.listener.OnViewClickListener;
import com.aliyun.pusher.core.utils.SharedPreferenceUtils;

/**
 * Created by Akira on 2018/5/30.
 * 美颜界面, 默认参数
 */

public class BeautyDefaultSettingView extends LinearLayout {
    private Context mContext;

    private RadioGroup mRadioGroupView;

    private ImageView mBtBeautyDetail;
    private int mCheckedPosition;

    public BeautyDefaultSettingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BeautyDefaultSettingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BeautyDefaultSettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private OnItemSeletedListener mListener;

    public void setItemSelectedListener(OnItemSeletedListener listener) {
        mListener = listener;
        checkedPostion(SharedPreferenceUtils.getBeautyLevel(getContext()));
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.aliyun_beauty_default, this);

        mRadioGroupView = findViewById(R.id.beauty_default_group);
        mBtBeautyDetail = findViewById(R.id.iv_beauty_detail);

        mBtBeautyDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSettingClickListener != null) {
                    mSettingClickListener.onClick();
                }
            }
        });


        mRadioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mListener == null) {
                    return;
                }
                if (checkedId == R.id.beauty_0) {
                    mListener.onItemSelected(0);
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 0);
                } else if (checkedId == R.id.beauty_1) {
                    mListener.onItemSelected(1);
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 1);
                } else if (checkedId == R.id.beauty_2) {
                    mListener.onItemSelected(2);
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 2);
                } else if (checkedId == R.id.beauty_3) {
                    mListener.onItemSelected(3);
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 3);
                } else if (checkedId == R.id.beauty_4) {
                    mListener.onItemSelected(4);
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 4);
                } else if (checkedId == R.id.beauty_5) {
                    SharedPreferenceUtils.setBeautyLevel(getContext(), 5);
                    mListener.onItemSelected(5);
                }
            }
        });
    }

    private void checkedPostion(int position) {
        switch (position) {
            case 0:
                mRadioGroupView.check(R.id.beauty_0);
                break;
            case 1:
                mRadioGroupView.check(R.id.beauty_1);
                break;
            case 2:
                mRadioGroupView.check(R.id.beauty_2);
                break;
            case 3:
                mRadioGroupView.check(R.id.beauty_3);
                break;
            case 4:
                mRadioGroupView.check(R.id.beauty_4);
                break;
            case 5:
                mRadioGroupView.check(R.id.beauty_5);
                break;
        }
    }

    private OnViewClickListener mSettingClickListener;

    public void setSettingClickListener(OnViewClickListener listener) {
        mSettingClickListener = listener;
    }
}
