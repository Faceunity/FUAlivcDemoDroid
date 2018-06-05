package com.alivc.live.pusher.demo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.LogUtil;
import com.faceunity.beautycontrolview.FURenderer;

//import com.squareup.leakcanary.LeakCanary;

public class LiveApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new ConnectivityChangedReceiver(), filter);
        if(BuildConfig.DEBUG) {
            LogUtil.enalbeDebug();
        } else {
            LogUtil.disableDebug();
        }
        AlivcLivePusher.showDebugView(this);
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

}
