package com.alivc.live.pusher.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.alivc.live.pusher.AlivcPreviewDisplayMode;

import static com.alivc.live.pusher.AlivcLivePushConstants.DEFAULT_VALUE_AUTO_FOCUS;
import static com.alivc.live.pusher.AlivcLivePushConstants.DEFAULT_VALUE_PREVIEW_MIRROR;
import static com.alivc.live.pusher.AlivcLivePushConstants.DEFAULT_VALUE_PUSH_MIRROR;

public class SharedPreferenceUtils {

    private static final String SHAREDPRE_FILE = "livepush";
    private static final String WHITE = "white";
    private static final String BUFFING = "buffing";
    private static final String RUDDY = "ruddy";
    private static final String CHEEKPINK = "cheekpink";
    private static final String BRIGHTNESS = "brightness";
    private static final String SLIMFACE = "slimface";
    private static final String SHORTENFACE = "shortenface";
    private static final String BIGEYE = "bigeye";
    private static final String AUTOFOCUS = "autofocus";
    private static final String PREVIEW_MIRROR = "previewmirror";
    private static final String PUSH_MIRROR = "pushmirror";
    private static final String TARGET_BIT = "target_bit";
    private static final String MIN_BIT = "min_bit";
    private static final String SHOWGUIDE = "guide";
    private static final String BEAUTYON = "beautyon";
    private static final String HINT_TARGET_BIT = "hint_target_bit";
    private static final String HINT_MIN_BIT = "hint_min_bit";
    private static final String DISPLAY_FIT = "display_fit";

    public static void setWhiteValue(Context context, int white) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(WHITE, white);
        editor.commit();
    }



    public static void setPreviewMirror(Context context, boolean previewMirror) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(PREVIEW_MIRROR, previewMirror);
        editor.commit();
    }

    public static boolean isPreviewMirror(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        boolean previewMirror = sharedPreferences.getBoolean(PREVIEW_MIRROR, DEFAULT_VALUE_PREVIEW_MIRROR);
        return previewMirror;
    }

    public static void setPushMirror(Context context, boolean pushMirror) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(PUSH_MIRROR, pushMirror);
        editor.commit();
    }

    public static boolean isPushMirror(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        boolean pushMirror = sharedPreferences.getBoolean(PUSH_MIRROR, DEFAULT_VALUE_PUSH_MIRROR);
        return pushMirror;
    }

    public static void setAutofocus(Context context, boolean autofocus) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(AUTOFOCUS, autofocus);
        editor.commit();
    }

    public static boolean isAutoFocus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        boolean autoFocus = sharedPreferences.getBoolean(AUTOFOCUS, DEFAULT_VALUE_AUTO_FOCUS);
        return autoFocus;
    }

    public static void setTargetBit(Context context, int target) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(TARGET_BIT, target);
        editor.commit();
    }

    public static int getTargetBit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        int target = sharedPreferences.getInt(TARGET_BIT, 0);
        return target;
    }

    public static void setMinBit(Context context, int min) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(MIN_BIT, min);
        editor.commit();
    }

    public static int getMinBit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        int min = sharedPreferences.getInt(MIN_BIT, 0);
        return min;
    }

    public static void setHintTargetBit(Context context, int hintTarget) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(HINT_TARGET_BIT, hintTarget);
        editor.commit();
    }

    public static int getHintTargetBit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        int hintTarget = sharedPreferences.getInt(HINT_TARGET_BIT, 0);
        return hintTarget;
    }

    public static void setHintMinBit(Context context, int hintMin) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(HINT_MIN_BIT, hintMin);
        editor.commit();
    }

    public static int getHintMinBit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        int hintMin = sharedPreferences.getInt(HINT_MIN_BIT, 0);
        return hintMin;
    }

    public static void setGuide(Context context, boolean guide) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOWGUIDE, guide);
        editor.commit();
    }

    public static boolean isGuide(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        boolean autoFocus = sharedPreferences.getBoolean(SHOWGUIDE, true);
        return autoFocus;
    }

    public static void setBeautyOn(Context context, boolean beautyOn) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(BEAUTYON, beautyOn);
        editor.commit();
    }

    public static boolean isBeautyOn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        boolean beautyOn = sharedPreferences.getBoolean(BEAUTYON, true);
        return beautyOn;
    }

    public static void setDisplayFit(Context context, int displayfit) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(DISPLAY_FIT, displayfit);
        editor.commit();
    }

    public static int getDisplayFit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
            Activity.MODE_PRIVATE);
        int displayfit = sharedPreferences.getInt(DISPLAY_FIT, AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FIT

            .getPreviewDisplayMode());
        return displayfit;
    }

    public static void clear(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove(WHITE);
        editor.remove(BUFFING);
        editor.remove(RUDDY);
        editor.remove(BRIGHTNESS);
        editor.remove(CHEEKPINK);
        editor.remove(AUTOFOCUS);
        editor.remove(PREVIEW_MIRROR);
        editor.remove(PUSH_MIRROR);
        editor.remove(TARGET_BIT);
        editor.remove(MIN_BIT);
        editor.remove(BEAUTYON);
        editor.remove(DISPLAY_FIT);
        editor.remove(HINT_MIN_BIT);
        editor.remove(HINT_TARGET_BIT);
        editor.commit();
    }
}
