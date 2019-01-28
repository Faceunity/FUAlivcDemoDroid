package com.alivc.live.pusher.demo.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.SensorManager;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.AlivcResolutionEnum;
import com.alivc.live.pusher.demo.R;
import com.alivc.live.pusher.demo.floatwindowpermission.FloatWindowManager;
import com.alivc.live.pusher.demo.floatwindowpermission.rom.MeizuUtils;
import com.alivc.live.pusher.demo.floatwindowpermission.rom.RomUtils;
import com.alivc.live.pusher.demo.ui.myview.PushQrCreateDialog;
import com.alivc.live.pusher.demo.utils.ClipboardUtils;
import com.alivc.live.pusher.demo.utils.Common;
import com.alivc.live.pusher.demo.utils.VideoRecordViewManager;
import com.google.zxing.activity.CaptureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;

/**
 * 录屏直播界面
 */
public class VideoRecordConfigActivity extends AppCompatActivity {
    private static final String TAG = "VideoRecordConfig";

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
    public static final int CAPTURE_PERMISSION_REQUEST_CODE = 0x1123;
    public static final int OVERLAY_PERMISSION_REQUEST_CODE = 0x1124;

    private InputMethodManager manager;

    private LinearLayout mPublish;
    private SeekBar mResolution;
    private TextView mResolutionText;
    private SeekBar mMicVolume;
    private TextView mMicVolumeText;

    private EditText mUrl;
    private ImageView mQr;
    private ImageView mBack;

    private TextView mPushTex;
    private RadioGroup mOrientation;
    private TextView mNoteText;
    private LinearLayout mNoteLinear;

    private AlivcLivePushConfig mAlivcLivePushConfig;
    private AlivcPreviewOrientationEnum mOrientationEnum = ORIENTATION_PORTRAIT;

    private AlivcLivePusher mAlivcLivePusher = null;
    private int mCaptureVolume = 50;

    private OrientationEventListener mOrientationEventListener;
    private boolean mIsLandscape;
    private int mPresetOrientation;

