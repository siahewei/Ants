package com.mine.base.commom.okhttpnetwork;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * project    Afine
 *
 * @author hewei
 * @verstion 15/11/18
 */
public class OkhttpHelper {

    private static OkHttpClient mClient = new OkHttpClient();
    private static Gson gson = new Gson();

    public static <T> void onGetBean(String url, final onHttpListener<T> l){
        if (!TextUtils.isEmpty(url) && l != null){
            new OkHttpGetRequest.Builder()
                    .url(url)
                    .get(new ResultCallback() {
                        @Override
                        protected void onFailure(Request request, Exception e, int httpErr) {
                            KLog.e(e + "");
                            l.onFailed(e);
                        }

                        @Override
                        protected void onSuccess(Object o) {

                            KLog.d("object", o.toString());
                            l.onSuccess((T)o);
                        }

                        @Override
                        protected void onSuccess(String data) {
                            KLog.d("String" ,data);
                        }
                    });
        }
    }

    public static abstract class onHttpListener<T>{
        public abstract void onSuccess(T data);
        public abstract void onFailed(Exception e);
    }

}
