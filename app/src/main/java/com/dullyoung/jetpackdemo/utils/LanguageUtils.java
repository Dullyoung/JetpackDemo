package com.dullyoung.jetpackdemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.SPUtils;

import java.util.Locale;


public class LanguageUtils {
    public static final int IN_RID = 0;
    public static final int EN = 1;
    public static final int ZH_CN = 2;

    public static Context attachBaseContext(Context context, int language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            return context;
        }
    }

    public static Locale getSupportLanguage(int language) {
        switch (language) {
            case ZH_CN:
                return Locale.CHINA;
            case EN:
                return Locale.ENGLISH;
            default:
                return new Locale("id", "ID");
        }
    }

    public static void applyLanguage(Context context, int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getSupportLanguage(language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DisplayMetrics dm = resources.getDisplayMetrics();
            // apply locale
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, dm);
        } else {
            // updateConfiguration
            DisplayMetrics dm = resources.getDisplayMetrics();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, dm);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Locale locale;
        if (language < 0) {
            // 如果没有指定语言使用系统首选语言
            locale = getSystemPreferredLanguage();
        } else {
            // 指定了语言使用指定语言，没有则使用首选语言
            locale = getLanguageLocale(language);
        }
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, dm);
        return context;
    }

    /**
     * 获取系统首选语言
     *
     * @return Locale
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Locale getSystemPreferredLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }

    public static int getLanguage() {
        return SPUtils.getInstance("language").getInt("language", IN_RID);
    }

    //传给后端用
    public static String getLanguageString() {
        switch (getLanguage()) {
            case IN_RID:
                return "yl";
            case ZH_CN:
                return "cn";
            case EN:
                return "en";
            default:
                return "yl";
        }
    }

    public static String getCurrentLanguageStr() {
        switch (getLanguage()) {
            case IN_RID:
                return "Bahasa Indonesia";
            case EN: {
                return "English";
            }
            case ZH_CN: {
                return "简体中文";
            }
            default:
                return "";
        }

    }

    public static void setLanguage(int language) {
        SPUtils.getInstance("language").put("language", language);
    }

    public static Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public static Locale getCurrentLocal() {
        return getLanguageLocale(getLanguage());
    }

    public static Locale getLanguageLocale(int language) {
        switch (language) {
            case IN_RID: {
                return new Locale("id", "ID");
            }
            case ZH_CN: {
                return Locale.CHINA;
            }
            case EN: {
                return Locale.ENGLISH;
            }
            default:
                return new Locale("id", "ID");
        }
    }
}