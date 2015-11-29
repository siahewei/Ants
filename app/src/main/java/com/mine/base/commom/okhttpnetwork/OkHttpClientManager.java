package com.mine.base.commom.okhttpnetwork;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/20
 */
public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpClientManager() {

        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());

        int sdk = Build.VERSION.SDK_INT;

        if (sdk >= 23) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);

            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }


    public Handler getmHandler() {
        return mHandler;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public void execute(final Request request, final ResultCallback callback) {

        if (callback == null || request == null) {
            throw new RuntimeException("not set request or callback");
        }

        callback.onBefore(request);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailure(request, e, ResultCallback.OK_HTTP_ERRO_IO, callback);
            }

            @Override
            public void onResponse(Response response) {

                if (response.code() >= 400 && response.code() <= 599) {
                    // 错误码定义范围
                    try {
                        sendFailure(request, new RuntimeException(response.body().string()), response.code(), callback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    String data = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccess(data, callback);
                    }else {
                        Object o = mGson.fromJson(data, callback.mType);
                        sendSuccess(o, callback);
                    }

                } catch (IOException e) {
                    sendFailure(response.request(), e, ResultCallback.OK_HTTP_ERRO_PARSER, callback);
                }catch (JsonParseException e) {
                    sendFailure(response.request(), e, ResultCallback.OK_HTTP_ERRO_IO, callback);
                }
            }
        });

    }

    public void sendSuccess(final Object response, final ResultCallback callback) {
        if (callback == null) {
            throw new RuntimeException("callback is null");
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response);
            }
        });
    }

    public void sendFailure(final Request request, final Exception e, final int httpErrorCode, final ResultCallback callback) {

        if (callback == null) {
            throw new RuntimeException("callback is null");
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e, httpErrorCode);
            }
        });
    }

    private void cancelTag(Object o) {
        if (o != null) {
            mOkHttpClient.cancel(o);
        }
    }

}
