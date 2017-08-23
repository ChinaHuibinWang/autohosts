package com.qz.autohosts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.qz.autohosts.util.DownloadUtil;
import com.qz.autohosts.util.Utils;
import com.stericson.RootShell.RootShell;
import com.stericson.RootTools.RootTools;

import java.io.File;

/**
 * Created by HuibinWang on 2017/08/21.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    private boolean isMobileRoot = false;
    private boolean bRemountSuccess = false;
    private String newLog = "";
    private Context m_Context = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        m_Context = context;
        if (intent.getAction().equals(action_boot)){
            isMobileRoot = RootShell.isRootAvailable();
            if (isMobileRoot) {
                bRemountSuccess = RootTools.remount("/system/etc/hosts", "RW");
                if(bRemountSuccess){
                    String hostURL = Utils.ParamGet(context, "hostURL", "https://raw.githubusercontent.com/yulei88/autohosts/master/data/hosts");
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
                                    newLog = Utils.formatLog(true, Constants.LOG_GET_HOST_SUCCESS_WHEN_REBOOT);
                                    Utils.LogModify(m_Context, newLog);
                                }
                            });
                        }

                        @Override
                        public void error() {
                            Log.e(TAG, "error");
                            newLog = Utils.formatLog(true, Constants.LOG_GET_HOST_FAILED_WHEN_REBOOT, Constants.LOG_REASON_FileUsed, Constants.LOG_SUGGEST_Reboot);
                            Utils.LogModify(m_Context, newLog);
                        }
                    });
                } else {
                    newLog = Utils.formatLog(true, Constants.LOG_GET_HOST_FAILED_WHEN_REBOOT, Constants.LOG_REASON_FileUsed, Constants.LOG_SUGGEST_Reboot);
                    Utils.LogModify(context, newLog);
                }
            } else {
                newLog = Utils.formatLog(true, Constants.LOG_GET_HOST_FAILED_WHEN_REBOOT, Constants.LOG_REASON_NOROOT, Constants.LOG_SUGGEST_Root);
                Utils.LogModify(context, newLog);
            }
        }
    }
}

