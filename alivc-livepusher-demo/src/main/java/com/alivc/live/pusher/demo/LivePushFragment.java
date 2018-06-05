package com.alivc.live.pusher.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcLivePushBGMListener;
import com.alivc.live.pusher.AlivcLivePushError;
import com.alivc.live.pusher.AlivcLivePushErrorListener;
import com.alivc.live.pusher.AlivcLivePushInfoListener;
import com.alivc.live.pusher.AlivcLivePushNetworkListener;
import com.alivc.live.pusher.AlivcLivePusher;
import com.faceunity.beautycontrolview.FURenderer;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK;
import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT;

public class LivePushFragment extends Fragment implements Runnable{
    public static final String TAG = "LivePushFragment";

    private static final String URL_KEY = "url_key";
    private static final String ASYNC_KEY= "async_key";
    private static final String AUDIO_ONLY_KEY = "audio_only_key";
    private static final String VIDEO_ONLY_KEY = "video_only_key";
    private static final String QUALITY_MODE_KEY = "quality_mode_key";
    private static final String CAMERA_ID = "camera_id";
    private static final String FLASH_ON = "flash_on";
    private static final String AUTH_TIME = "auth_time";
    private static final String PRIVACY_KEY = "privacy_key";
    private static final String MIX_EXTERN = "mix_extern";
    private static final String MIX_MAIN = "mix_main";
    private final long REFRESH_INTERVAL = 2000;

    private ImageView mExit;
    private ImageView mMusic;
    private ImageView mFlash;
    private ImageView mCamera;
    private ImageView mBeautyButton;
    private TextView mAnswer;
    private LinearLayout mTopBar;
    private TextView mUrl;
    private TextView mIsPushing;
    private LinearLayout mGuide;

    private Button mPreviewButton;
    private Button mPushButton;
    private Button mOperaButton;
    private Button mMore;
    private Button mRestartButton;
    private AlivcLivePusher mAlivcLivePusher = null;
    private String mPushUrl = null;
    private SurfaceView mSurfaceView = null;
    private boolean mAsync = false;

    private boolean mAudio = false;
    private boolean mVideoOnly = false;
    private boolean isPushing = false;
    private Handler mHandler = new Handler();

    private LivePushActivity.PauseState mStateListener = null;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean isFlash = false;
    private boolean mMixExtern = false;
    private boolean mMixMain = false;
    private boolean flashState = true;

    private int mQualityMode = 0;

    ScheduledExecutorService mExecutorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
    private boolean videoThreadOn = false;
    private boolean videoThreadOn2 = false;
    private boolean audioThreadOn = false;

    private MusicDialog mMusicDialog = null;

    private String mAuthString = "?auth_key=%1$d-%2$d-%3$d-%4$s";
    private String mMd5String = "%1$s-%2$d-%3$d-%4$d-%5$s";
    private String mTempUrl = null;
    private String mAuthTime = "";
    private String mPrivacyKey = "";

    Vector<Integer> mDynamicals = new Vector<>();
    private FURenderer mFURenderer;

    public void setFURenderer(FURenderer FURenderer) {
        mFURenderer = FURenderer;
    }

