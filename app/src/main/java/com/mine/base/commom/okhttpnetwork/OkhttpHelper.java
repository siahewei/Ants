package com.mine.base.commom.okhttpnetwork;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * project    Afine
 *
 * @author hewei
 * @verstion 15/11/18
 */
public class OkhttpHelper {

    private static OkHttpClient mClient = new OkHttpClient();
    private static Gson gson = new Gson();

    public static void onGetJson(String url, final onHttpListener l){
        if (!TextUtils.isEmpty(url) && l != null){

            Request request = new Request.Builder()
                    .url(url)
                    .build();

               mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        l.onFailed(e);
                        KLog.d("e:" + e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null){
                            l.onSuccess(response.body().toString());
                            KLog.json(response.body().string());
                        }
                    }
                });
        }
    }

    public interface onHttpListener{
        public void onSuccess(String s);
        public void onFailed(Exception e);
    }

}
