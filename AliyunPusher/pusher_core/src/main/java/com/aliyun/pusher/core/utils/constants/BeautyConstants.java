package com.aliyun.pusher.core.utils.constants;


import com.aliyun.pusher.core.module.BeautyParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akira on 2018/5/30.
 */

public class BeautyConstants {

    public static String FLAVOR = "";

    public final static int BUFFING = 0;
    public final static int WHITENING = 1;
    public final static int RUDDY = 2;
    public final static int SHORTEN_JAW = 3;
    public final static int BIG_EYE = 4;
    public final static int THIN_FACE = 5;
    public final static int RED_LIPS = 6;


    public static final String KEY_WHITE = "white";
    public static final String KEY_BUFFING = "buffing";
    public static final String KEY_RUDDY = "ruddy";
    public static final String KEY_CHEEKPINK = "checkpink";
    public static final String KEY_SLIMFACE = "slimface";
    public static final String KEY_SHORTENFACE = "shortenface";
    public static final String KEY_BIGEYE = "bigeye";

    public static final int DEFAULT_VALUE_INT_BEAUTY_WHITE = 70;
    public static final int DEFAULT_VALUE_INT_BEAUTY_BUFFING = 40;
    public static final int DEFAULT_VALUE_INT_BEAUTY_RUDDY = 40;
    public static final int DEFAULT_VALUE_INT_BEAUTY_CHEEKPINK = 15;
    public static final int DEFAULT_VALUE_INT_BEAUTY_SLIMFACE = 40;
    public static final int DEFAULT_VALUE_INT_BEAUTY_BIGEYE = 30;
    public static final int DEFAULT_VALUE_INT_BEAUTY_SHORTENFACE = 50;

    public final static Map<Integer, BeautyParams> BEAUTY_MAP = new HashMap();

    static {
        final BeautyParams beautyParams_0 = new BeautyParams();
        beautyParams_0.beautyBuffing = 0;
        beautyParams_0.beautyWhite = 0;
        beautyParams_0.beautyRuddy = 0;
        beautyParams_0.beautyCheekPink = 0;
        beautyParams_0.beautyBigEye = 0;
        beautyParams_0.beautySlimFace = 0;
        beautyParams_0.beautyShortenFace = 0;

        final BeautyParams beautyParams_1 = new BeautyParams();
        beautyParams_1.beautyBuffing = 40;
        beautyParams_1.beautyWhite = 35;
        beautyParams_1.beautyRuddy = 10;
        beautyParams_1.beautyCheekPink = 0;
        beautyParams_1.beautyBigEye = 0;
        beautyParams_1.beautySlimFace = 0;
        beautyParams_1.beautyShortenFace = 0;

        final BeautyParams beautyParams_2 = new BeautyParams();
        beautyParams_2.beautyBuffing = 60;
        beautyParams_2.beautyWhite = 80;
        beautyParams_2.beautyRuddy = 20;
        beautyParams_2.beautyCheekPink = 0;
        beautyParams_2.beautyBigEye = 0;
        beautyParams_2.beautySlimFace = 0;
        beautyParams_2.beautyShortenFace = 0;

        final BeautyParams beautyParams_3 = new BeautyParams();
        beautyParams_3.beautyBuffing = 50;
        beautyParams_3.beautyWhite = 100;
        beautyParams_3.beautyRuddy = 20;
        beautyParams_3.beautyCheekPink = 0;
        beautyParams_3.beautyBigEye = 0;
        beautyParams_3.beautySlimFace = 0;
        beautyParams_3.beautyShortenFace = 0;

        final BeautyParams beautyParams_4 = new BeautyParams();
        beautyParams_4.beautyBuffing = 40;
        beautyParams_4.beautyWhite = 70;
        beautyParams_4.beautyRuddy = 40;
        beautyParams_4.beautyCheekPink = 15;
        beautyParams_4.beautyBigEye = 30;
        beautyParams_4.beautySlimFace = 40;
        beautyParams_4.beautyShortenFace = 50;

        final BeautyParams beautyParams_5 = new BeautyParams();
        beautyParams_5.beautyBuffing = 70;
        beautyParams_5.beautyWhite = 100;
        beautyParams_5.beautyRuddy = 10;
        beautyParams_5.beautyCheekPink = 0;
        beautyParams_5.beautyBigEye = 30;
        beautyParams_5.beautySlimFace = 40;
        beautyParams_5.beautyShortenFace = 50;

        BEAUTY_MAP.put(0, beautyParams_0);
        BEAUTY_MAP.put(1, beautyParams_1);
        BEAUTY_MAP.put(2, beautyParams_2);
        BEAUTY_MAP.put(3, beautyParams_3);
        BEAUTY_MAP.put(4, beautyParams_4);
        BEAUTY_MAP.put(5, beautyParams_5);

    }
}