    public static LivePushFragment newInstance(String url, boolean async, boolean mAudio, boolean mVideoOnly , int cameraId, boolean isFlash, int mode, String authTime, String privacyKey, boolean mixExtern, boolean mixMain) {
        LivePushFragment livePushFragment = new LivePushFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url);
        bundle.putBoolean(ASYNC_KEY, async);
        bundle.putBoolean(AUDIO_ONLY_KEY, mAudio);
        bundle.putBoolean(VIDEO_ONLY_KEY, mVideoOnly);
        bundle.putInt(QUALITY_MODE_KEY,mode);
        bundle.putInt(CAMERA_ID, cameraId);
        bundle.putBoolean(FLASH_ON, isFlash);
        bundle.putString(AUTH_TIME, authTime);
        bundle.putString(PRIVACY_KEY, privacyKey);
        bundle.putBoolean(MIX_EXTERN, mixExtern);
        bundle.putBoolean(MIX_MAIN, mixMain);
        livePushFragment.setArguments(bundle);
        return livePushFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mPushUrl = getArguments().getString(URL_KEY);
            mTempUrl = mPushUrl;
            mAsync = getArguments().getBoolean(ASYNC_KEY, false);
            mAudio = getArguments().getBoolean(AUDIO_ONLY_KEY, false);
            mVideoOnly = getArguments().getBoolean(VIDEO_ONLY_KEY, false);
            mCameraId = getArguments().getInt(CAMERA_ID);
            isFlash = getArguments().getBoolean(FLASH_ON, false);
            mMixExtern = getArguments().getBoolean(MIX_EXTERN,false);
            mMixMain = getArguments().getBoolean(MIX_MAIN,false);
            mQualityMode = getArguments().getInt(QUALITY_MODE_KEY);
            mAuthTime = getArguments().getString(AUTH_TIME);
            mPrivacyKey = getArguments().getString(PRIVACY_KEY);
            flashState = isFlash;
        }
        if(mAlivcLivePusher != null) {
            mAlivcLivePusher.setLivePushInfoListener(mPushInfoListener);
            mAlivcLivePusher.setLivePushErrorListener(mPushErrorListener);
            mAlivcLivePusher.setLivePushNetworkListener(mPushNetworkListener);
            mAlivcLivePusher.setLivePushBGMListener(mPushBGMListener);
            isPushing = mAlivcLivePusher.isPushing();
        }

        if(mMixExtern) {
            //startYUV(getActivity());
            //startYUV2(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.push_fragment,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExit = (ImageView) view.findViewById(R.id.exit);
        mMusic = (ImageView) view.findViewById(R.id.music);
        mFlash = (ImageView) view.findViewById(R.id.flash);
        mFlash.setSelected(isFlash);
        mCamera = (ImageView) view.findViewById(R.id.camera);
        mCamera.setSelected(true);
        mPreviewButton = (Button) view.findViewById(R.id.preview_button);
        mPreviewButton.setSelected(false);
        mPushButton = (Button) view.findViewById(R.id.push_button);
        mPushButton.setSelected(true);
        mOperaButton = (Button) view.findViewById(R.id.opera_button);
        mOperaButton.setSelected(false);
        mMore = (Button) view.findViewById(R.id.more);
        mBeautyButton = (ImageView) view.findViewById(R.id.beauty_button);
        mBeautyButton.setSelected(SharedPreferenceUtils.isBeautyOn(getActivity().getApplicationContext()));
        mAnswer = (TextView) view.findViewById(R.id.answer_button);
        mRestartButton = (Button) view.findViewById(R.id.restart_button);
        mTopBar = (LinearLayout) view.findViewById(R.id.top_bar);
        mUrl = (TextView) view.findViewById(R.id.push_url);
        mUrl.setText(mPushUrl);
        mIsPushing = (TextView) view.findViewById(R.id.isPushing);
        mIsPushing.setText(String.valueOf(isPushing));
        mGuide = (LinearLayout) view.findViewById(R.id.guide);
        mExit.setOnClickListener(onClickListener);
        mMusic.setOnClickListener(onClickListener);
        mFlash.setOnClickListener(onClickListener);
        mCamera.setOnClickListener(onClickListener);
        mPreviewButton.setOnClickListener(onClickListener);
        mPushButton.setOnClickListener(onClickListener);
        mOperaButton.setOnClickListener(onClickListener);
        mBeautyButton.setOnClickListener(onClickListener);
        mAnswer.setOnClickListener(onClickListener);
        mRestartButton.setOnClickListener(onClickListener);
        mMore.setOnClickListener(onClickListener);
//        if(SharedPreferenceUtils.isGuide(getActivity().getApplicationContext())) {
//            mGuide.setVisibility(View.VISIBLE);
//            mGuide.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if(mGuide != null) {
//                        mGuide.setVisibility(View.GONE);
//                        SharedPreferenceUtils.setGuide(getActivity().getApplicationContext(), false);
//                    }
//                    return false;
//                }
//            });
//        }

        if(mVideoOnly)
        {
            mMusic.setVisibility(View.GONE);
        }
        if(mAudio)
        {
            mPreviewButton.setVisibility(View.GONE);
        }
        if(mMixMain) {
            mBeautyButton.setVisibility(View.GONE);
            mMusic.setVisibility(View.GONE);
            mFlash.setVisibility(View.GONE);
            mCamera.setVisibility(View.GONE);
        }
        mMore.setVisibility(mAudio ? View.GONE : View.VISIBLE);
//        mTopBar.setVisibility(mAudio ? View.GONE : View.VISIBLE);
        mBeautyButton.setVisibility(mAudio ? View.GONE : View.INVISIBLE);
        mFlash.setVisibility(mAudio ? View.GONE : View.VISIBLE);
        mCamera.setVisibility(mAudio ? View.GONE : View.VISIBLE);
        mFlash.setClickable(mCameraId == CAMERA_TYPE_FRONT.getCameraId() ? false : true);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int id = view.getId();

            if(mAlivcLivePusher == null) {
                if(getActivity() != null) {
                    mAlivcLivePusher = ((LivePushActivity)getActivity()).getLivePusher();
                }

                if(mAlivcLivePusher == null) {
                    return;
                }
            }

            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        switch (id) {
                            case R.id.exit:
                                getActivity().finish();
                                break;
                            case R.id.music:
                                if(mMusicDialog == null) {
                                    mMusicDialog = MusicDialog.newInstance();
                                    mMusicDialog.setAlivcLivePusher(mAlivcLivePusher);
                                }
                                mMusicDialog.show(getFragmentManager(), "beautyDialog");

                                break;
                            case R.id.flash:
                                mAlivcLivePusher.setFlash(!mFlash.isSelected());
                                flashState = !mFlash.isSelected();
                                mFlash.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mFlash.setSelected(!mFlash.isSelected());
                                    }
                                });
                                break;
                            case R.id.camera:
                                if(mCameraId == CAMERA_TYPE_FRONT.getCameraId()) {
                                    mCameraId = CAMERA_TYPE_BACK.getCameraId();
                                } else {
                                    mCameraId = CAMERA_TYPE_FRONT.getCameraId();
                                }
                                mFURenderer.onCameraChange(mCameraId,0);
                                mAlivcLivePusher.switchCamera();
                                mFlash.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mFlash.setClickable(mCameraId == CAMERA_TYPE_FRONT.getCameraId() ? false : true);
                                        if(mCameraId == CAMERA_TYPE_FRONT.getCameraId()) {
                                            mFlash.setSelected(false);
                                        } else {
                                            mFlash.setSelected(flashState);
                                        }
                                    }
                                });

                                break;
                            case R.id.preview_button:
                                final boolean isPreview = mPreviewButton.isSelected();
                                if(mSurfaceView == null && getActivity() != null) {
                                    mSurfaceView = ((LivePushActivity)getActivity()).getPreviewView();
                                }
                                if(!isPreview) {
                                    mAlivcLivePusher.stopPreview();
                                    stopYUV();
                                } else {
                                    if(mAsync) {
                                        mAlivcLivePusher.startPreviewAysnc(mSurfaceView);
                                    } else {
                                        mAlivcLivePusher.startPreview(mSurfaceView);
                                    }
                                    if(mMixExtern) {
                                        //startYUV(getActivity());
                                    }
                                }

                                mPreviewButton.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPreviewButton.setText(isPreview ? getString(R.string.stop_preview_button) : getString(R.string.start_preview_button));
                                        mPreviewButton.setSelected(!isPreview);
                                    }
                                });

                                break;
                            case R.id.push_button:
                                final boolean isPush = mPushButton.isSelected();
                                if(isPush) {
                                    if(mAsync) {
                                        mAlivcLivePusher.startPushAysnc(getAuthString(mAuthTime));
                                    } else {
                                        mAlivcLivePusher.startPush(getAuthString(mAuthTime));
                                    }
                                    if(mMixExtern) {
                                        //startMixPCM(getActivity());
                                    } else if(mMixMain) {
                                        startPCM(getActivity());
                                    }
                                } else {
                                    mAlivcLivePusher.stopPush();
                                    stopPcm();
                                    mOperaButton.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mOperaButton.setText(getString(R.string.pause_button));
                                            mOperaButton.setSelected(false);
                                        }
                                    });
                                    if(mStateListener != null) {
                                        mStateListener.updatePause(false);
                                    }
                                }

                                mPushButton.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPushButton.setText(isPush ? getString(R.string.stop_button) : getString(R.string.start_button));
                                        mPushButton.setSelected(!isPush);
                                    }
                                });

                                break;
                            case R.id.opera_button:
                                final boolean isPause = mOperaButton.isSelected();
                                if(!isPause) {
                                    mAlivcLivePusher.pause();
                                } else {
                                    if(mAsync) {
                                        mAlivcLivePusher.resumeAsync();
                                    } else {
                                        mAlivcLivePusher.resume();
                                    }
                                }

                                if(mStateListener != null) {
                                    mStateListener.updatePause(!isPause);
                                }
                                mOperaButton.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mOperaButton.setText(isPause ? getString(R.string.pause_button) : getString(R.string.resume_button));
                                        mOperaButton.setSelected(!isPause);
                                    }
                                });

                                break;
                            case R.id.beauty_button:
                                PushBeautyDialog pushBeautyDialog = PushBeautyDialog.newInstance(mBeautyButton.isSelected());
                                pushBeautyDialog.setAlivcLivePusher(mAlivcLivePusher);
                                pushBeautyDialog.setBeautyListener(mBeautyListener);
                                pushBeautyDialog.show(getFragmentManager(), "beautyDialog");
                                break;
                            case R.id.answer_button:
                                PushAnswerGameDialog pushAnswerGameDialog = PushAnswerGameDialog.newInstance();
                                pushAnswerGameDialog.setAlivcLivePusher(mAlivcLivePusher);
                                pushAnswerGameDialog.show(getFragmentManager(), "answerDialog");
                                break;
                            case R.id.restart_button:
                                /*if(mMixExtern) {
                                    stopYUV();
                                    stopPcm();
                                }*/
                                if(mAsync) {
                                    mAlivcLivePusher.restartPushAync();
                                } else {
                                    mAlivcLivePusher.restartPush();
                                }
                                /*if(mMixExtern) {
                                    startYUV(getActivity());
                                    startPCM(getActivity());
                                }*/
                                break;
                            case R.id.more:
                                PushMoreDialog pushMoreDialog = new PushMoreDialog();
                                pushMoreDialog.setAlivcLivePusher(mAlivcLivePusher, new DynamicListern() {
                                    @Override
                                    public void onAddDynamic() {
                                        if(mAlivcLivePusher != null && mDynamicals.size() < 5) {
                                            float startX = 0.1f + mDynamicals.size() * 0.2f;
                                            float startY = 0.1f + mDynamicals.size() * 0.2f;
                                            int id = mAlivcLivePusher.addDynamicsAddons(Environment.getExternalStorageDirectory().getPath() + File.separator+"alivc_resource/qizi/",startX, startY, 0.2f, 0.2f);
                                            if(id > 0) {
                                                mDynamicals.add(id);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onRemoveDynamic() {
                                        if(mDynamicals.size() > 0) {
                                            int id = mDynamicals.get(0);
                                            mAlivcLivePusher.removeDynamicsAddons(id);
                                            mDynamicals.remove(0);
                                        }
                                    }
                                });
                                pushMoreDialog.setQualityMode(mQualityMode);
                                pushMoreDialog.setPushUrl(mPushUrl);
                                pushMoreDialog.show(getFragmentManager(), "moreDialog");
                                break;
                            default:
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        showDialog(e.getMessage());
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        showDialog(e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

        }
    };


    public void setAlivcLivePusher(AlivcLivePusher alivcLivePusher) {
        this.mAlivcLivePusher = alivcLivePusher;
    }

    public void setStateListener(LivePushActivity.PauseState listener) {
        this.mStateListener = listener;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }


    AlivcLivePushInfoListener mPushInfoListener = new AlivcLivePushInfoListener() {
        @Override
        public void onPreviewStarted(AlivcLivePusher pusher) {
            showToast(getString(R.string.start_preview));
        }

        @Override
        public void onPreviewStoped(AlivcLivePusher pusher) {
            showToast(getString(R.string.stop_preview));
        }

        @Override
        public void onPushStarted(AlivcLivePusher pusher) {
            showToast(getString(R.string.start_push));
        }

        @Override
        public void onPushPauesed(AlivcLivePusher pusher) {
            showToast(getString(R.string.pause_push));
        }

        @Override
        public void onPushResumed(AlivcLivePusher pusher) {
            showToast(getString(R.string.resume_push));
        }

        @Override
        public void onPushStoped(AlivcLivePusher pusher) {
            showToast(getString(R.string.stop_push));
        }

        /**
         * 推流重启通知
         *
         * @param pusher AlivcLivePusher实例
         */
        @Override
        public void onPushRestarted(AlivcLivePusher pusher) {
            showToast(getString(R.string.restart_success));
        }

        @Override
        public void onFirstFramePreviewed(AlivcLivePusher pusher) {
            showToast(getString(R.string.first_frame));
        }

        @Override
        public void onDropFrame(AlivcLivePusher pusher, int countBef, int countAft) {
            showToast(getString(R.string.drop_frame) + ", 丢帧前："+countBef+", 丢帧后："+countAft);
        }

        @Override
        public void onAdjustBitRate(AlivcLivePusher pusher, int curBr, int targetBr) {
            showToast(getString(R.string.adjust_bitrate) + ", 当前码率："+curBr+"Kps, 目标码率："+targetBr+"Kps");
        }

        @Override
        public void onAdjustFps(AlivcLivePusher pusher, int curFps, int targetFps) {
            showToast(getString(R.string.adjust_fps) + ", 当前帧率："+curFps+", 目标帧率："+targetFps);
        }
    };

    AlivcLivePushErrorListener mPushErrorListener = new AlivcLivePushErrorListener() {

        @Override
        public void onSystemError(AlivcLivePusher livePusher, AlivcLivePushError error) {
            showDialog(getString(R.string.system_error) +  error.toString());
        }

        @Override
        public void onSDKError(AlivcLivePusher livePusher, AlivcLivePushError error) {
            if(error != null) {
                showDialog(getString(R.string.sdk_error) + error.toString());
            }
        }
    };

    AlivcLivePushNetworkListener mPushNetworkListener = new AlivcLivePushNetworkListener() {
        @Override
        public void onNetworkPoor(AlivcLivePusher pusher) {
            showNetWorkDialog(getString(R.string.network_poor));
        }

        @Override
        public void onNetworkRecovery(AlivcLivePusher pusher) {
            showToast(getString(R.string.network_recovery));
        }

        @Override
        public void onReconnectStart(AlivcLivePusher pusher) {

            showToastShort(getString(R.string.reconnect_start));
        }

        @Override
        public void onReconnectFail(AlivcLivePusher pusher) {

            showDialog(getString(R.string.reconnect_fail));
        }

        @Override
        public void onReconnectSucceed(AlivcLivePusher pusher) {
            showToast(getString(R.string.reconnect_success));
        }

        @Override
        public void onSendDataTimeout(AlivcLivePusher pusher) {
            showDialog(getString(R.string.senddata_timeout));
        }

        @Override
        public void onConnectFail(AlivcLivePusher pusher) {
            showDialog(getString(R.string.connect_fail));
        }

        @Override
        public String onPushURLAuthenticationOverdue(AlivcLivePusher pusher) {
            showDialog("流即将过期，请更换url");
            return getAuthString(mAuthTime);
        }

        @Override
        public void onSendMessage(AlivcLivePusher pusher) {
            showToast(getString(R.string.send_message));
        }
    };

    private AlivcLivePushBGMListener mPushBGMListener = new AlivcLivePushBGMListener() {
        @Override
        public void onStarted() {

        }

        @Override
        public void onStoped() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onResumed() {

        }

        @Override
        public void onProgress(final long progress, final long duration) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mMusicDialog != null) {
                        mMusicDialog.updateProgress(progress, duration);
                    }
                }
            });
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onDownloadTimeout() {

        }

        @Override
        public void onOpenFailed() {
            showDialog(getString(R.string.bgm_open_failed));
        }
    };

    @Override
    public void onDestroy() {
        stopPcm();
        stopYUV();
        super.onDestroy();
        if(mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
        }
    }

    private void showToast(final String text) {
        if(getActivity() == null || text == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    private void showToastShort(final String text) {
        if(getActivity() == null || text == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    private void showDialog(final String message) {
        if(getActivity() == null || message == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle(getString(R.string.dialog_title));
                    dialog.setMessage(message);
                    dialog.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void showNetWorkDialog(final String message) {
        if(getActivity() == null || message == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle(getString(R.string.dialog_title));
                    dialog.setMessage(message);
                    dialog.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.setNeutralButton(getString(R.string.reconnect), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                mAlivcLivePusher.reconnectPushAsync(null);
                            } catch (IllegalStateException e) {

                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void run() {
        if(mIsPushing != null && mAlivcLivePusher != null) {
            try {
                isPushing = mAlivcLivePusher.isNetworkPushing();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            AlivcLivePushError error = mAlivcLivePusher.getLastError();
            if(!error.equals(AlivcLivePushError.ALIVC_COMMON_RETURN_SUCCESS)) {
                mIsPushing.setText(String.valueOf(isPushing)+", error code : "+error.getCode());
            } else {
                mIsPushing.setText(String.valueOf(isPushing));
            }
        }
        mHandler.postDelayed(this, REFRESH_INTERVAL);

    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(this);
    }

    public interface BeautyListener {
        void onBeautySwitch(boolean beauty);
    }

    private BeautyListener mBeautyListener = new BeautyListener() {
        @Override
        public void onBeautySwitch(boolean beauty) {
            if(mBeautyButton != null) {
                mBeautyButton.setSelected(beauty);
            }
        }
    };

    private String getMD5(String string) {

        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    private String getUri(String url) {
        String result = "";
        String temp = url.substring(7);
        if(temp != null && !temp.isEmpty()) {
            result = temp.substring(temp.indexOf("/"));
        }
        return result;
    }

//    private void showTimeDialog() {
//        final EditText et = new EditText(getActivity());
//        et.setInputType(InputType.TYPE_CLASS_NUMBER);
//        new AlertDialog.Builder(getContext()).setTitle("输入流鉴权时间")
//                .setView(et)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String input = et.getText().toString();
//                        getAuthString(input);
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .show();
//    }

    private String getAuthString(String time) {
        if (!time.isEmpty() && !mPrivacyKey.isEmpty()) {
            long tempTime = (System.currentTimeMillis() + Integer.valueOf(time))/1000;
            String tempprivacyKey = String.format(mMd5String, getUri(mPushUrl), tempTime, 0, 0, mPrivacyKey);
            String auth = String.format(mAuthString, tempTime, 0, 0, getMD5(tempprivacyKey));
            mTempUrl = mPushUrl + auth;
        } else {
            mTempUrl = mPushUrl;
        }
        return mTempUrl;
    }

    /*public void startYUV(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                videoThreadOn = true;
                byte[] yuv;
                int mixvideId = 0;
                InputStream myInput = null;
                try {
                    File f = new File("/sdcard/alivc_resource/capture0.yuv");
                    myInput = new FileInputStream(f);
                    mixvideId = mAlivcLivePusher.addMixVideo(AlivcImageFormat.IMAGE_FORMAT_YUVNV12,720,1280,0,0.5f,0.5f,0.3f,0.3f);
                    byte[] buffer = new byte[1280*720*3/2];
                    int length = myInput.read(buffer);
                    //发数据
                    while(length > 0 && videoThreadOn)
                    {
                        mAlivcLivePusher.inputMixVideoData(mixvideId,buffer,720,1280, 1280*720*3/2,System.nanoTime()/1000,0);
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发数据
                        length = myInput.read(buffer);
                        if(length < 1280*720*3/2)
                        {
                            mAlivcLivePusher.removeMixVideo(mixvideId);
                            return;
                        }
                    }
                    myInput.close();
                    videoThreadOn = false;
                    mAlivcLivePusher.removeMixVideo(mixvideId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void startYUV2(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                videoThreadOn2 = true;
                byte[] yuv;
                int mixvideId = 0;
                InputStream myInput = null;
                try {
                    File f = new File("/sdcard/alivc_resource/capture0.yuv");
                    myInput = new FileInputStream(f);
                    mixvideId = mAlivcLivePusher.addMixVideo(AlivcImageFormat.IMAGE_FORMAT_YUVNV12,720,1280,0,0.2f,0.2f,0.3f,0.3f);
                    byte[] buffer = new byte[1280*720*3/2];
                    int length = myInput.read(buffer);
                    //发数据
                    while(length > 0 && videoThreadOn2)
                    {
                        mAlivcLivePusher.inputMixVideoData(mixvideId,buffer,720, 1280, 1280*720*3/2,System.nanoTime()/1000,0);
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发数据
                        length = myInput.read(buffer);
                        if(length < 1280*720*3/2)
                        {
                            myInput.close();
                            myInput = new FileInputStream(f);
                            length = myInput.read(buffer);
                        }
                    }
                    mAlivcLivePusher.removeMixVideo(mixvideId);
                    myInput.close();
                    videoThreadOn2 = false;
                    mAlivcLivePusher.removeMixVideo(mixvideId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }*/

    private void stopYUV() {
        videoThreadOn = false;
        videoThreadOn2 = false;
    }

    private void stopPcm() {
        audioThreadOn = false;
    }

    /*private void startMixPCM(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                audioThreadOn = true;
                byte[] pcm;
                int offset = 0;
                int mixAudioId = 0;
                InputStream myInput = null;
                OutputStream myOutput = null;
                boolean reUse = false;
                try {
                    File f = new File("/sdcard/alivc_resource/441.pcm");
                    myInput = new FileInputStream(f);
                    mixAudioId = mAlivcLivePusher.addMixAudio(1, AlivcSoundFormat.SOUND_FORMAT_S16,44100);
                    byte[] buffer = new byte[2048];
                    int length = myInput.read(buffer,0,2048);
                    offset += length;
                    while(length > 0 && audioThreadOn)
                    {
                        reUse = mAlivcLivePusher.inputMixAudioData(mixAudioId,buffer,length, System.nanoTime()/1000);
                        if(reUse) {
                            //发数据
                            length = myInput.read(buffer);
                            offset += length;
                            if (length < 2048) {
                                myInput.close();
                                offset = 0;
                                myInput = new FileInputStream(f);
                                length = myInput.read(buffer);
                                offset += length;
                            }
                        }
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myInput.close();
                    mAlivcLivePusher.removeMixAudio(mixAudioId);
                    audioThreadOn = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    private void startPCM(final Context context) {
        new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private AtomicInteger atoInteger = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("LivePushActivity-readPCM-Thread"+ atoInteger.getAndIncrement());
                return t;
            }
        }).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                audioThreadOn = true;
                byte[] pcm;
                int allSended = 0;
                int sizePerSecond = 44100*2;
                InputStream myInput = null;
                OutputStream myOutput = null;
                boolean reUse = false;
                long startPts = System.nanoTime()/1000;
                try {
                    File f = new File("/sdcard/alivc_resource/441.pcm");
                    myInput = new FileInputStream(f);
                    byte[] buffer = new byte[2048];
                    int length = myInput.read(buffer,0,2048);
                    while(length > 0 && audioThreadOn)
                    {
                        long pts = System.nanoTime()/1000;
                        mAlivcLivePusher.inputStreamAudioData(buffer,length, pts);
                        allSended += length;
                        if((allSended*1000000L/sizePerSecond - 50000) > (pts - startPts))
                        {
                            try {
                                Thread.sleep(45);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        length = myInput.read(buffer);
                        if (length < 2048) {
                            myInput.close();
                            myInput = new FileInputStream(f);
                            length = myInput.read(buffer);
                        }
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myInput.close();
                    audioThreadOn = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface DynamicListern {
        void onAddDynamic();
        void onRemoveDynamic();
    }
}
