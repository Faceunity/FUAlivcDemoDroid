package com.aliyun.alivcsolution;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;

import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.LogUtil;
import com.alivc.live.pusher.demo.BuildConfig;
import com.aliyun.pusher.core.utils.FileUtils;
import com.faceunity.beautycontrolview.FURenderer;

//import com.squareup.leakcanary.LeakCanary;

public class MutiApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new ConnectivityChangedReceiver(), filter);

        AlivcLivePusher.showDebugView(this);
        //复制asset中的资源到内存卡
        copyTask.execute();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        FURenderer.initFURenderer(this);
    }


    class ConnectivityChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    private AsyncTask copyTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            FileUtils.copyAll(getApplicationContext());
            return null;
        }
    };

}
