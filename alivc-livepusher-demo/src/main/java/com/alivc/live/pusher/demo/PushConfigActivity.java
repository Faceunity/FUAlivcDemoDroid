
package com.alivc.live.pusher.demo;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alivc.live.pusher.AlivcAudioAACProfileEnum;
import com.alivc.live.pusher.AlivcAudioChannelEnum;
import com.alivc.live.pusher.AlivcAudioSampleRateEnum;
import com.alivc.live.pusher.AlivcBeautyLevelEnum;
import com.alivc.live.pusher.AlivcEncodeModeEnum;
import com.alivc.live.pusher.AlivcImageFormat;
import com.alivc.live.pusher.AlivcLivePushCameraTypeEnum;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushConstants;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcQualityModeEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;
import com.alivc.live.pusher.AlivcSoundFormat;
import com.alivc.live.pusher.WaterMarkInfo;
import com.alivc.live.pusher.demo.auth.PushAuth;

import java.io.File;
import java.util.ArrayList;

import static com.alivc.live.pusher.AlivcAudioChannelEnum.AUDIO_CHANNEL_ONE;
import static com.alivc.live.pusher.AlivcAudioChannelEnum.AUDIO_CHANNEL_TWO;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_10;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_12;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_15;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_20;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_25;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_30;
import static com.alivc.live.pusher.AlivcFpsEnum.FPS_8;
import static com.alivc.live.pusher.AlivcLivePushConstants.DEFAULT_VALUE_INT_AUDIO_RETRY_COUNT;
import static com.alivc.live.pusher.AlivcLivePushConstants.DEFAULT_VALUE_INT_RETRY_INTERVAL;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;
import static com.alivc.live.pusher.AlivcVideoEncodeGopEnum.GOP_FIVE;
import static com.alivc.live.pusher.AlivcVideoEncodeGopEnum.GOP_FOUR;
import static com.alivc.live.pusher.AlivcVideoEncodeGopEnum.GOP_ONE;
import static com.alivc.live.pusher.AlivcVideoEncodeGopEnum.GOP_THREE;
import static com.alivc.live.pusher.AlivcVideoEncodeGopEnum.GOP_TWO;
import static com.alivc.live.pusher.demo.LivePushActivity.REQ_CODE_PUSH;

public class PushConfigActivity extends AppCompatActivity {
    private static final String TAG = "PushConfigActivity";

    private AlivcResolutionEnum mDefinition = AlivcResolutionEnum.RESOLUTION_540P;
    private static final int REQ_CODE_PERMISSION = 0x1111;
    private static final int PROGRESS_0 = 0;
    private static final int PROGRESS_16 = 16;
    private static final int PROGRESS_20 = 20;
    private static final int PROGRESS_33 = 33;
    private static final int PROGRESS_40 = 40;
    private static final int PROGRESS_50 = 50;
    private static final int PROGRESS_60 = 60;
    private static final int PROGRESS_66 = 66;
    private static final int PROGRESS_75 = 75;
    private static final int PROGRESS_80 = 80;
    private static final int PROGRESS_100 = 100;

    private static final int PROGRESS_AUDIO_320 = 30;
    private static final int PROGRESS_AUDIO_441 = 70;
    private static final int PROGRESS_AUDIO_480 = 100;
    private InputMethodManager manager;

    private LinearLayout mPublish;
    private SeekBar mResolution;
    private SeekBar mAudioRate;
    private SeekBar mFps;
    private SeekBar mMinFps;
    private TextView mResolutionText;
    private TextView mAudioRateText;
    private TextView mWaterPosition;
    private TextView mFpsText;
    private TextView mMinFpsText;

    private EditText mUrl;
    private EditText mTargetRate;
    private EditText mMinRate;
    private EditText mInitRate;
    private EditText mAudioBitRate;
    private EditText mRetryInterval;
    private EditText mRetryCount;
    private EditText mAuthTime;
    private EditText mPrivacyKey;

    private Switch mWaterMark;
    private Switch mPushMirror;
    private Switch mPreviewMirror;
    private Switch mHardCode;
    private Switch mAudioHardCode;
    private Switch mCamera;
    private Switch mAudioOnly;
    private Switch mVideoOnly;
    private Switch mAutoFocus;
    private Switch mBeautyOn;
    private Switch mAsync;
    private Switch mFlash;
    private Switch mLog;
    private Switch mBitrate;
    private Switch mVariableResolution;
    private Switch mExtern;
    //private Switch mExternMix;
    private Switch mPauseImage;
    private Switch mNetworkImage;
    private ImageView mQr;
    private ImageView mCopy;
    private ImageView mBack;
    private RadioGroup mAudioRadio;
    private RadioGroup mQualityMode;
    private RadioGroup mGop;
    private RadioGroup mOrientation;
    private RadioGroup mDisplayMode;
    private RadioGroup mBeautyLevel;
    private RadioGroup mAudioProfiles;

    //美颜相关数据
    private SeekBar mCheekPinkBar;
    private SeekBar mWhiteBar;
    private SeekBar mSkinBar;
    private SeekBar mRuddyBar;
    private SeekBar mSlimFaceBar;
    private SeekBar mShortenFaceBar;
    private SeekBar mBigEyeBar;

    private TextView mCheekpink;
    private TextView mWhite;
    private TextView mSkin;
    private TextView mRuddy;
    private TextView mSlimFace;
    private TextView mShortenFace;
    private TextView mBigEye;
    private TextView mPushTex;

    private LinearLayout mWaterLinear;

