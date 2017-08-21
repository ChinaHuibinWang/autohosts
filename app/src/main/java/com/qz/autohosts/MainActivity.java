package com.qz.autohosts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qz.autohosts.util.CloseUtil;
import com.qz.autohosts.util.DownloadUtil;
import com.stericson.RootShell.RootShell;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";

	Button mProxyHostBtn, cleanHostBtn, readHostBtn;
	TextView tipsView;

	ProgressDialog mProgressDialog;
	Handler mHandler = new Handler();

	private boolean isMobileRoot = false;
	private boolean bRemountSuccess = false;
	private String hostURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProxyHostBtn = (Button) findViewById(R.id.proxy_btn);
		cleanHostBtn = (Button) findViewById(R.id.clean_host);
		readHostBtn = (Button) findViewById(R.id.read_host);
		tipsView = (TextView) findViewById(R.id.tips);

		mProxyHostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
				hostURL = preferences.getString("hostURL", "https://raw.githubusercontent.com/yulei88/autohosts/master/data/hosts");

				if (!isMobileRoot) {
					Toast.makeText(getContext(), R.string.get_root_fail, Toast.LENGTH_SHORT).show();
					return;
				}

				bRemountSuccess = false;
				showProgressDialog();
				DownloadUtil.downloadHostFile(MainActivity.this, hostURL, new DownloadUtil.DownloadListener() {
					@Override
					public void success(final File file) {
						Log.e(TAG, "success: Thread = " + Thread.currentThread());
						// 需要host
						AsyncTask.execute(new Runnable() {
							@Override
							public void run() {
								try {
									RootShell.getShell(true);
								} catch (Exception e) {
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											dismissDialog();
										}
									});
									e.printStackTrace();
								}

								try {
									bRemountSuccess = RootTools.remount("/system/etc/hosts", "RW");
									if(!bRemountSuccess){
										mHandler.post(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getContext(), R.string.remount_failed, Toast.LENGTH_LONG).show();
												dismissDialog();
											}
										});
										return;
									}

									RootTools.copyFile(file.getAbsolutePath(), "/system/etc/hosts", true, false);
								} catch (Exception e) {
									e.printStackTrace();
								}

								if(bRemountSuccess){
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getContext(), R.string.get_last_host_tips, Toast.LENGTH_SHORT).show();
											dismissDialog();
										}
									});
								}
							}
						});
					}

					@Override
					public void error() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
								dismissDialog();
							}
						});
					}
				});
			}
		});

		cleanHostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isMobileRoot) {
					Toast.makeText(getContext(), R.string.get_root_fail, Toast.LENGTH_SHORT).show();
					return;
				}

				bRemountSuccess = false;
				AsyncTask.execute(new Runnable() {
					@Override
					public void run() {
						try {
							RootShell.getShell(true);
						} catch (Exception e) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									dismissDialog();
								}
							});
							e.printStackTrace();
						}

						bRemountSuccess = RootTools.remount("/system/etc/hosts", "RW");
						if(!bRemountSuccess){
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getContext(), R.string.remount_failed, Toast.LENGTH_LONG).show();
								}
							});
							return;
						}

						if(bRemountSuccess){
							RootTools.copyFile(getVoidHostPath(), "/system/etc/hosts", true, false);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getContext(), R.string.huifu_host_tips, Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				});
			}
		});

		readHostBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), HostDetailActivity.class);
				startActivity(intent);
			}
		});

		initVoidHost();
		checkMobileIsRoot();

	}

	private void checkMobileIsRoot() {
		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				isMobileRoot = RootShell.isRootAvailable();
				if (!isMobileRoot) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							tipsView.setText(getString(R.string.your_mobile_have_no_root));
							mProxyHostBtn.setEnabled(false);
							cleanHostBtn.setEnabled(false);
						}
					});
				}
			}
		});
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
		}
		mProgressDialog.setTitle("");
		mProgressDialog.setMessage(getString(R.string.loading));
		mProgressDialog.show();
	}

	private void dismissDialog() {
		if (mProgressDialog == null) {
			return;
		}
		mProgressDialog.dismiss();
	}


	public String getRealFileDirPath() {
		File dir = getFilesDir();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}

	private String getVoidHostPath() {
		return getRealFileDirPath() + File.separator + Constants.VOID_HOST_NAME;
	}

	private Context getContext() {
		return this;
	}

	private void initVoidHost() {
		File voidHostFile = new File(getVoidHostPath());
		if (voidHostFile.exists()) {
			return;
		}
		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				File file = new File(getRealFileDirPath() + File.separator + Constants.VOID_HOST_NAME);
				FileWriter fileWriter = null;
				try {
					fileWriter = new FileWriter(file);
					fileWriter.write(Constants.VOID_HOST_VALUE);
					fileWriter.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					CloseUtil.close(fileWriter);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_about) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
