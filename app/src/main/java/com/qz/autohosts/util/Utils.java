package com.qz.autohosts.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HuibinWang on 2017/08/22.
 */

public class Utils {
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