    private AlivcLivePushConfig mAlivcLivePushConfig;
    private boolean mAsyncValue = true;
    private boolean mAudioOnlyPush = false;
    private boolean mVideoOnlyPush = false;
    private AlivcPreviewOrientationEnum mOrientationEnum = ORIENTATION_PORTRAIT;
    private AlivcQualityModeEnum mQualityModeEnum = AlivcQualityModeEnum.QM_RESOLUTION_FIRST;

    private ArrayList<WaterMarkInfo> waterMarkInfos = new ArrayList<>();

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean isFlash = false;

    private String mAuthTimeStr = "";
    private String mPrivacyKeyStr = "";
    private boolean mMixStream = false;

    private boolean mShowDebugView = false;

    private AlivcLivePusher mAlivcLivePusher = null;

    private ClipboardManager cm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.push_setting);
        mAlivcLivePushConfig = new AlivcLivePushConfig();
        if(mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation() || mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation())
        {
            mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
            mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
        } else {
            mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network.png");
            mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push.png");
        }
        AlivcLivePushConfig.setMediaProjectionPermissionResultData(null);
        initView();
        setClick();
        Common.copyAsset(this);
        Common.copyAll(this);
        addWaterMarkInfo();
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void turnOnBitRateFps(boolean on)
    {
        if(!on)
        {
            mFps.setProgress(83);
            mAlivcLivePushConfig.setFps(FPS_25);
            mFpsText.setText(String.valueOf(FPS_25.getFps()));
            mTargetRate.setFocusable(false);
            mMinRate.setFocusable(false);
            mInitRate.setFocusable(false);
            mFps.setFocusable(false);
            mTargetRate.setFocusableInTouchMode(false);
            mMinRate.setFocusableInTouchMode(false);
            mInitRate.setFocusableInTouchMode(false);
            mFps.setFocusableInTouchMode(false);
        } else {
            mTargetRate.setFocusable(true);
            mMinRate.setFocusable(true);
            mInitRate.setFocusable(true);
            mTargetRate.setFocusableInTouchMode(true);
            mMinRate.setFocusableInTouchMode(true);
            mInitRate.setFocusableInTouchMode(true);
            mTargetRate.requestFocus();
            mInitRate.requestFocus();
            mMinRate.requestFocus();
        }
    }

    private void initView() {
        mUrl = (EditText) findViewById(R.id.url_editor);
        mPublish = (LinearLayout) findViewById(R.id.beginPublish);
        mPushTex = (TextView) findViewById(R.id.pushStatusTex);
        mResolution = (SeekBar) findViewById(R.id.resolution_seekbar);
        mResolutionText = (TextView) findViewById(R.id.resolution_text);
        mFps = (SeekBar) findViewById(R.id.fps_seekbar);
        mFpsText = (TextView) findViewById(R.id.fps_text);
        mTargetRate = (EditText) findViewById(R.id.target_rate_edit);
        mMinRate = (EditText) findViewById(R.id.min_rate_edit);
        mInitRate = (EditText) findViewById(R.id.init_rate_edit);
        mAudioBitRate = (EditText) findViewById(R.id.audio_bitrate);
        mAudioRate = (SeekBar) findViewById(R.id.audio_rate_seekbar);
        mAudioRateText = (TextView) findViewById(R.id.audio_rate_text);
        mRetryInterval = (EditText) findViewById(R.id.retry_interval);
        mRetryCount = (EditText) findViewById(R.id.retry_count);
        mAuthTime = (EditText) findViewById(R.id.auth_time);
        mPrivacyKey = (EditText) findViewById(R.id.privacy_key);
        mMinFps = (SeekBar) findViewById(R.id.min_fps_seekbar);
        mMinFpsText = (TextView) findViewById(R.id.min_fps_text);
        mWaterMark = (Switch) findViewById(R.id.watermark_switch);
        mWaterPosition = (TextView) findViewById(R.id.water_position);
        mPushMirror = (Switch) findViewById(R.id.push_mirror_switch);
        mPreviewMirror = (Switch) findViewById(R.id.preview_mirror_switch);
        mHardCode = (Switch) findViewById(R.id.hard_switch);
        mAudioHardCode = (Switch) findViewById(R.id.audio_hardenc);
        mCamera = (Switch) findViewById(R.id.camera_switch);
        mAudioOnly = (Switch) findViewById(R.id.audio_only_switch);
        mVideoOnly = (Switch) findViewById(R.id.video_only_switch);
        mAutoFocus = (Switch) findViewById(R.id.autofocus_switch);
        mBeautyOn = (Switch) findViewById(R.id.beautyOn_switch);
        mAsync = (Switch) findViewById(R.id.async_switch);
        mFlash = (Switch) findViewById(R.id.flash_switch);
        mLog = (Switch) findViewById(R.id.log_switch);
        mBitrate = (Switch) findViewById(R.id.bitrate_control);
        mVariableResolution = (Switch) findViewById(R.id.variable_resolution);
        mExtern = (Switch) findViewById(R.id.extern_video);
        //mExternMix = (Switch) findViewById(R.id.extern_video_mix);
        mPauseImage = (Switch) findViewById(R.id.pause_image);
        mNetworkImage = (Switch) findViewById(R.id.network_image);
        mQr = (ImageView) findViewById(R.id.qr_code);
        mCopy = (ImageView) findViewById(R.id.copy_paste);
        mBack = (ImageView) findViewById(R.id.iv_back);
        mAudioRadio = (RadioGroup) findViewById(R.id.main_audio);
        mQualityMode = (RadioGroup) findViewById(R.id.quality_modes);
        mGop = (RadioGroup) findViewById(R.id.main_gop);
        mOrientation = (RadioGroup) findViewById(R.id.main_orientation);
        mDisplayMode = (RadioGroup) findViewById(R.id.setting_display_mode);
        mBeautyLevel = (RadioGroup) findViewById(R.id.beauty_modes);
        mAudioProfiles = (RadioGroup) findViewById(R.id.audio_profiles);

        mCheekPinkBar = (SeekBar) findViewById(R.id.beauty_cheekpink_seekbar);
        mWhiteBar = (SeekBar) findViewById(R.id.beauty_white_seekbar);
        mSkinBar = (SeekBar) findViewById(R.id.beauty_skin_seekbar);
        mRuddyBar = (SeekBar) findViewById(R.id.beauty_ruddy_seekbar);
        mSlimFaceBar = (SeekBar) findViewById(R.id.beauty_thinface_seekbar);
        mShortenFaceBar = (SeekBar) findViewById(R.id.beauty_shortenface_seekbar);
        mBigEyeBar = (SeekBar) findViewById(R.id.beauty_bigeye_seekbar);
        mCheekpink = (TextView) findViewById(R.id.cheekpink);
        mWhite = (TextView) findViewById(R.id.white);
        mSkin = (TextView) findViewById(R.id.skin);
        mRuddy = (TextView) findViewById(R.id.ruddy);
        mSlimFace = (TextView) findViewById(R.id.thinface);
        mShortenFace = (TextView) findViewById(R.id.shortenface);
        mBigEye = (TextView) findViewById(R.id.bigeye);
        mWaterLinear = (LinearLayout) findViewById(R.id.water_linear);
        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
        turnOnBitRateFps(false);

        mUrl.setText(PushAuth.wrapAuthUrl());
    }

    private void setClick() {
        mPublish.setOnClickListener(onClickListener);
        mWaterPosition.setOnClickListener(onClickListener);
        mWaterMark.setOnCheckedChangeListener(onCheckedChangeListener);
        mPushMirror.setOnCheckedChangeListener(onCheckedChangeListener);
        mPreviewMirror.setOnCheckedChangeListener(onCheckedChangeListener);
        mHardCode.setOnCheckedChangeListener(onCheckedChangeListener);
        mAudioHardCode.setOnCheckedChangeListener(onCheckedChangeListener);
        mCamera.setOnCheckedChangeListener(onCheckedChangeListener);
        mAudioOnly.setOnCheckedChangeListener(onCheckedChangeListener);
        mVideoOnly.setOnCheckedChangeListener(onCheckedChangeListener);
        mAutoFocus.setOnCheckedChangeListener(onCheckedChangeListener);
        mBeautyOn.setOnCheckedChangeListener(onCheckedChangeListener);
        mResolution.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mAudioRate.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mExtern.setOnCheckedChangeListener(onCheckedChangeListener);
        mFps.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mMinFps.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mAsync.setOnCheckedChangeListener(onCheckedChangeListener);
        mFlash.setOnCheckedChangeListener(onCheckedChangeListener);
        mLog.setOnCheckedChangeListener(onCheckedChangeListener);
        mBitrate.setOnCheckedChangeListener(onCheckedChangeListener);
        mVariableResolution.setOnCheckedChangeListener(onCheckedChangeListener);
        //mExternMix.setOnCheckedChangeListener(onCheckedChangeListener);
        mPauseImage.setOnCheckedChangeListener(onCheckedChangeListener);
        mNetworkImage.setOnCheckedChangeListener(onCheckedChangeListener);
        mQr.setOnClickListener(onClickListener);
        mCopy.setOnClickListener(onClickListener);
        mBack.setOnClickListener(onClickListener);
        mAudioRadio.setOnCheckedChangeListener(mAudioListener);
        mQualityMode.setOnCheckedChangeListener(mQualityListener);
        mGop.setOnCheckedChangeListener(mGopListener);
        mOrientation.setOnCheckedChangeListener(mOrientationListener);
        mDisplayMode.setOnCheckedChangeListener(mDisplayModeListener);
        mBeautyLevel.setOnCheckedChangeListener(mBeautyLevelListener);
        mAudioProfiles.setOnCheckedChangeListener(mAudioProfileListener);
        mCheekPinkBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mWhiteBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSkinBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mRuddyBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSlimFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mShortenFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mBigEyeBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.beginPublish:
                    //
                    if(getPushConfig() != null) {
                        LivePushActivity.startActivity(PushConfigActivity.this, mAlivcLivePushConfig, mUrl.getText().toString(), mAsyncValue, mAudioOnlyPush, mVideoOnlyPush, mOrientationEnum, mCameraId, isFlash, mAuthTimeStr, mPrivacyKeyStr, mMixStream, mAlivcLivePushConfig.isExternMainStream());
                    }
                    break;
                case R.id.qr_code:
                    if (ContextCompat.checkSelfPermission(PushConfigActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Do not have the permission of camera, request it.
                        ActivityCompat.requestPermissions(PushConfigActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                    } else {
                        // Have gotten the permission
                        startCaptureActivityForResult();
                    }
                    break;
                case R.id.water_position:
                    PushWaterMarkDialog pushWaterMarkDialog = new PushWaterMarkDialog();
                    pushWaterMarkDialog.setWaterMarkInfo(waterMarkInfos);
                    pushWaterMarkDialog.show(getSupportFragmentManager(), "waterDialog");
                    break;
                case R.id.iv_back:
                    finish();
                case R.id.copy_paste:
                    if(cm != null) {
                        if(mUrl.getText().toString() != null && mUrl.getText().toString().startsWith("rtmp://push-demo-rtmp.aliyunlive.com/test/stream")) {
                            ClipData mClipData = ClipData.newPlainText("Label", "rtmp://push-demo.aliyunlive.com/test/stream"+mUrl.getText().toString().substring(48,mUrl.getText().length()));
                            cm.setPrimaryClip(mClipData);
                            Toast.makeText(getApplicationContext(),R.string.promt_copy_url, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),R.string.promt_copy_customized_url, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;    
                default:
                    break;
            }
        }
    };

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();
            if(id == R.id.watermark_switch) {
                if(mWaterPosition != null) {
                    mWaterPosition.setClickable(isChecked);
                    mWaterPosition.setTextColor(isChecked ? getResources().getColor(R.color.text_blue) : getResources().getColor(R.color.darker_gray));
                }
            } else if(id == R.id.push_mirror_switch) {
                mAlivcLivePushConfig.setPushMirror(isChecked);
                SharedPreferenceUtils.setPushMirror(getApplicationContext(), isChecked);
            } else if(id == R.id.preview_mirror_switch) {
                mAlivcLivePushConfig.setPreviewMirror(isChecked);
                SharedPreferenceUtils.setPreviewMirror(getApplicationContext(), isChecked);
            } else if(id == R.id.hard_switch){
                mAlivcLivePushConfig.setVideoEncodeMode(isChecked ? AlivcEncodeModeEnum.Encode_MODE_HARD : AlivcEncodeModeEnum.Encode_MODE_SOFT);
            } else if(id == R.id.audio_hardenc){
                mAlivcLivePushConfig.setAudioEncodeMode(isChecked ? AlivcEncodeModeEnum.Encode_MODE_HARD : AlivcEncodeModeEnum.Encode_MODE_SOFT);
            } else if(id == R.id.camera_switch) {
                mAlivcLivePushConfig.setCameraType(isChecked ? AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT: AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK);
                mCameraId = (isChecked ? AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT.getCameraId(): AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK.getCameraId());
            } else if(id == R.id.audio_only_switch) {
                mAudioOnlyPush = isChecked;
                mAlivcLivePushConfig.setAudioOnly(isChecked);
            }  else if(id == R.id.video_only_switch) {
                mVideoOnlyPush = isChecked;
                mAlivcLivePushConfig.setVideoOnly(isChecked);
            } else if(id == R.id.autofocus_switch) {
                mAlivcLivePushConfig.setAutoFocus(isChecked);
                SharedPreferenceUtils.setAutofocus(getApplicationContext(), isChecked);
            } else if(id == R.id.beautyOn_switch) {
                mAlivcLivePushConfig.setBeautyOn(isChecked);
                SharedPreferenceUtils.setBeautyOn(getApplicationContext(), isChecked);
            } else if(id == R.id.async_switch) {
                mAsyncValue = isChecked;
            } else if(id == R.id.flash_switch) {
                mAlivcLivePushConfig.setFlash(isChecked);
                isFlash = isChecked;
            } else if(id == R.id.log_switch) {
                if(isChecked) {
                    LogcatHelper.getInstance(getApplicationContext()).start();
                } else {
                    LogcatHelper.getInstance(getApplicationContext()).stop();
                }
            } else if(id == R.id.bitrate_control)
            {
                mAlivcLivePushConfig.setEnableAutoResolution(isChecked);
            } else if(id == R.id.variable_resolution)
            {
                mAlivcLivePushConfig.setEnableAutoResolution(isChecked);
            } else if(id == R.id.extern_video) {
                mAlivcLivePushConfig.setExternMainStream(isChecked, AlivcImageFormat.IMAGE_FORMAT_YUVNV12, AlivcSoundFormat.SOUND_FORMAT_S16);
                mAlivcLivePushConfig.setAudioChannels(AlivcAudioChannelEnum.AUDIO_CHANNEL_ONE);
                mAlivcLivePushConfig.setAudioSamepleRate(AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_44100);
            } else if(id == R.id.pause_image) {
                if(!isChecked) {
                    mAlivcLivePushConfig.setPausePushImage("");
                } else {
                    if(mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation() || mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation())
                    {
                        mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
                    } else {
                        mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push.png");
                    }
                }
            } else if(id == R.id.network_image) {
                if(!isChecked) {
                    mAlivcLivePushConfig.setNetworkPoorPushImage("");
                } else {
                    if(mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT.getOrientation() || mAlivcLivePushConfig.getPreviewOrientation() == AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT.getOrientation())
                    {
                        mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
                    } else {
                        mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network.png");
                    }
                }
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int seekBarId = seekBar.getId();
            if(mResolution.getId() == seekBarId) {
                if(progress <= PROGRESS_0){
                    mDefinition = AlivcResolutionEnum.RESOLUTION_180P;
                    mResolutionText.setText(R.string.setting_resolution_180P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                } else if (progress > PROGRESS_0 && progress <= PROGRESS_20) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_240P;
                    mResolutionText.setText(R.string.setting_resolution_240P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                } else if(progress > PROGRESS_20 && progress <= PROGRESS_40){
                    mDefinition = AlivcResolutionEnum.RESOLUTION_360P;
                    mResolutionText.setText(R.string.setting_resolution_360P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                } else if(progress > PROGRESS_40 && progress <= PROGRESS_60) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_480P;
                    mResolutionText.setText(R.string.setting_resolution_480P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                } else if(progress > PROGRESS_60 && progress <= PROGRESS_80){
                    mDefinition = AlivcResolutionEnum.RESOLUTION_540P;
                    mResolutionText.setText(R.string.setting_resolution_540P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                }else if(progress > PROGRESS_80){
                    mDefinition = AlivcResolutionEnum.RESOLUTION_720P;
                    mResolutionText.setText(R.string.setting_resolution_720P);
                    if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_RESOLUTION_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else if(mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_FLUENCY_FIRST))
                    {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    } else {
                        mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                        mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                        mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                        SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                        SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                    }
                }
            } else if (mAudioRate.getId() == seekBarId) {
                if(progress <=30) {
                    mAlivcLivePushConfig.setAudioSamepleRate(AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_32000);
                    mAudioRateText.setText(getString(R.string.setting_audio_320));
                } else if(progress>30 && progress <=70){
                    mAlivcLivePushConfig.setAudioSamepleRate(AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_44100);
                    mAudioRateText.setText(getString(R.string.setting_audio_441));
                } else if(progress>70){
                    mAlivcLivePushConfig.setAudioSamepleRate(AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_48000);
                    mAudioRateText.setText(getString(R.string.setting_audio_480));
                }
            }else if (mFps.getId() == seekBarId) {
                if(!mAlivcLivePushConfig.getQualityMode().equals(AlivcQualityModeEnum.QM_CUSTOM))
                {
                    mFps.setProgress(83);
                    mAlivcLivePushConfig.setFps(FPS_25);
                    mFpsText.setText(String.valueOf(FPS_25.getFps()));
                    return;
                }
                if(progress <= PROGRESS_0){
                    mAlivcLivePushConfig.setFps(FPS_8);
                    mFpsText.setText(String.valueOf(FPS_8.getFps()));
                }else if(progress > PROGRESS_0 && progress <= PROGRESS_16){
                    mAlivcLivePushConfig.setFps(FPS_10);
                    mFpsText.setText(String.valueOf(FPS_10.getFps()));
                }else if(progress > PROGRESS_16 && progress <= PROGRESS_33){
                    mAlivcLivePushConfig.setFps(FPS_12);
                    mFpsText.setText(String.valueOf(FPS_12.getFps()));
                }else if(progress > PROGRESS_33 && progress <= PROGRESS_50){
                    mAlivcLivePushConfig.setFps(FPS_15);
                    mFpsText.setText(String.valueOf(FPS_15.getFps()));
                }else if(progress > PROGRESS_50 && progress <= PROGRESS_66) {
                    mAlivcLivePushConfig.setFps(FPS_20);
                    mFpsText.setText(String.valueOf(FPS_20.getFps()));
                }else if(progress > PROGRESS_66 && progress <= PROGRESS_80) {
                    mAlivcLivePushConfig.setFps(FPS_25);
                    mFpsText.setText(String.valueOf(FPS_25.getFps()));
                }else if(progress > PROGRESS_80) {
                    mAlivcLivePushConfig.setFps(FPS_30);
                    mFpsText.setText(String.valueOf(FPS_30.getFps()));
                }
            } else if(mMinFps.getId() == seekBarId) {
                if(progress <= PROGRESS_0){
                    mAlivcLivePushConfig.setMinFps(FPS_8);
                    mMinFpsText.setText(String.valueOf(FPS_8.getFps()));
                }else if(progress > PROGRESS_0 && progress <= PROGRESS_16){
                    mAlivcLivePushConfig.setMinFps(FPS_10);
                    mMinFpsText.setText(String.valueOf(FPS_10.getFps()));
                }else if(progress > PROGRESS_16 && progress <= PROGRESS_33){
                    mAlivcLivePushConfig.setMinFps(FPS_12);
                    mMinFpsText.setText(String.valueOf(FPS_12.getFps()));
                }else if(progress > PROGRESS_33 && progress <= PROGRESS_50){
                    mAlivcLivePushConfig.setMinFps(FPS_15);
                    mMinFpsText.setText(String.valueOf(FPS_15.getFps()));
                }else if(progress > PROGRESS_50 && progress <= PROGRESS_66) {
                    mAlivcLivePushConfig.setMinFps(FPS_20);
                    mMinFpsText.setText(String.valueOf(FPS_20.getFps()));
                }else if(progress > PROGRESS_66 && progress <= PROGRESS_80) {
                    mAlivcLivePushConfig.setMinFps(FPS_25);
                    mMinFpsText.setText(String.valueOf(FPS_25.getFps()));
                }else if(progress > PROGRESS_80) {
                    mAlivcLivePushConfig.setMinFps(FPS_30);
                    mMinFpsText.setText(String.valueOf(FPS_30.getFps()));
                }
            } else if(mCheekPinkBar.getId() == seekBarId) {
                mCheekpink.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyCheekPink(progress);
                    SharedPreferenceUtils.setCheekPink(getApplicationContext(), progress);
                }
            } else if(mWhiteBar.getId() == seekBarId) {
                mWhite.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyWhite(progress);
                    SharedPreferenceUtils.setWhiteValue(getApplicationContext(), progress);
                }
            } else if(mSkinBar.getId() == seekBarId) {
                mSkin.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyBuffing(progress);
                    SharedPreferenceUtils.setBuffing(getApplicationContext(), progress);
                }
            } else if (mRuddyBar.getId() == seekBarId) {
                mRuddy.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyRuddy(progress);
                    SharedPreferenceUtils.setRuddy(getApplicationContext(), progress);
                }
            } else if(mSlimFaceBar.getId() == seekBarId) {
                mSlimFace.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyThinFace(progress);
                    SharedPreferenceUtils.setSlimFace(getApplicationContext(), progress);
                }
            } else if(mShortenFaceBar.getId() == seekBarId) {
                mShortenFace.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyShortenFace(progress);
                    SharedPreferenceUtils.setShortenFace(getApplicationContext(), progress);
                }
            } else if(mBigEyeBar.getId() == seekBarId) {
                mBigEye.setText(String.valueOf(progress));
                if(mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setBeautyBigEye(progress);
                    SharedPreferenceUtils.setBigEye(getApplicationContext(), progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if(mResolution.getId() == seekBar.getId()) {
                if(progress < PROGRESS_0){
                    seekBar.setProgress(0);
                } else if (progress > PROGRESS_0 && progress <= PROGRESS_20) {
                    seekBar.setProgress(PROGRESS_20);
                } else if(progress > PROGRESS_20 && progress <= PROGRESS_40){
                    seekBar.setProgress(PROGRESS_40);
                } else if(progress > PROGRESS_40 && progress <= PROGRESS_60){
                    seekBar.setProgress(PROGRESS_60);
                } else if(progress > PROGRESS_60 && progress <= PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_80);
                } else if(progress > PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_100);
                }
            } else if (mFps.getId() == seekBar.getId()) {
                if(progress <= PROGRESS_0){
                    seekBar.setProgress(0);
                }else if(progress > PROGRESS_0 && progress <= PROGRESS_16){
                    seekBar.setProgress(PROGRESS_16);
                }else if(progress > PROGRESS_16 && progress <= PROGRESS_33){
                    seekBar.setProgress(PROGRESS_33);
                }else if(progress > PROGRESS_33 && progress <= PROGRESS_50){
                    seekBar.setProgress(PROGRESS_50);
                }else if(progress > PROGRESS_50 && progress <= PROGRESS_66) {
                    seekBar.setProgress(PROGRESS_66);
                }else if(progress > PROGRESS_66 && progress <= PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_80);
                }else if(progress > PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_100);
                }
            } else if(mAudioRate.getId() == seekBar.getId()) {
                if(progress <= 30) {
                    seekBar.setProgress(PROGRESS_AUDIO_320);
                } else if(progress > 30 && progress<=70){
                    seekBar.setProgress(PROGRESS_AUDIO_441);
                } else if(progress > 70){
                    seekBar.setProgress(PROGRESS_AUDIO_480);
                }
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mQualityListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.resolution_first:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_RESOLUTION_FIRST);
                        if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_180P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_240P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_360P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_480P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_540P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_720P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_RESOLUTION_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        }
                    }
                    turnOnBitRateFps(false);
                    break;
                case R.id.fluency_first:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_FLUENCY_FIRST);
                        if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_180P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_240P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_360P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_480P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_540P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_720P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P_FLUENCY_FIRST.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        }
                    }
                    turnOnBitRateFps(false);
                    break;
                case R.id.custom_mode:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setQualityMode(AlivcQualityModeEnum.QM_CUSTOM);
                        if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_180P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_180P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_240P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_240P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_360P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_360P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_480P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_480P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_540P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_540P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        } else if(mDefinition.equals(AlivcResolutionEnum.RESOLUTION_720P))
                        {
                            mTargetRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate()));
                            mMinRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate()));
                            mInitRate.setHint(String.valueOf(AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_INIT_BITRATE.getBitrate()));
                            SharedPreferenceUtils.setHintTargetBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_TARGET_BITRATE.getBitrate());
                            SharedPreferenceUtils.setHintMinBit(getApplicationContext(), AlivcLivePushConstants.BITRATE_720P.DEFAULT_VALUE_INT_MIN_BITRATE.getBitrate());
                        }
                    }
                    turnOnBitRateFps(true);
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mAudioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.audio_channel_one:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioChannels(AUDIO_CHANNEL_ONE);
                    }
                    break;
                case R.id.audio_channel_two:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioChannels(AUDIO_CHANNEL_TWO);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mGopListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.gop_one:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setVideoEncodeGop(GOP_ONE);
                    }
                    break;
                case R.id.gop_two:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setVideoEncodeGop(GOP_TWO);
                    }
                    break;
                case R.id.gop_three:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setVideoEncodeGop(GOP_THREE);
                    }
                    break;
                case R.id.gop_four:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setVideoEncodeGop(GOP_FOUR);
                    }
                    break;
                case R.id.gop_five:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setVideoEncodeGop(GOP_FIVE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mOrientationListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.portrait:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_PORTRAIT);
                        mOrientationEnum = ORIENTATION_PORTRAIT;
                        if(mAlivcLivePushConfig.getPausePushImage() != null && !mAlivcLivePushConfig.getPausePushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push.png");
                        }
                        if(mAlivcLivePushConfig.getNetworkPoorPushImage() != null && !mAlivcLivePushConfig.getNetworkPoorPushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network.png");
                        }
                    }
                    break;
                case R.id.home_left:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_LANDSCAPE_HOME_LEFT);
                        mOrientationEnum = ORIENTATION_LANDSCAPE_HOME_LEFT;
                        if(mAlivcLivePushConfig.getPausePushImage() != null && !mAlivcLivePushConfig.getPausePushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
                        }
                        if(mAlivcLivePushConfig.getNetworkPoorPushImage() != null && !mAlivcLivePushConfig.getNetworkPoorPushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
                        }
                    }
                    break;
                case R.id.home_right:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_LANDSCAPE_HOME_RIGHT);
                        mOrientationEnum = ORIENTATION_LANDSCAPE_HOME_RIGHT;
                        if(mAlivcLivePushConfig.getPausePushImage() != null && !mAlivcLivePushConfig.getPausePushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
                        }
                        if(mAlivcLivePushConfig.getNetworkPoorPushImage() != null && !mAlivcLivePushConfig.getNetworkPoorPushImage().equals(""))
                        {
                            mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mDisplayModeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.full:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_SCALE_FILL);
                        SharedPreferenceUtils.setDisplayFit(getApplicationContext(), AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_SCALE_FILL

                            .getPreviewDisplayMode());
                    }
                    break;
                case R.id.fit:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT);
                        SharedPreferenceUtils.setDisplayFit(getApplicationContext(), AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT

                            .getPreviewDisplayMode());
                    }
                    break;
                case R.id.cut:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setPreviewDisplayMode(AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL);
                        SharedPreferenceUtils.setDisplayFit(getApplicationContext(), AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL

                            .getPreviewDisplayMode());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mBeautyLevelListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.beauty_normal:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setBeautyLevel(AlivcBeautyLevelEnum.BEAUTY_Normal);
                    }
                    break;
                case R.id.beauty_professional:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setBeautyLevel(AlivcBeautyLevelEnum.BEAUTY_Professional);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mAudioProfileListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.audio_profile_lc:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LC);
                    }
                    break;
                case R.id.audio_profile_he:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.HE_AAC);
                    }
                    break;
                case R.id.audio_profile_hev2:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.HE_AAC_v2);
                    }
                    break;
                case R.id.audio_profile_ld:
                    if(mAlivcLivePushConfig != null) {
                        mAlivcLivePushConfig.setAudioProfile(AlivcAudioAACProfileEnum.AAC_LD);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mWhite.setText(String.valueOf(SharedPreferenceUtils.getWhiteValue(getApplicationContext())));
        mWhiteBar.setProgress(SharedPreferenceUtils.getWhiteValue(getApplicationContext()));
        mSkin.setText(String.valueOf(SharedPreferenceUtils.getBuffing(getApplicationContext())));
        mSkinBar.setProgress(SharedPreferenceUtils.getBuffing(getApplicationContext()));
        mRuddy.setText(String.valueOf(SharedPreferenceUtils.getRuddy(getApplicationContext())));
        mRuddyBar.setProgress(SharedPreferenceUtils.getRuddy(getApplicationContext()));
        mCheekpink.setText(String.valueOf(SharedPreferenceUtils.getCheekpink(getApplicationContext())));
        mCheekPinkBar.setProgress(SharedPreferenceUtils.getCheekpink(getApplicationContext()));
        mPushMirror.setChecked(SharedPreferenceUtils.isPushMirror(getApplicationContext()));
        mPreviewMirror.setChecked(SharedPreferenceUtils.isPreviewMirror(getApplicationContext()));
        mAutoFocus.setChecked(SharedPreferenceUtils.isAutoFocus(getApplicationContext()));
        mBeautyOn.setChecked(SharedPreferenceUtils.isBeautyOn(getApplicationContext()));
        if(SharedPreferenceUtils.getDisplayFit(getApplicationContext()) == AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_SCALE_FILL
            .getPreviewDisplayMode()) {
            mDisplayMode.check(R.id.full);
        } else if(SharedPreferenceUtils.getDisplayFit(getApplicationContext()) == AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT
            .getPreviewDisplayMode()) {
            mDisplayMode.check(R.id.fit);
        } else if(SharedPreferenceUtils.getDisplayFit(getApplicationContext()) == AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL
            .getPreviewDisplayMode()) {
            mDisplayMode.check(R.id.cut);
        }
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(PushConfigActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    Toast.makeText(this, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                break;
        }
    }

    private AlivcLivePushConfig getPushConfig() {
        if(mUrl.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.url_empty), Toast.LENGTH_LONG).show();
            return null;
        }
        mAlivcLivePushConfig.setResolution(mDefinition);
        if(!mInitRate.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setInitialVideoBitrate(Integer.valueOf(mInitRate.getText().toString()));
        } else {
            mAlivcLivePushConfig.setInitialVideoBitrate(Integer.valueOf(mInitRate.getHint().toString()));
        }

        if(!mAudioBitRate.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setAudioBitRate(1000*Integer.valueOf(mAudioBitRate.getText().toString()));
        } else {
            mAlivcLivePushConfig.setAudioBitRate(1000*Integer.valueOf(mAudioBitRate.getHint().toString()));
        }

        if(!mMinRate.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setMinVideoBitrate(Integer.valueOf(mMinRate.getText().toString()));
            SharedPreferenceUtils.setMinBit(getApplicationContext(), Integer.valueOf(mMinRate.getText().toString()));
        } else {
            mAlivcLivePushConfig.setMinVideoBitrate(Integer.valueOf(mMinRate.getHint().toString()));
            SharedPreferenceUtils.setMinBit(getApplicationContext(), Integer.valueOf(mMinRate.getHint().toString()));
        }

        if(!mTargetRate.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setTargetVideoBitrate(Integer.valueOf(mTargetRate.getText().toString()));
            SharedPreferenceUtils.setTargetBit(getApplicationContext(), Integer.valueOf(mTargetRate.getText().toString()));
        } else {
            mAlivcLivePushConfig.setTargetVideoBitrate(Integer.valueOf(mTargetRate.getHint().toString()));
            SharedPreferenceUtils.setTargetBit(getApplicationContext(), Integer.valueOf(mTargetRate.getHint().toString()));
        }

        if(!mRetryCount.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setConnectRetryCount(Integer.valueOf(mRetryCount.getText().toString()));
        } else {
            mAlivcLivePushConfig.setConnectRetryCount(DEFAULT_VALUE_INT_AUDIO_RETRY_COUNT);
        }

        if(!mRetryInterval.getText().toString().isEmpty()) {
            mAlivcLivePushConfig.setConnectRetryInterval(Integer.valueOf(mRetryInterval.getText().toString()));
        } else {
            mAlivcLivePushConfig.setConnectRetryInterval(DEFAULT_VALUE_INT_RETRY_INTERVAL);
        }

        if(mWaterMark.isChecked()) {
            for(int i = 0; i < waterMarkInfos.size(); i++) {
                mAlivcLivePushConfig.addWaterMark(waterMarkInfos.get(i).mWaterMarkPath, waterMarkInfos.get(i).mWaterMarkCoordX, waterMarkInfos.get(i).mWaterMarkCoordY, waterMarkInfos.get(i).mWaterMarkWidth);
            }
        }

        mAuthTimeStr = mAuthTime.getText().toString();

        mPrivacyKeyStr = mPrivacyKey.getText().toString();

        return mAlivcLivePushConfig;
    }

    private void addWaterMarkInfo() {
        //添加三个水印，位置坐标不同
        WaterMarkInfo waterMarkInfo = new WaterMarkInfo();
        waterMarkInfo.mWaterMarkPath = Common.waterMark;
        WaterMarkInfo waterMarkInfo1 = new WaterMarkInfo();
        waterMarkInfo1.mWaterMarkPath = Common.waterMark;
        waterMarkInfo.mWaterMarkCoordY += 0.2;
        WaterMarkInfo waterMarkInfo2 = new WaterMarkInfo();
        waterMarkInfo2.mWaterMarkPath = Common.waterMark;
        waterMarkInfo2.mWaterMarkCoordY += 0.4;
        waterMarkInfos.add(waterMarkInfo);
        waterMarkInfos.add(waterMarkInfo1);
        waterMarkInfos.add(waterMarkInfo2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        mUrl.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));  //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            mUrl.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case REQ_CODE_PUSH: {
                if(mWaterLinear != null && mWaterMark.isChecked()) {
                    mWaterMark.setEnabled(false);
                    mWaterPosition.setEnabled(false);
                }
                if(mTargetRate != null && mMinRate != null) {

                    if(!mTargetRate.getText().toString().isEmpty() || Integer.valueOf(mTargetRate.getHint().toString()) != SharedPreferenceUtils.getTargetBit(getApplicationContext())) {
                        mTargetRate.setText(String.valueOf(SharedPreferenceUtils.getTargetBit(getApplicationContext())));
                    }

                    if(!mMinRate.getText().toString().isEmpty() || Integer.valueOf(mMinRate.getHint().toString()) != SharedPreferenceUtils.getMinBit(getApplicationContext())) {
                        mMinRate.setText(String.valueOf(SharedPreferenceUtils.getMinBit(getApplicationContext())));
                    }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                if(manager == null) {
                    manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        if(mAlivcLivePusher != null) {
            try {
                mAlivcLivePusher.destroy();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
        SharedPreferenceUtils.clear(getApplicationContext());
    }

    private void stopPushWithoutSurface()
    {
        if(mAlivcLivePusher != null)
        {
            mAlivcLivePusher.stopPush();
            mAlivcLivePusher.stopPreview();
            mAlivcLivePusher.destroy();
            mAlivcLivePusher.setLivePushInfoListener(null);
            mAlivcLivePusher = null;
        }
        //
        mPushTex.setText(R.string.start_push);
    }

    private void startPushWithoutSurface(String url)
    {
        mAlivcLivePusher = new AlivcLivePusher();
        try 
        {
            mAlivcLivePusher.init(getApplicationContext(),mAlivcLivePushConfig);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mAlivcLivePusher.setLivePushInfoListener(new AlivcLivePushInfoListener() {
            @Override
            public void onPreviewStarted(AlivcLivePusher pusher) {

            }

            @Override
            public void onPreviewStoped(AlivcLivePusher pusher) {

            }

            @Override
            public void onPushStarted(AlivcLivePusher pusher) {
                //
            }

            @Override
            public void onFirstAVFramePushed(AlivcLivePusher pusher) {
            }
            @Override
            public void onPushPauesed(AlivcLivePusher pusher) {

            }

            @Override
            public void onPushResumed(AlivcLivePusher pusher) {

            }

            @Override
            public void onPushStoped(AlivcLivePusher pusher) {

            }

            @Override
            public void onPushRestarted(AlivcLivePusher pusher) {

            }

            @Override
            public void onFirstFramePreviewed(AlivcLivePusher pusher) {

            }

            @Override
            public void onDropFrame(AlivcLivePusher pusher, int countBef, int countAft) {

            }

            @Override
            public void onAdjustBitRate(AlivcLivePusher pusher, int curBr, int targetBr) {

            }

            @Override
            public void onAdjustFps(AlivcLivePusher pusher, int curFps, int targetFps) {

            }
        });

        mAlivcLivePusher.startPreview(null);
        mAlivcLivePusher.startPush(url);

        mPushTex.setText(R.string.stop_button);
    }
}
