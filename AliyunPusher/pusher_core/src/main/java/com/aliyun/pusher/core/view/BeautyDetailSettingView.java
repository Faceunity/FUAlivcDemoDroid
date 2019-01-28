package com.aliyun.pusher.core.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.pusher.core.R;
import com.aliyun.pusher.core.listener.OnBeautyParamsChangeListener;
import com.aliyun.pusher.core.listener.OnProgresschangeListener;
import com.aliyun.pusher.core.listener.OnViewClickListener;
import com.aliyun.pusher.core.module.BeautyParams;
import com.aliyun.pusher.core.utils.constants.BeautyConstants;

/**
 * Created by Akira on 2018/5/30.
 * 美颜微调view
 */

public class BeautyDetailSettingView extends LinearLayout {
    private Context mContext;

    private RadioGroup mRadioGroupView;

    private ImageView mIvReset;

    private TextView mTvBack;

    private PushBeautySeekBar mSeekBar;

    private int mCheckedPosition = 0;

    private BeautyParams mParams;

    public BeautyDetailSettingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BeautyDetailSettingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BeautyDetailSettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setParams(BeautyParams params) {
        mParams = params;
        saveProgress();
    }

    private OnViewClickListener mBackClickListener;

    public void setBackClickListener(OnViewClickListener listener) {
        mBackClickListener = listener;
    }

    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;

    public void setBeautyParamsChangeListener(OnBeautyParamsChangeListener listener) {
        mBeautyParamsChangeListener = listener;
    }

    public void saveProgress(){
        switch (mCheckedPosition) {
            case BeautyConstants.BUFFING:
                mSeekBar.setLastProgress(mParams.beautyBuffing);
                break;
            case BeautyConstants.WHITENING:
                mSeekBar.setLastProgress(mParams.beautyWhite);
                break;
            case BeautyConstants.RUDDY:
                mSeekBar.setLastProgress(mParams.beautyRuddy);
                break;
            case BeautyConstants.SHORTEN_JAW:
                mSeekBar.setLastProgress(mParams.beautyShortenFace);
                break;
            case BeautyConstants.BIG_EYE:
                mSeekBar.setLastProgress(mParams.beautyBigEye);
                break;
            case BeautyConstants.THIN_FACE:
                mSeekBar.setLastProgress(mParams.beautySlimFace);
                break;
            case BeautyConstants.RED_LIPS:
                mSeekBar.setLastProgress(mParams.beautyCheekPink);
                break;
                default:
                    break;
        }
    }

    private void initView() {
        if (BeautyConstants.FLAVOR.contains("liveonly")) {
            LayoutInflater.from(mContext).inflate(R.layout.alivc_push_beauty_detail_rtc, this);
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.alivc_push_beauty_detail, this);
        }

        mRadioGroupView = findViewById(R.id.beauty_detail_group);
        mIvReset = findViewById(R.id.iv_reset);
        mTvBack = findViewById(R.id.tv_back);
        mSeekBar = findViewById(R.id.push_beauty_seekbar);
        mIvReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.resetProgress();
            }
        });

        mTvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackClickListener != null) {
                    mBackClickListener.onClick();
                }
            }
        });

        mSeekBar.setProgressChangeListener(new OnProgresschangeListener() {
            @Override
            public void onProgressChange(int progress) {
                if (mParams != null) {
                    switch (mCheckedPosition) {
                        case BeautyConstants.BUFFING:
                            if (mParams.beautyBuffing == progress) {
                                return;
                            }
                            mParams.beautyBuffing = progress;
                            break;

                        case BeautyConstants.WHITENING:
                            if (mParams.beautyWhite == progress) {
                                return;
                            }
                            mParams.beautyWhite = progress;
                            break;

                        case BeautyConstants.RUDDY:
                            if (mParams.beautyRuddy == progress) {
                                return;
                            }
                            mParams.beautyRuddy = progress;
                            break;

                        case BeautyConstants.RED_LIPS:
                            if (mParams.beautyCheekPink == progress) {
                                return;
                            }
                            mParams.beautyCheekPink = progress;
                            break;

                        case BeautyConstants.BIG_EYE:
                            if (mParams.beautyBigEye == progress) {
                                return;
                            }
                            mParams.beautyBigEye = progress;
                            break;

                        case BeautyConstants.THIN_FACE:
                            if (mParams.beautySlimFace == progress) {
                                return;
                            }
                            mParams.beautySlimFace = progress;
                            break;

                        case BeautyConstants.SHORTEN_JAW:
                            if (mParams.beautyShortenFace == progress) {
                                return;
                            }
                            mParams.beautyShortenFace = progress;
                            break;
                        default:
                            break;
                    }
                }

                if (mBeautyParamsChangeListener != null) {
                    mBeautyParamsChangeListener.onBeautyChange(mParams);
                }


            }
        });

        mRadioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.beauty_buffing) {
                    mCheckedPosition = BeautyConstants.BUFFING;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyBuffing);
                    }
                } else if (checkedId == R.id.beauty_whitening) {
                    mCheckedPosition = BeautyConstants.WHITENING;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyWhite);
                    }
                } else if (checkedId == R.id.beauty_ruddy) {
                    mCheckedPosition = BeautyConstants.RUDDY;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyRuddy);
                    }
                } else if (checkedId == R.id.beauty_red_lips) {
                    mCheckedPosition = BeautyConstants.RED_LIPS;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyCheekPink);
                    }
                } else if (checkedId == R.id.beauty_bigeye) {
                    mCheckedPosition = BeautyConstants.BIG_EYE;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyBigEye);
                    }
                } else if (checkedId == R.id.beauty_thin_face) {
                    mCheckedPosition = BeautyConstants.THIN_FACE;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautySlimFace);
                    }
                } else if (checkedId == R.id.beauty_shorten_jaw) {
                    mCheckedPosition = BeautyConstants.SHORTEN_JAW;
                    if (mParams != null) {
                        mSeekBar.setLastProgress(mParams.beautyShortenFace);
                    }
                }
            }
        });
        mRadioGroupView.check(R.id.beauty_buffing);
    }
}
