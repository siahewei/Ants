package com.mine.base.utils;

/**
 * project     Afine
 *
 * @author hewei
 * @verstion 15/11/19
 */
public class UrlAddress {

    public enum Env {
        TEST,           // 线上测试环境
        ONLINE,         // 线上正式环境
        LOCAL_TEST     // 本地测试环境
    }

    private static Env mEnv = Env.LOCAL_TEST;

    public static void setEnv(Env env) {
        mEnv = env;
    }

    private static String getTestDomain() {
        if (mEnv == Env.LOCAL_TEST) {
            return "http://192.168.1.140:8000";
        } else {
            return "";
        }
    }

    public static String TEST_URL = getTestDomain() + "/find/";


}
