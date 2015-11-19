package com.mine;

import android.app.Application;

import com.mine.base.utils.InitUtils;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/19
 */
public class MineApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        InitUtils.initToolsConfig();
    }
}
