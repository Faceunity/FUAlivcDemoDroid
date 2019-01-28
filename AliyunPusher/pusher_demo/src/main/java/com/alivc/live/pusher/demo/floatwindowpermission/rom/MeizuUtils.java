/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.alivc.live.pusher.demo.floatwindowpermission.rom;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.alivc.live.pusher.demo.floatwindowpermission.FloatWindowManager;

import java.lang.reflect.Method;

public class MeizuUtils {
    private static final String TAG = "MeizuUtils";

    /**
     * 检测 meizu 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }
    /**
     * 检测 meizu 摄像头，由于魅族MX5的ROM改了很多东西，一般方法不能判断摄像头权限是否开启，只有这样了
     */
    public static boolean checkCameraPermission(Context context) {
        boolean canUse =true;

        Camera mCamera =null;

        try{

            mCamera = Camera.open();

            // setParameters 是针对魅族MX5。MX5通过Camera.open()拿到的Camera对象不为null

            Camera.Parameters mParameters = mCamera.getParameters();

            mCamera.setParameters(mParameters);

        }catch(Exception e) {

            canUse =false;

        }

        if(mCamera !=null) {

            mCamera.release();

        }

        return canUse;


    }
    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e) {
            try {
                Log.e(TAG, "获取悬浮窗权限, 打开AppSecActivity失败, " + Log.getStackTraceString(e));
                // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
                FloatWindowManager.commonROMPermissionApplyInternal(context);
            } catch (Exception eFinal) {
                Log.e(TAG, "获取悬浮窗权限失败, 通用获取方法失败, " + Log.getStackTraceString(eFinal));
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int mode =(int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
                Log.e("TAG","mode:"+mode);
                return AppOpsManager.MODE_ALLOWED == mode;
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private static boolean checkOp(Context context, String op) {
//        final int version = Build.VERSION.SDK_INT;
//        if (version >= 19) {
//            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            try {
//                Class clazz = AppOpsManager.class;
//                Method method = clazz.getDeclaredMethod("checkOp", String.class, int.class, String.class);
//                int mode =(int) method.invoke(manager, op, context.getApplicationInfo().uid, context.getPackageName());
//                Log.e("TAG","mode:"+mode);
//                return AppOpsManager.MODE_ALLOWED == mode;
//            } catch (Exception e) {
//                Log.e(TAG, Log.getStackTraceString(e));
//            }
//        } else {
//            Log.e(TAG, "Below API 19 cannot invoke!");
//        }
//        return false;
//    }
}
