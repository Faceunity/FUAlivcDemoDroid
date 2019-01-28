/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.aliyun.alivcsolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Mulberry
 */
public class SplashActivity extends Activity implements Handler.Callback{
    private static final int JUMP_TO_MAIN = 1;
    private static final int SPALASH_DELAY_TIME = 2000;

    private Handler jumpHandler = new Handler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalash);
//        initData();
        setJumpToMain();
    }

//    private void initData() {
//        AlivcLiveUserManager.getInstance().init(getApplicationContext());
//    }

    private void setJumpToMain(){
        Message msg = jumpHandler.obtainMessage();
        msg.what = JUMP_TO_MAIN;
        jumpHandler.sendMessageDelayed(msg,SPALASH_DELAY_TIME);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case JUMP_TO_MAIN:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return false;
    }

}
