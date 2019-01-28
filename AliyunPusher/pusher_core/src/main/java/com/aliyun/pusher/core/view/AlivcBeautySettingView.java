package com.aliyun.pusher.core.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


import com.aliyun.pusher.core.R;
import com.aliyun.pusher.core.utils.AnimUitls;
import com.aliyun.pusher.core.utils.constants.BeautyConstants;
import com.aliyun.pusher.core.listener.OnBeautyParamsChangeListener;
import com.aliyun.pusher.core.listener.OnItemSeletedListener;
import com.aliyun.pusher.core.listener.OnViewClickListener;
import com.aliyun.pusher.core.module.BeautyParams;

/**
 * Created by Akira on 2018/5/31.
 * 美颜view, 主要负责显示BeautyDefaultSettiingView 和 BeautyDetailSettingView
 */

public class AlivcBeautySettingView extends FrameLayout {

    private final String TAG = "BeautySettingView";

    private Context mContext;

    private BeautyDefaultSettingView mDefaultSettingView;
    private BeautyDetailSettingView mDetailSettingView;
    private View mBlankBiew;
    private BeautyParams mParams;

    public AlivcBeautySettingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public AlivcBeautySettingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public AlivcBeautySettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setParams(BeautyParams params) {
        mDetailSettingView.setParams(params);
        mParams = params;
    }

    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;

    public void setBeautyParamsChangeListener(OnBeautyParamsChangeListener listener) {
        mBeautyParamsChangeListener = listener;
    }

    private OnViewClickListener mHideClickListener;

    public void setHideClickListener(OnViewClickListener listener) {
        mHideClickListener = listener;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.alivc_push_beauty_layout, this);
         mDefaultSettingView = findViewById(R.id.push_default_setting);
        mDetailSettingView = findViewById(R.id.push_detail_setting);
        mBlankBiew = findViewById(R.id.blank_view);

        mBlankBiew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHideClickListener != null) {
                    mHideClickListener.onClick();
                    mDetailSettingView.saveProgress();
                }
            }
        });

        mDefaultSettingView.setItemSelectedListener(new OnItemSeletedListener() {
            @Override
            public void onItemSelected(int postion) {
                if (postion < BeautyConstants.BEAUTY_MAP.size()) {
                    if (mParams == null) {
                        return;
                    }
                    Log.d(TAG, " default onBeautyChange");
                    mParams.beautyBuffing = BeautyConstants.BEAUTY_MAP.get(postion).beautyBuffing;
                    mParams.beautyRuddy = BeautyConstants.BEAUTY_MAP.get(postion).beautyRuddy;
                    mParams.beautyCheekPink = BeautyConstants.BEAUTY_MAP.get(postion).beautyCheekPink;
                    mParams.beautyBigEye = BeautyConstants.BEAUTY_MAP.get(postion).beautyBigEye;
                    mParams.beautySlimFace = BeautyConstants.BEAUTY_MAP.get(postion).beautySlimFace;
                    mParams.beautyShortenFace = BeautyConstants.BEAUTY_MAP.get(postion).beautyShortenFace;
                    mParams.beautyWhite = BeautyConstants.BEAUTY_MAP.get(postion).beautyWhite;
                    if (mBeautyParamsChangeListener != null) {
                        mBeautyParamsChangeListener.onBeautyChange(mParams);
                    }
                    if (mDetailSettingView != null) {
                        mDetailSettingView.setParams(mParams);
                    }
                }
            }
        });

        mDefaultSettingView.setSettingClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                //todo 切换到detail
                Log.d(TAG, "switch to detail");
                AnimUitls.startAppearAnimY(mDetailSettingView);
                mDetailSettingView.setVisibility(View.VISIBLE);

                AnimUitls.startDisappearAnimY(mDefaultSettingView);
                mDefaultSettingView.setVisibility(View.GONE);
            }
        });

        mDetailSettingView.setBeautyParamsChangeListener(new OnBeautyParamsChangeListener() {

            @Override
            public void onBeautyChange(BeautyParams param) {
                Log.d(TAG, "onBeautyChange");
                if (mBeautyParamsChangeListener != null) {
                    mBeautyParamsChangeListener.onBeautyChange(param);
                }
            }
        });

        mDetailSettingView.setBackClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                Log.d(TAG, "back to default");
                //切换到default
                AnimUitls.startAppearAnimY(mDefaultSettingView);
                mDefaultSettingView.setVisibility(View.VISIBLE);

                AnimUitls.startDisappearAnimY(mDetailSettingView);
                mDetailSettingView.setVisibility(View.GONE);
            }
        });
    }

}
