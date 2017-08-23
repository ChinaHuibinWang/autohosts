package com.qz.autohosts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qz.autohosts.util.DownloadUtil;

/**
 * Created by HuibinWang on 2017/08/21.
 */

public class AboutActivity extends AppCompatActivity {

    private String versionName = "";

    private TextView tvVersion;
    private TextView tvFunctionInfo;
    private TextView tvVersionCheck;
    private TextView tvLog;

    ProgressDialog mProgressDialog;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(R.string.about);

        tvVersion = (TextView) findViewById(R.id.version);
        tvFunctionInfo = (TextView) findViewById(R.id.function_info);
        tvVersionCheck = (TextView) findViewById(R.id.version_check);
        tvLog = (TextView) findViewById(R.id.log);

        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(versionName != ""){
            tvVersion.setText(tvVersion.getText().toString() + " " + versionName);
        }

        tvFunctionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutActivity.this, "Coming Soon...", Toast.LENGTH_LONG).show();
            }
        });

        tvVersionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog(R.string.loading2);
                DownloadUtil.getLatestVersion(AboutActivity.this, new DownloadUtil.GetLatestVersionListener() {
                    @Override
                    public void success(final String version) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] strList = version.split("\n|:");
                                if(strList.length == 4 && strList[1].trim().equals(versionName)) {
                                    Toast.makeText(AboutActivity.this, R.string.version_latest, Toast.LENGTH_LONG).show();
                                } else {
                                    new AlertDialog.Builder(AboutActivity.this)
                                            .setTitle(R.string.dlg_title)
                                            .setMessage(R.string.dlg_info_apkdownload)
                                            .setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    intent.setAction("android.intent.action.VIEW");
                                                    Uri content_url = Uri.parse("https://github.com/ChinaHuibinWang/autohosts/raw/master/autohosts.apk");
                                                    intent.setData(content_url);
                                                    startActivity(intent);
//                                                    showProgressDialog(R.string.downloading);
//                                                    DownloadUtil.downloadAPKFile(AboutActivity.this, new DownloadUtil.DownloadListener() {
//                                                        @Override
//                                                        public void success(final File file) {
//                                                            mHandler.post(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    //AutoInstall.setUrl(Environment.getExternalStorageDirectory() + "/Download/Spore.apk");
//                                                                    AutoInstall.setUrl(file.getAbsolutePath());
//                                                                    AutoInstall.install(AboutActivity.this);
//                                                                    dismissDialog();
//                                                                }
//                                                            });
//                                                        }
//
//                                                        @Override
//                                                        public void error() {
//                                                            mHandler.post(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    Toast.makeText(AboutActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
//                                                                    dismissDialog();
//                                                                }
//                                                            });
//                                                        }
//                                                    });
                                                }
                                            })
                                            .setNegativeButton(R.string.dlg_cancel, null)
                                            .show();
                                }
                                dismissDialog();
                            }
                        });
                    }

                    @Override
                    public void error() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AboutActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                dismissDialog();
                            }
                        });
                    }
                });
            }
        });

        tvLog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showProgressDialog(int resId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(AboutActivity.this);
        }
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage(getString(resId));
        mProgressDialog.show();
    }

    private void dismissDialog() {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.dismiss();
    }
}