    private int mLastRotation;
    private ImageView mQrCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video_recording_setting);
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
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Common.copyAsset(VideoRecordConfigActivity.this);
                Common.copyAll(VideoRecordConfigActivity.this);
                return null;
            }
        }.execute();

        mOrientationEventListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation = getDisplayRotation();
                if (rotation != mLastRotation) {
                    mIsLandscape = (rotation % 180) != 0;
                    if(mAlivcLivePusher != null)
                    {
                        mAlivcLivePusher.setScreenOrientation(rotation);
                    }

                    mLastRotation = rotation;
                }
            }
        };
    }

    private int getDisplayRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                break;
        }
        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isHome()) {
            //VideoRecordViewManager.removeVideoRecordWindow(getApplicationContext());
            //VideoRecordViewManager.removeVideoRecordCameraWindow(getApplicationContext());
            VideoRecordViewManager.hideViewRecordWindow();
        }
    }

    @Override
    protected void onStop() {
        if(isHome()) {
            if(mAlivcLivePusher != null && mAlivcLivePusher.isPushing()) {
                VideoRecordViewManager.createViewoRecordWindow(getApplicationContext(), mAlivcLivePusher,cameraOnListener);
                VideoRecordViewManager.showViewRecordWindow();
            }
        }
        super.onStop();

    }

    private VideoRecordViewManager.CameraOn cameraOnListener = new VideoRecordViewManager.CameraOn() {
        @Override
        public void onCameraOn(boolean on) {
            if(on) {
                VideoRecordViewManager.createViewoRecordCameraWindow(getApplicationContext(), mAlivcLivePusher);
            } else {
                VideoRecordViewManager.removeVideoRecordCameraWindow(getApplicationContext());
            }
        }
    };

    private void initView() {
        mUrl = (EditText) findViewById(R.id.url_editor);
        int streamId = (int) (Math.random()*9999);
        mUrl.setText("rtmp://push-demo-rtmp.aliyunlive.com/test/stream"+streamId);

        mPublish = (LinearLayout) findViewById(R.id.beginPublish);
        mPushTex = (TextView) findViewById(R.id.pushStatusTex);
        mResolution = (SeekBar) findViewById(R.id.resolution_seekbar);
        mResolutionText = (TextView) findViewById(R.id.resolution_text);
        mMicVolume = (SeekBar) findViewById(R.id.mic_seekbar);
        mMicVolumeText = (TextView) findViewById(R.id.mic_text);
        mOrientation = (RadioGroup) findViewById(R.id.main_orientation);
        mQr = (ImageView) findViewById(R.id.qr_code);
        mQrCreate = findViewById(R.id.qr_create);
        mBack = (ImageView) findViewById(R.id.iv_back);
        mNoteLinear = (LinearLayout) findViewById(R.id.note_linear);
        mNoteText = (TextView) findViewById(R.id.note_text);
    }

    private void setClick() {
        mPublish.setOnClickListener(onClickListener);
        mResolution.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mMicVolume.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mQr.setOnClickListener(onClickListener);
        mBack.setOnClickListener(onClickListener);
        mOrientation.setOnCheckedChangeListener(mOrientationListener);
        mQrCreate.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.beginPublish) {
                if (getPushConfig() != null) {
                    if (mAlivcLivePusher == null) {
                        //if(VideoRecordViewManager.permission(getApplicationContext()))
                        if (RomUtils.checkIsMeizuRom()){
                            MeizuUtils.checkCameraPermission(VideoRecordConfigActivity.this);
                        }
                        if (FloatWindowManager.getInstance().applyFloatWindow(VideoRecordConfigActivity.this)) {
                            startScreenCapture();
                        }
                    } else {
                        stopPushWithoutSurface();
                    }
                }

            } else if (id == R.id.qr_code) {
                if (ContextCompat.checkSelfPermission(VideoRecordConfigActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(VideoRecordConfigActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }

            } else if (id == R.id.iv_back) {
                finish();


            }else if (id==R.id.qr_create){
                String pullUrl = getPullUrl();
                ClipboardUtils.copyText(VideoRecordConfigActivity.this,pullUrl);
                PushQrCreateDialog qrCreateDialog = PushQrCreateDialog.newInstance(pullUrl);
                qrCreateDialog.show(getSupportFragmentManager(),"qrCreateDialog");

            } else{
            }
        }
    };
    private String getPullUrl(){
        String pullUrl = "rtmp://push-demo.aliyunlive.com/test/";
        String pushUrl = mUrl.getText().toString();
        if (TextUtils.isEmpty(pushUrl)){
            Toast.makeText(this,"推流地址为空",Toast.LENGTH_SHORT).show();
            return pullUrl;
        }
        int index = pushUrl.indexOf("stream");
        if (index<0||index>pushUrl.length()-1){
            return pullUrl;
        }
        String pushId = pushUrl.substring(index);
        return pullUrl+pushId;
    }
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int seekBarId = seekBar.getId();
            if (mResolution.getId() == seekBarId) {
                if (progress <= PROGRESS_0) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_180P;
                    mResolutionText.setText(R.string.setting_resolution_180P);
                } else if (progress > PROGRESS_0 && progress <= PROGRESS_20) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_240P;
                    mResolutionText.setText(R.string.setting_resolution_240P);
                } else if (progress > PROGRESS_20 && progress <= PROGRESS_40) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_360P;
                    mResolutionText.setText(R.string.setting_resolution_360P);
                } else if (progress > PROGRESS_40 && progress <= PROGRESS_60) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_480P;
                    mResolutionText.setText(R.string.setting_resolution_480P);
                } else if (progress > PROGRESS_60 && progress <= PROGRESS_80) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_540P;
                    mResolutionText.setText(R.string.setting_resolution_540P);
                } else if (progress > PROGRESS_80) {
                    mDefinition = AlivcResolutionEnum.RESOLUTION_720P;
                    mResolutionText.setText(R.string.setting_resolution_720P);
                }
            } else if (mMicVolume.getId() == seekBarId) {
                mMicVolumeText.setText(String.valueOf(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (mResolution.getId() == seekBar.getId()) {
                if (progress < PROGRESS_0) {
                    seekBar.setProgress(0);
                } else if (progress > PROGRESS_0 && progress <= PROGRESS_20) {
                    seekBar.setProgress(PROGRESS_20);
                } else if (progress > PROGRESS_20 && progress <= PROGRESS_40) {
                    seekBar.setProgress(PROGRESS_40);
                } else if (progress > PROGRESS_40 && progress <= PROGRESS_60) {
                    seekBar.setProgress(PROGRESS_60);
                } else if (progress > PROGRESS_60 && progress <= PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_80);
                } else if (progress > PROGRESS_80) {
                    seekBar.setProgress(PROGRESS_100);
                }
            } else if(mMicVolume.getId() == seekBar.getId()) {
                mCaptureVolume = progress;
                if(mAlivcLivePusher != null && mAlivcLivePusher.isPushing()) {
                    mAlivcLivePusher.setCaptureVolume(progress);
                }
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mOrientationListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            if (i == R.id.portrait) {
                if (mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_PORTRAIT);
                    mOrientationEnum = ORIENTATION_PORTRAIT;
                    mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push.png");
                    mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network.png");
                }

            } else if (i == R.id.home_left) {
                if (mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_LANDSCAPE_HOME_LEFT);
                    mOrientationEnum = ORIENTATION_LANDSCAPE_HOME_LEFT;
                    VideoRecordViewManager.cameraRotation = 90;
                    mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
                    mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
                }

            } else if (i == R.id.home_right) {
                if (mAlivcLivePushConfig != null) {
                    mAlivcLivePushConfig.setPreviewOrientation(ORIENTATION_LANDSCAPE_HOME_RIGHT);
                    mOrientationEnum = ORIENTATION_LANDSCAPE_HOME_RIGHT;
                    VideoRecordViewManager.cameraRotation = 270;
                    mAlivcLivePushConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push_land.png");
                    mAlivcLivePushConfig.setNetworkPoorPushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/poor_network_land.png");
                }

            } else {
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        VideoRecordViewManager.refreshFloatWindowPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationEventListener != null &&
                mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }

    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(VideoRecordConfigActivity.this, CaptureActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
//        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
//        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
//        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
//        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
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
        if (mUrl.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.url_empty), Toast.LENGTH_LONG).show();
            return null;
        }
        mAlivcLivePushConfig.setResolution(mDefinition);

        return mAlivcLivePushConfig;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        mUrl.setText(data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN));  //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            mUrl.setText(data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case CAPTURE_PERMISSION_REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    mAlivcLivePushConfig.setMediaProjectionPermissionResultData(data);
                    if (mAlivcLivePushConfig.getMediaProjectionPermissionResultData() != null) {
                        if (mAlivcLivePusher == null) {
                            startPushWithoutSurface(mUrl.getText().toString());
                        } else {
                            stopPushWithoutSurface();
                        }
                    }
                }
            }
            break;
            case OVERLAY_PERMISSION_REQUEST_CODE:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                if (manager == null) {
                    manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        VideoRecordViewManager.removeVideoRecordCameraWindow(getApplicationContext());
        VideoRecordViewManager.removeVideoRecordWindow(getApplicationContext());
        if (mAlivcLivePusher != null) {
            try {
                mAlivcLivePusher.stopCamera();
                mAlivcLivePusher.stopPush();
                mAlivcLivePusher.stopPreview();
                mAlivcLivePusher.destroy();
                mAlivcLivePusher.setLivePushInfoListener(null);
                mAlivcLivePusher = null;
            } catch (Exception e) {

            }
        }
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
        super.onDestroy();
    }

    @TargetApi(21)
    private void startScreenCapture() {
        if (Build.VERSION.SDK_INT >= 21) {
            MediaProjectionManager mediaProjectionManager =
                    (MediaProjectionManager) getApplication().getSystemService(
                            Context.MEDIA_PROJECTION_SERVICE);
            this.startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(), CAPTURE_PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(this, "录屏需要5.0版本以上", Toast.LENGTH_LONG).show();
        }
    }

    private void stopPushWithoutSurface() {
        VideoRecordViewManager.removeVideoRecordCameraWindow(getApplicationContext());
        VideoRecordViewManager.removeVideoRecordWindow(getApplicationContext());
        if (mAlivcLivePusher != null) {
            try {
                mAlivcLivePusher.stopCamera();
                mAlivcLivePusher.stopPush();
                mAlivcLivePusher.stopPreview();
                mAlivcLivePusher.destroy();
                mAlivcLivePusher.setLivePushInfoListener(null);
                mAlivcLivePusher = null;
            } catch (Exception e) {
                
            }
        }
        mPushTex.setText(R.string.start_push);
        mNoteLinear.setVisibility(View.VISIBLE);
        mNoteText.setText(getString(R.string.screen_note1));
        mResolution.setEnabled(true);
        setRadioGroup(mOrientation, true);
    }

    private void startPushWithoutSurface(String url) {
        mAlivcLivePusher = new AlivcLivePusher();
        try {
            mAlivcLivePusher.init(getApplicationContext(), mAlivcLivePushConfig);
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

            }

            @Override
            public void onFirstAVFramePushed(AlivcLivePusher alivcLivePusher) {
                
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

        mAlivcLivePusher.setCaptureVolume(mCaptureVolume);
        mPushTex.setText(R.string.stop_button);
        mNoteLinear.setVisibility(View.VISIBLE);
        mNoteText.setText(getString(R.string.screen_note));
        mResolution.setEnabled(false);
        setRadioGroup(mOrientation, false);
    }

    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfos) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    public void setRadioGroup(RadioGroup radioGroup, boolean bool) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(bool);
        }
    }
}
