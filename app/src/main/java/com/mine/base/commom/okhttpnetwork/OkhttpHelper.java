package com.mine.base.commom.okhttpnetwork;

import android.text.TextUtils;

import com.mine.base.utils.Constants;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

/**
 * project    Afine
 *
 * @author hewei
 * @verstion 15/11/18
 */
public class OkhttpHelper {

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

    public static void onDownload(String url, String fileName, onHttpListener l){
        if (!TextUtils.isEmpty(url) && l != null){
            new OkHttpDownloadRequest.Builder()
                    .url(url)
                    .destFileDir(Constants.IMAGE_PATH)
                    .destFileName(fileName)
                    .download(new ResultCallback() {
                        @Override
                        protected void onFailure(Request request, Exception e, int httpErr) {

                        }

                        @Override
                        protected void onSuccess(Object o) {
                            KLog.d("path:" + o);
                        }

                        @Override
                        protected void onSuccess(String data) {
                            KLog.d("path:" + data);
                        }

                        @Override
                        protected void inProgress(float progress) {
                            super.inProgress(progress);
                            KLog.d("progress:" + progress);

                        }
                    });
        }

    }


    public static abstract class onHttpListener<T>{
        public abstract void onSuccess(T data);
        public abstract void onFailed(Exception e);
    }

}
