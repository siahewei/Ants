package com.mine.base.utils;

import com.mine.base.commom.imageloader.ImageUtils;
import com.socks.library.KLog;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/19
 */
public class InitUtils {

    public static void initToolsConfig(){
        KLog.init(true);                                    // 打开log开关
        ImageUtils.initFreso();                             // 初始化ImageLoader
        UrlAddress.setEnv(UrlAddress.Env.LOCAL_TEST);       // 设置测试环境
    }


}
