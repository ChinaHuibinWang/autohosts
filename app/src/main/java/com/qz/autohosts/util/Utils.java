package com.qz.autohosts.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HuibinWang on 2017/08/22.
 */

public class Utils {


    public static final String TAG = "Utils";
    public static String logStringCache = "";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "error " + e.getMessage());
        }
        return apiKey;
    }

    public static List<String> getTagsList(String originalText) {
        if (TextUtils.isEmpty(originalText)) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

    public static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());    //获取当前时间
        return formatter.format(curDate);
    }

    public static String formatLog(boolean addDate, String... strs){

        String strReturn = "";
        if (strs.length == 0) {
            return "";
        }

        if(addDate){
            strReturn = getCurrentDate() + "||";
        }

        for (int i = 0; i < strs.length; i++) {
            strReturn += strs[i] + "||";
        }

        return strReturn;
    }

    public static String ParamGet(Context context, String key, String defaultValue){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void ParamSet(Context context, String key, String value){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void LogModify(Context context, String newLog){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String oldLog = preferences.getString("Log", "");
        String Log = newLog + "\n" + oldLog;
        editor.putString("Log", Log);
        editor.commit();
    }

    public static void LogClear(Context context){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Log", "");
        editor.commit();
    }
}
