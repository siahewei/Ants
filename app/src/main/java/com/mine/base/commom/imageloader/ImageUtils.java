package com.mine.base.commom.imageloader;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.mine.MineApplication;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

        view.setAspectRatio(2.0f);
        Uri uri = Uri.parse(url);
        view.setImageURI(uri);
    }

    public static void saveImagesToLocal(String url) {
        if (TextUtils.isEmpty(url)){
            throw new IllegalArgumentException("parameter is null");
        }

        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request);

        if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
            File cacheFile = ((FileBinaryResource) resource).getFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(cacheFile);
                ImageFormat imageFormat = ImageFormatChecker.getImageFormat(fis);

                KLog.d(cacheFile.getAbsolutePath());
                KLog.d(cacheFile.getName());

                switch (imageFormat) {
                    case GIF:
                        //copy cacheFile to sdcard
                        KLog.d("save GIF to loacal");
                        break;

                    case JPEG:
                        KLog.d("save JPEG to loacal");
                        break;

                    case PNG:
                        KLog.d("save PNG to loacal");
                        break;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


