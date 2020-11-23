package com.alivc.live.pusher.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alivc.live.pusher.demo.utils.PreferenceUtil;


public class NeedFaceUnityAcct extends AppCompatActivity {

    private boolean isOn = true;//是否使用FaceUnity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //首次安装按home键置入后台，从桌面图标点击重新启动的问题
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.act_faceunity);

        final Button button = (Button) findViewById(R.id.btn_set);
        String isOpen = PreferenceUtil.getString(LiveApplication.getInstance(), PreferenceUtil.KEY_FACEUNITY_ISON);
        if (TextUtils.isEmpty(isOpen) || isOpen.equals("false")) {
            isOn = false;
        } else {
            isOn = true;
        }
        button.setText(isOn ? "On" : "Off");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = !isOn;
                button.setText(isOn ? "On" : "Off");
            }
        });

        Button btn_to_main = (Button) findViewById(R.id.btn_to_main);
        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NeedFaceUnityAcct.this, MainActivity.class);
                PreferenceUtil.persistString(LiveApplication.getInstance(), PreferenceUtil.KEY_FACEUNITY_ISON,
                        isOn + "");
                startActivity(intent);
                finish();
            }
        });

    }
}
