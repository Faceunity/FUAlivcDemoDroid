package com.alivc.live.pusher.demo.ui.myview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.demo.R;
import com.alivc.live.pusher.demo.utils.SharedPreferenceUtils;
import com.alivc.live.pusher.demo.listener.DialogVisibleListener;
import com.alivc.live.pusher.demo.ui.fragment.LivePushFragment;
import com.aliyun.pusher.core.listener.OnBeautyParamsChangeListener;
import com.aliyun.pusher.core.module.BeautyParams;
import com.aliyun.pusher.core.view.AlivcBeautySettingView;

/**
 * 美颜dialog
 */
public class PushBeautyDialog extends DialogFragment{

    private static final String BEAUTY_ON = "beauty_on";
    private TextView mBeautySwitch;
    private SeekBar mCheekPinkBar;
    private SeekBar mWhiteBar;
    private SeekBar mSkinBar;
    private SeekBar mRuddyBar;
    private SeekBar mSlimFaceBar;
    private SeekBar mShortenFaceBar;
    private SeekBar mBigEyeBar;

    private TextView mCheekPink;
    private TextView mWhite;
    private TextView mSkin;
    private TextView mRuddy;
    private TextView mSlimFace;
    private TextView mShortenFace;
    private TextView mBigEye;
    private boolean mBeautyOn = true;
    private LivePushFragment.BeautyListener mBeautyListener;

    private AlivcLivePusher mAlivcLivePusher = null;
    private AlivcBeautySettingView beautySettingView;

