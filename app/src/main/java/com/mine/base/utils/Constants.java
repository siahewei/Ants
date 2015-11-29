package com.mine.base.utils;

import android.os.Environment;

import java.io.File;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/29
 */
public class Constants {
    /*公用的名称*/
    public static final String COMMON_NAME = "mine";

    /*SD卡对应项目目录名称*/
    public static final String SDCARD_NAME = Environment.getExternalStorageDirectory() + File.separator + COMMON_NAME;
    public static final String IMAGE_PATH = SDCARD_NAME + File.separator + "image" + File.separator;
}
