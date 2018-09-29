package com.wind.carmanager.appupdate;

import android.os.Environment;

import java.io.File;

public class Constants {
    //int 常量
    public static final int UPDATE_APP_VERSION_MSG = 0xFFFD;
    public static final int UPDATE_APP_DOWNLOAD_LOADING = 0xFFFC;
    public static final int UPDATE_APP_DOWNLOAD_FAILED = 0xFFFB;
    public static final int UPDATE_APP_SERVICE_ERROR = 0xFFFA;

    public static final String APP_NAME = "AgCarManager.apk";
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AgCarManager";
}
