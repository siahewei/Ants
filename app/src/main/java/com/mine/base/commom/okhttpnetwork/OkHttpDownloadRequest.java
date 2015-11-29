package com.mine.base.commom.okhttpnetwork;

import android.text.TextUtils;

import com.socks.library.KLog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/29
 */
public class OkHttpDownloadRequest extends BaseRequest {

    private String destFileDir;
    private String destFileName;

    public OkHttpDownloadRequest(String url, String tag, Map<String, String> params, Map<String, String> header,
                                 String destFileDir, String destFileName) {
        super(url, tag, params, header);
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url))
        {
            throw new IllegalArgumentException("url can not be empty!");
        }
        //append params , if necessary
        Request.Builder builder = new Request.Builder();
        //add headers , if necessary
        builder.url(url).tag(tag);
        return builder.build();
    }

    @Override
    public void invokeAsyn(final ResultCallback callback) {
        //super.invokeAsyn(callback);
        prepareInvoked(callback);
        final Call call = mOkHttpClient.newCall(mRequest);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mManager.sendFailure(request, e, ResultCallback.OK_HTTP_ERRO_IO, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String filePath = saveFile(response, callback);
                OkHttpClientManager.getInstance().sendSuccess(filePath, callback);
            }
        });
    }

    private String saveFile(Response response, final ResultCallback callback) {
        InputStream is = null;
        byte[] buf = new byte[2048];

        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            KLog.d("total", total + "");

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    final long finalSum = sum;
                    mManager.getmHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.inProgress(finalSum * 1.0f / total);
                        }
                    });
                }
            }
            fos.flush();

            return file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
