package com.qz.autohosts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.qz.autohosts.util.DownloadUtil;
import com.stericson.RootShell.RootShell;
import com.stericson.RootTools.RootTools;

import java.io.File;

/**
 * Created by HuibinWang on 2017/08/21.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)){
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String hostURL = preferences.getString("hostURL", "https://raw.githubusercontent.com/yulei88/autohosts/master/data/hosts");
            DownloadUtil.downloadHostFile(context, hostURL, new DownloadUtil.DownloadListener() {
                @Override
                public void success(final File file) {
                    Log.e(TAG, "success: Thread = " + Thread.currentThread());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RootShell.getShell(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RootTools.copyFile(file.getAbsolutePath(), "/system/etc/hosts", true, false);
                        }
                    });
                }

                @Override
                public void error() {
                    Log.e(TAG, "error");
                }
            });
        }
    }
}

