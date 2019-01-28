package com.alivc.live.pusher.demo.ui.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.demo.R;

import java.lang.reflect.Field;

/**
 * 录屏直播, 退到后台的摄像头预览窗口
 */
public class VideoRecordCameraPreviewView extends LinearLayout {

    public static int viewWidth;

    public static int viewHeight;

    private static int statusBarHeight;

    private WindowManager windowManager;

    private WindowManager.LayoutParams mParams;

    private AlivcLivePusher mLivePusher = null;

    private float xInScreen;

    private float yInScreen;

    private float xDownInScreen;

    private float yDownInScreen;

    private float xInView;

    private float yInView;

    private Context mContext;

    private SurfaceView cameraPreview = null;

    private boolean mLastIsLandscape = false;

    public VideoRecordCameraPreviewView(Context context) {
        super(context);
        this.mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.record_camera_preview, this);
        View view = findViewById(R.id.record_camera_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        cameraPreview.getHolder().addCallback(mCallback);
    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if(mLivePusher != null ) {
                mLivePusher.startCamera(cameraPreview);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if(mLivePusher != null) {
                mLivePusher.stopCamera();
            }
        }
    };

    public void refreshPosition()
    {
        updateViewPosition();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setParams(AlivcLivePusher pusher, WindowManager.LayoutParams params) {
        mParams = params;
        mLivePusher = pusher;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public SurfaceView getSurfaceView()
    {
        return cameraPreview;
    }

}
