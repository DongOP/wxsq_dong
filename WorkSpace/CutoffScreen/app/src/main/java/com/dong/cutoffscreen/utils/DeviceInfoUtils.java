package com.dong.cutoffscreen.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Dong on 2018/7/16.
 */

public class DeviceInfoUtils {

    private static final String TAG = "DeviceInfoUtils";
//    private static final DeviceInfoUtils mInstance = new DeviceInfoUtils();
//
//    public static DeviceInfoUtils getInstance() {
//        return mInstance;
//    }
    /**
     * 获取设备的屏幕信息(不含虚拟键)
     *
     * @return
     */
    public static void getWindowInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float density = displayMetrics.density;
        // 获取屏幕dpi,即每英寸包含像素个数
        int dpi = displayMetrics.densityDpi;
        // 获取屏幕像素宽度 px
        int width = displayMetrics.widthPixels;
        // 获取屏幕像素高度 px
        int height = displayMetrics.heightPixels;

        // 夜神虚拟机 屏幕密度：2.0, dpi=320, width=1080, height=1920
        Log.i(TAG, "屏幕密度：" + density + ", dpi=" + dpi + ", width=" + width
                + ", height=" + height);
    }

    // 获取机器的密度(不含虚拟键)
    public static float getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        return density;
    }

    // 获取机器的宽度(不含虚拟键)
    public static int getWidthPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;

        return width;
    }

    // 获取机器的宽度(不含虚拟键)
    public static int getHeightPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;

        return height;
    }
}
