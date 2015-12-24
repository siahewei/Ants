package com.mine.base.commom.designpatten.srp;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/12/13
 */
public class SrpImageLoader {
    private ImageCache mImageCache;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void setmImageCache(ImageCache imageCache){
        mImageCache = imageCache;
    }

    public void display(String url, ImageView imageView){
        if (TextUtils.isEmpty(url) || imageView == null){
            return;
        }

        if (mImageCache == null){
            throw new NullPointerException("image cache is null");
        }

        Bitmap bitmap = mImageCache.get(url);

        if (bitmap == null){

        }
    }

    private void submitImageLoad(final String url, ImageView imageView){
        imageView.setTag(url);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = dounloadImage(url);
            }
        });

    }

    private Bitmap dounloadImage(String url){

        Bitmap bitmap = null;

        try {
            URL usls = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
