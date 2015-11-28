package com.mine;

import android.app.Application;
import android.content.Context;

import com.mine.base.utils.InitUtils;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/19
 */
public class MineApplication extends Application{

    public static Context instacne;

    @Override
    public void onCreate() {
        super.onCreate();
        instacne = this;
        InitUtils.initToolsConfig();
    }

    public static Context getInstacne() {
        return instacne;
    }
}