    public static PushBeautyDialog newInstance(boolean beauty) {
        PushBeautyDialog pushBeautyDialog = new PushBeautyDialog();
        Bundle args = new Bundle();
        args.putBoolean(BEAUTY_ON, beauty);
        pushBeautyDialog.setArguments(args);
        return pushBeautyDialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        if(getArguments() != null) {
            mBeautyOn = getArguments().getBoolean(BEAUTY_ON, true);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beautySettingView = new AlivcBeautySettingView(getContext());//inflater.inflate(R.layout.push_beauty, container);

        final BeautyParams params = new BeautyParams();
        params.beautyWhite = SharedPreferenceUtils.getWhiteValue(getActivity().getApplicationContext());
        params.beautyShortenFace = SharedPreferenceUtils.getBuffing(getActivity().getApplicationContext());
        params.beautySlimFace = SharedPreferenceUtils.getSlimFace(getActivity().getApplicationContext());
        params.beautyBigEye = SharedPreferenceUtils.getBigEye(getActivity().getApplicationContext());
        params.beautyCheekPink = SharedPreferenceUtils.getCheekpink(getActivity().getApplicationContext());
        params.beautyRuddy = SharedPreferenceUtils.getRuddy(getActivity().getApplicationContext());
        params.beautyBuffing = SharedPreferenceUtils.getBuffing(getActivity().getApplicationContext());
        beautySettingView.setParams(params);
        beautySettingView.setBeautyParamsChangeListener(new OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyChange(BeautyParams param) {
                if (param == null) {
                    mBeautyOn=false;
                    if (mBeautyListener != null) {
                        mBeautyListener.onBeautySwitch(mBeautyOn);
                    }
                } else {
                    if (!mBeautyOn) {
                        mBeautyOn = true;
                        if (mBeautyListener != null) {
                            mBeautyListener.onBeautySwitch(mBeautyOn);
                        }
                    }
                    if (mBeautyListener != null) {
                        mBeautyListener.onBeautySettingChange(params);
                    }
                    saveBeautySettingParams(params);
                }
            }
        });
        if (visibleListener!=null){
            visibleListener.isDialogVisible(true);
        }

//        mBeautySwitch = (TextView) view.findViewById(R.id.beauty_switch);
//        mBeautySwitch.setOnClickListener(this);
//        mBeautySwitch.setSelected(!mBeautyOn);
//        mBeautySwitch.setText(!mBeautyOn ? getString(R.string.beauty_on) : getString(R.string.beauty_off));
//        mCheekPinkBar = (SeekBar) view.findViewById(R.id.beauty_cheekpink_seekbar);
//        mWhiteBar = (SeekBar) view.findViewById(R.id.beauty_white_seekbar);
//        mSkinBar = (SeekBar) view.findViewById(R.id.beauty_skin_seekbar);
//        mRuddyBar = (SeekBar) view.findViewById(R.id.beauty_ruddy_seekbar);
//        mSlimFaceBar = (SeekBar) view.findViewById(R.id.beauty_thinface_seekbar);
//        mShortenFaceBar = (SeekBar) view.findViewById(R.id.beauty_shortenface_seekbar);
//        mBigEyeBar = (SeekBar) view.findViewById(R.id.beauty_bigeye_seekbar);
//        mCheekPink = (TextView) view.findViewById(R.id.cheekpink);
//        mWhite = (TextView) view.findViewById(R.id.white);
//        mSkin = (TextView) view.findViewById(R.id.skin);
//        mRuddy = (TextView) view.findViewById(R.id.ruddy);
//        mSlimFace = (TextView) view.findViewById(R.id.thinface);
//        mShortenFace = (TextView) view.findViewById(R.id.shortenface);
//        mBigEye = (TextView) view.findViewById(R.id.bigeye);
//        mCheekPinkBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mWhiteBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mSkinBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mRuddyBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mSlimFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mShortenFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mBigEyeBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        mWhite.setText(String.valueOf(SharedPreferenceUtils.getWhiteValue(getActivity().getApplicationContext())));
//        mWhiteBar.setProgress(SharedPreferenceUtils.getWhiteValue(getActivity().getApplicationContext()));
//        mSkin.setText(String.valueOf(SharedPreferenceUtils.getBuffing(getActivity().getApplicationContext())));
//        mSkinBar.setProgress(SharedPreferenceUtils.getBuffing(getActivity().getApplicationContext()));
//        mRuddy.setText(String.valueOf(SharedPreferenceUtils.getRuddy(getActivity().getApplicationContext())));
//        mRuddyBar.setProgress(SharedPreferenceUtils.getRuddy(getActivity().getApplicationContext()));
//        mCheekPink.setText(String.valueOf(SharedPreferenceUtils.getCheekpink(getActivity().getApplicationContext())));
//        mCheekPinkBar.setProgress(SharedPreferenceUtils.getCheekpink(getActivity().getApplicationContext()));
//        mSlimFace.setText(String.valueOf(SharedPreferenceUtils.getSlimFace(getActivity().getApplicationContext())));
//        mSlimFaceBar.setProgress(SharedPreferenceUtils.getSlimFace(getActivity().getApplicationContext()));
//        mShortenFace.setText(String.valueOf(SharedPreferenceUtils.getShortenFace(getActivity().getApplicationContext())));
//        mShortenFaceBar.setProgress(SharedPreferenceUtils.getShortenFace(getActivity().getApplicationContext()));
//        mBigEye.setText(String.valueOf(SharedPreferenceUtils.getBigEye(getActivity().getApplicationContext())));
//        mBigEyeBar.setProgress(SharedPreferenceUtils.getBigEye(getActivity().getApplicationContext()));
        return beautySettingView;
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        super.onResume();

        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();

        p.width = dpMetrics.widthPixels;
        p.height = dpMetrics.heightPixels/2;
        getDialog().getWindow().setAttributes(p);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    private void saveBeautySettingParams(BeautyParams params){
        SharedPreferenceUtils.setCheekPink(getActivity().getApplicationContext(), params.beautyCheekPink);
        SharedPreferenceUtils.setWhiteValue(getActivity().getApplicationContext(), params.beautyWhite);
        SharedPreferenceUtils.setBuffing(getActivity().getApplicationContext(), params.beautyBuffing);
        SharedPreferenceUtils.setRuddy(getActivity().getApplicationContext(), params.beautyRuddy);
        SharedPreferenceUtils.setSlimFace(getActivity().getApplicationContext(), params.beautySlimFace);
        SharedPreferenceUtils.setShortenFace(getActivity().getApplicationContext(), params.beautyShortenFace);
        SharedPreferenceUtils.setBigEye(getActivity().getApplicationContext(), params.beautyBigEye);
    }


    public void setAlivcLivePusher(AlivcLivePusher alivcLivePusher) {
        this.mAlivcLivePusher = alivcLivePusher;
    }

    public void setBeautyListener(LivePushFragment.BeautyListener beautyListener) {
        this.mBeautyListener = beautyListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (visibleListener!=null){
            visibleListener.isDialogVisible(false);
        }

    }
    private DialogVisibleListener visibleListener;

    public void setVisibleListener(DialogVisibleListener visibleListener) {
        this.visibleListener = visibleListener;
    }
}
