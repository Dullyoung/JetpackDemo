package com.dullyoung.jetpackdemo.utils;

import android.util.Log;

import com.dullyoung.jetpackdemo.BuildConfig;
import com.google.gson.Gson;


public class LogUtil {
    public static String TAG = "DDDDDD";

    public static void e(Throwable throwable) {
        e(throwable.getStackTrace(), throwable);
    }

    public static void e(StackTraceElement[] stackTraceElements, Object... msg) {
        StackTraceElement element = null;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().startsWith(BuildConfig.APPLICATION_ID) &&
                    !stackTraceElement.getClassName().startsWith(BuildConfig.APPLICATION_ID + ".base")) {
                element = stackTraceElement;
                break;
            }
        }
        if (element == null) {
            return;
        }
        int line = element.getLineNumber();
        String name = element.getFileName();
        Log.e(TAG, "┌───────────────────────────────────────────────────────────────────────────────────────────────────");
        Log.e(TAG, "│ (" + name + ":" + line + ")");
        if (msg.length > 3) {
            for (int i = 0; i < msg.length; i++) {
                Log.e(TAG, "│ Param " + (i + 1) + ": " + new Gson().toJson(msg[i]));
            }
        } else {
            for (Object o : msg) {
                Log.e(TAG, "│ " + new Gson().toJson(o));
            }
        }
        Log.e(TAG, "└───────────────────────────────────────────────────────────────────────────────────────────────────");

    }

    public static void i(Object... msg) {
        int line = Thread.currentThread().getStackTrace()[3].getLineNumber();
        String name = Thread.currentThread().getStackTrace()[3].getFileName();
        Log.i(TAG, "┌───────────────────────────────────────────────────────────────────────────────────────────────────");
        Log.i(TAG, "│ (" + name + ":" + line + ")");
        if (msg.length > 3) {
            for (int i = 0; i < msg.length; i++) {
                Log.i(TAG, "│ Param " + (i + 1) + ": " + new Gson().toJson(msg[i]));
            }
        } else {
            for (Object o : msg) {
                Log.i(TAG, "│ " + new Gson().toJson(o));
            }
        }
        Log.i(TAG, "└───────────────────────────────────────────────────────────────────────────────────────────────────");
    }
}
