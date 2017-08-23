package com.qz.autohosts.util;

import android.content.Context;
import android.os.AsyncTask;

import com.qz.autohosts.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chenzhiyong on 16/4/17.
 */
public class DownloadUtil {
	private static final String TAG = "DownloadUtil";

	public static void getLatestVersion(final Context context, final GetLatestVersionListener downloadListener) {
		AsyncHttpClient mClient = new AsyncHttpClient();
		String url = "https://github.com/ChinaHuibinWang/autohosts/raw/master/version";
		mClient.get(context, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, final Header[] headers, final byte[] bytes) {
				AsyncTask.execute(new Runnable() {
					@Override
					public void run() {
						OutputStream os = null;
						try {
							long totalLength = 0;
							for (int i = 0; i < headers.length; i++) {
								Header header = headers[i];
								if ("Content-Length".equals(header.getName())) {
									try {
										totalLength = Long.parseLong(header.getValue());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							downloadListener.success(new String(bytes));
						} catch (Exception e) {
							e.printStackTrace();
							downloadListener.error();
						} finally {
							if (os != null) {
								try {
									os.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				downloadListener.error();
			}
		}).setTag(url);
	}

	public static void downloadAPKFile(final Context context, final DownloadListener downloadListener) {
		AsyncHttpClient mClient = new AsyncHttpClient();
		String apkURL = "https://github.com/ChinaHuibinWang/autohosts/raw/master/autohosts.apk";
		mClient.get(context, apkURL, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, final Header[] headers, final byte[] bytes) {
				AsyncTask.execute(new Runnable() {
					@Override
					public void run() {
						OutputStream os = null;
						try {
							long totalLength = 0;
							for (int i = 0; i < headers.length; i++) {
								Header header = headers[i];
								if ("Content-Length".equals(header.getName())) {
									try {
										totalLength = Long.parseLong(header.getValue());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if (bytes == null || bytes.length < totalLength) {
								downloadListener.error();
								return;
							}

							File saveDir = context.getNoBackupFilesDir();
							//File saveDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/autohosts/");
							//File saveDir = new File(Environment.DIRECTORY_DOWNLOADS);
							if (!saveDir.exists()) {
								saveDir.mkdirs();
							}

							File apkFile = new File(saveDir.getAbsolutePath() + File.separator + Constants.DOWNLOAD_APK);
							os = new FileOutputStream(apkFile);
							os.write(bytes);
							os.flush();
							downloadListener.success(apkFile);
						} catch (Exception e) {
							e.printStackTrace();
							downloadListener.error();
						} finally {
							if (os != null) {
								try {
									os.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				downloadListener.error();
			}
		}).setTag(apkURL);
	}

	public static void downloadHostFile(final Context context, String url, final DownloadListener downloadListener) {

		AsyncHttpClient mClient = new AsyncHttpClient();
		mClient.get(context, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, final Header[] headers, final byte[] bytes) {
				AsyncTask.execute(new Runnable() {
					@Override
					public void run() {
						OutputStream os = null;
						try {
							long totalLength = 0;
							for (int i = 0; i < headers.length; i++) {
								Header header = headers[i];
								if ("Content-Length".equals(header.getName())) {
									try {
										totalLength = Long.parseLong(header.getValue());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if (bytes == null || bytes.length < totalLength) {
								downloadListener.error();
								return;
							}

							File saveDir = context.getFilesDir();
							if (!saveDir.exists()) {
								saveDir.mkdirs();
							}

							InputStream is = null;
							String originalHosts = "";
							try {
								is = new FileInputStream(new File("/system/etc/hosts"));
								int length = is.available();
								byte [] buffer = new byte[length];
								is.read(buffer);
								String AllHost = new String(buffer);
								int nIndex = AllHost.indexOf("\n# -----AutoHosts Start-----\n");
								if(nIndex == -1){
									originalHosts = AllHost;
								} else {
									originalHosts = AllHost.substring(0, nIndex);
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								if (is != null) {
									try {
										is.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}

							File hostFile = new File(saveDir.getAbsolutePath() + File.separator + Constants.DOWNLOAD_HOST_NAME);
							os = new FileOutputStream(hostFile);
							if(originalHosts != null && originalHosts != ""){
								os.write(originalHosts.getBytes());
							}
							os.write("\n# -----AutoHosts Start-----\n".getBytes());
							os.write(bytes);
							os.write("\n# -----AutoHosts End-----\n".getBytes());
							downloadListener.success(hostFile);
						} catch (Exception e) {
							e.printStackTrace();
							downloadListener.error();
						} finally {
							if (os != null) {
								try {
									os.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				downloadListener.error();
			}
		}).setTag(url);
	}


	public interface DownloadListener {
		void success(File file);
		void error();
	}

	public interface GetLatestVersionListener {
		void success(String version);
		void error();
	}

//	public interface DownloadapkListener {
//		void success(File file);
//		void error();
//	}

}
