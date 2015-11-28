package com.mine.base.commom.imageloader;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mine.MineApplication;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/28
 */
public class ImageUtils {

    public static void initFreso(){
        Fresco.initialize(MineApplication.getInstacne());
    }

    public static void displayImg(SimpleDraweeView view, String url){
        if (TextUtils.isEmpty(url) || view == null){
            throw new IllegalArgumentException("parameter is null");
        }

        Uri uri = Uri.parse(url);
        view.setImageURI(uri);
    }
}
