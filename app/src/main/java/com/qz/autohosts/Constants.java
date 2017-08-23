package com.qz.autohosts;

/**
 * Created by chenzhiyong on 16/4/17.
 */
public interface Constants {

	String SYSTEM_HOST_FILE_PATH = "/system/etc/hosts";
	String VOID_HOST_NAME = "void_host";
	String DOWNLOAD_HOST_NAME = "download_host";
	String DOWNLOAD_APK = "autohosts.apk";
	String VOID_HOST_VALUE = "127.0.0.1 localhost";

	String LOG_GET_HOST_SUCCESS_WHEN_REBOOT = "LogGetHostSuccessWhenReboot";
	String LOG_GET_HOST_SUCCESS_WHEN_ACTIVE = "LogGetHostSuccessWhenActive";
	String LOG_GET_HOST_FAILED_WHEN_REBOOT = "LogGetHostFailedWhenReboot";
	String LOG_GET_HOST_FAILED_WHEN_ACTIVE = "LogGetHostFailedWhenActive";
	String LOG_REASON_FileUsed = "LogReasonFileUsed";
	String LOG_REASON_NOROOT = "LogReasonNoRoot";
	String LOG_SUGGEST_Root = "LogSuggestRoot";
	String LOG_SUGGEST_Reboot = "LogSuggestReboot";

}
